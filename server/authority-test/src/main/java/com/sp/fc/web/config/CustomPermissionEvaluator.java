package com.sp.fc.web.config;

import com.sp.fc.web.service.Paper;
import com.sp.fc.web.service.PaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private PaperService paperService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        Paper paper = paperService.getPaper((Long)targetId);
        if(paper == null) throw new AccessDeniedException("시험지가 존재 하지 않습니다.");
        boolean canUse = paper.getStudentIds().stream().anyMatch(userId -> userId.equals(authentication.getName()));
        return canUse;
    }
}
