package com.dada.hrm.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dada.hrm.dto.UserRequest;
import com.dada.hrm.entity.AppUser;
import com.dada.hrm.entity.UserRole;
import com.dada.hrm.repository.AppUserRepository;
import com.dada.hrm.service.SessionAccessService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/admin/users")
public class AdminApiController {
    private final AppUserRepository userRepository;
    private final SessionAccessService sessionAccessService;

    public AdminApiController(AppUserRepository userRepository, SessionAccessService sessionAccessService) {
        this.userRepository = userRepository;
        this.sessionAccessService = sessionAccessService;
    }

    @GetMapping
    public List<AppUser> listUsers(HttpSession session) {
        sessionAccessService.requireAdmin(session);
        return userRepository.findAll();
    }

    @GetMapping("/engineers")
    public List<AppUser> engineers(HttpSession session) {
        sessionAccessService.requireAdmin(session);
        return userRepository.findByRoleOrderByFullName(UserRole.ENGINEER);
    }

    @PostMapping
    public AppUser createUser(@RequestBody UserRequest request, HttpSession session) {
        sessionAccessService.requireAdmin(session);
        AppUser user = new AppUser();
        user.setUsername(request.username());
        user.setPassword(request.password());
        user.setFullName(request.fullName());
        user.setRole(request.role());
        return userRepository.save(user);
    }
}
