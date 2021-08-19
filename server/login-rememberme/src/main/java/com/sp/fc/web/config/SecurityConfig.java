package com.sp.fc.web.config;

import com.sp.fc.user.service.SpUserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.servlet.http.HttpSessionEvent;
import java.time.LocalDateTime;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SpUserService spUserService;

    public SecurityConfig(SpUserService spUserService) {
        this.spUserService = spUserService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(spUserService);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    @Bean
    SessionRegistry sessionRegistry() {
        return  new SessionRegistryImpl();
    }


    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(
                new HttpSessionEventPublisher() {
                    @Override
                    public void sessionCreated(HttpSessionEvent event) {
                        super.sessionCreated(event);
                        System.out.printf("====> [%s] 세션 생성됨. %s \n", LocalDateTime.now(), event.getSession().getId());
                    }

                    @Override
                    public void sessionDestroyed(HttpSessionEvent event) {
                        super.sessionDestroyed(event);
                        System.out.printf("====> [%s] 세션 만료됨. %s \n", LocalDateTime.now(), event.getSession().getId());
                    }

                    @Override
                    public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
                        super.sessionIdChanged(event, oldSessionId);
                        System.out.printf("====> [%s] 세션 아이디가 변경됨. %s \n", LocalDateTime.now(), event.getSession().getId());
                    }
                }
        );
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(request->
                    request.antMatchers("/").permitAll()
                            .anyRequest().authenticated()


                )
                .formLogin(login->
                        login.loginPage("/login")
                        .loginProcessingUrl("/loginprocess")
                        .permitAll()
                        .defaultSuccessUrl("/", false)
                        .failureUrl("/login-error")
                )
                .logout(logout->
                        logout.logoutSuccessUrl("/"))
                .exceptionHandling(error->
                        error//.accessDeniedPage("/access-denied")
                                .accessDeniedHandler(new CustomDeniedHandler())
                )
                .sessionManagement(
                        s -> s
                                .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::changeSessionId)
                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                                .maximumSessions(1)
                                .maxSessionsPreventsLogin(false)  //(true 인경우)session 개수를 초과한 경우 로그인 자체를 막아버림.
                                .expiredUrl("/session-expired")
                )

                ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations(),
                        PathRequest.toH2Console()
                )
        ;
    }

}
