package com.sp.fc.web.teacher;

import com.sp.fc.web.student.Student;
import com.sp.fc.web.student.StudentAuthenticationToken;
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
public class TeacherManger implements AuthenticationProvider, InitializingBean {
    private HashMap<String, Teacher> teacherDB = new HashMap<>();  //더비 DB, 실제로는 디비에서 가져와야함.

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        TeacherAuthenticationToken token = (TeacherAuthenticationToken) authentication;
        System.out.println("token name is " + token.getName());
        //여기서 DB에서 해당 username과 password로 조회
        if(teacherDB.containsKey(token.getName())) {
            Teacher teacher = teacherDB.get(token.getCredentials());
            return TeacherAuthenticationToken.builder()
                    .principal(teacher)
                    .details(teacher.getUsername())  //실제로는 Request 정보를 담아야함.
                    .authenticated(true)
                    .build();
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == TeacherAuthenticationToken.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<GrantedAuthority> s1Authority = new HashSet<>();
        s1Authority.add(new SimpleGrantedAuthority("ROLE_TEACHER"));
        Teacher s1 = new Teacher("choi","최선생",   s1Authority);

        teacherDB.put(s1.getId(), s1);

    }
}
