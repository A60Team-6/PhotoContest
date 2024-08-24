package com.telerikacademy.web.photocontest.sercices.contracts;

import com.telerikacademy.web.photocontest.models.Role;

import java.util.UUID;

public interface RoleService {
    Role getRoleById(UUID id);
}
