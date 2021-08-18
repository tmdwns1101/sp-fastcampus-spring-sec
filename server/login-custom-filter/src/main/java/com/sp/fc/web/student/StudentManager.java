package com.sp.fc.web.student;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
public class StudentManager implements AuthenticationProvider, InitializingBean { //InitializingBean은 실제로 필요 없음. 실제 DB에서 조회 하는 로직이 필요

    private HashMap<String, Student> studentDB = new HashMap<>();  //더비 DB, 실제로는 디비에서 가져와야함.

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        StudentAuthenticationToken token = (StudentAuthenticationToken) authentication;
        System.out.println("token name is " + token.getName());
        //여기서 DB에서 해당 username과 password로 조회
        if(studentDB.containsKey(token.getName())) {
            Student student = studentDB.get(token.getCredentials());
            return StudentAuthenticationToken.builder()
                    .principal(student)
                    .details(student.getUsername())  //실제로는 Request 정보를 담아야함.
                    .authenticated(true)
                    .build();
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == StudentAuthenticationToken.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<GrantedAuthority> s1Authority = new HashSet<>();
        s1Authority.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
        Student s1 = new Student("hong","홍길동",   s1Authority);
        Set<GrantedAuthority> s2Authority = new HashSet<>();
        s2Authority.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
        Student s2 = new Student("go","고길동",   s2Authority);
        Set<GrantedAuthority> s3Authority = new HashSet<>();
        s3Authority.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
        Student s3 = new Student("rang","호랑이",   s3Authority);
        studentDB.put(s1.getId(),s1);
        studentDB.put(s2.getId(), s2);
        studentDB.put(s3.getId(), s3);

    }
}
