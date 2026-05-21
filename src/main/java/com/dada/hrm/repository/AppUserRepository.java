package com.dada.hrm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dada.hrm.entity.AppUser;
import com.dada.hrm.entity.UserRole;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    List<AppUser> findByRoleOrderByFullName(UserRole role);
}
