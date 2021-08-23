package com.sp.fc.web.controller;

import com.sp.fc.web.service.Paper;
import com.sp.fc.web.service.PaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/papers")
@RequiredArgsConstructor
public class PaperController {

    private final PaperService paperService;

    @PreAuthorize("isTutor()")
//    @PostFilter("filterObject.state != T(com.sp.fc.web.service.Paper.State).PREPARE")
    @GetMapping("/my-papers")
    public List<Paper> getMyPapers(@AuthenticationPrincipal User user) {
        return paperService.getMyPapers(user.getUsername());
    }

    @PreAuthorize("hasPermission(#paperId, 'paper','read')")
    @GetMapping("/{paperId}")
    public Paper getPaper(@AuthenticationPrincipal User user, @PathVariable Long paperId) {
        return paperService.getPaper(paperId);
    }
}
