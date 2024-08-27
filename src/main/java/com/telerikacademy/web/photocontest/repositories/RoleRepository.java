package com.telerikacademy.web.photocontest.repositories;

import com.telerikacademy.web.photocontest.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Role findByName(String name);
}
