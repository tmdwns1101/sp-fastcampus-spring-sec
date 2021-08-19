package com.sp.fc.web.error;

import org.springframework.security.access.AccessDeniedException;

public class AccessDeniedUserPageException extends AccessDeniedException {
    
    public AccessDeniedUserPageException() {
        super("유저페이지 접근 불가");
    }
}
