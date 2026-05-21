package com.dada.hrm.dto;

import com.dada.hrm.entity.UserRole;

public record UserRequest(String username, String password, String fullName, UserRole role) {
}
