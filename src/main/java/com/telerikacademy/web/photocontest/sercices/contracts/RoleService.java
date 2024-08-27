package com.telerikacademy.web.photocontest.sercices.contracts;

import com.telerikacademy.web.photocontest.entities.Role;

import java.util.UUID;

public interface RoleService {
    Role getRoleById(UUID id);

    Role getRoleByName(String name);
}
