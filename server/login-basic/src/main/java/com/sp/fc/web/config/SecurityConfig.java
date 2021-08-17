package com.sp.fc.web.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthDetails customAuthDetails;

    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(
                        User.withDefaultPasswordEncoder()   //develop 단계에서 테스트 용도로만 사용!!! bcrypt 같은 암호화 인코더 사용 바람.
                                .username("user1")
                                .password("1234")
                                .roles("USER")
                )
                .withUser(
                        User.withDefaultPasswordEncoder()
                                .username("user2")
                                .password("1234")
                                .roles("ADMIN")
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(request->{
                    request
                            .antMatchers("/").permitAll()
                            .anyRequest().authenticated();
                })
                .formLogin(
                        login -> login.loginPage("/login")
                                .permitAll()
                                .defaultSuccessUrl("/", false) //기본적으로 로그인 성공시 루트 경로로 가지만,
                                                                                        // alwaysUse 를 false로 하면 로그인 화면으로 리다이렉트 됬던 경로로 돌아감.
                                .failureUrl("/login-error")
                                .authenticationDetailsSource(customAuthDetails)
                )
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .exceptionHandling(
                        exception -> exception.accessDeniedPage("/access-denied")
                )
        ;
    }

    //css 나 js 같은 정적 web resource 를 security 관리에서 제외 한다.
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                );
    }


}
