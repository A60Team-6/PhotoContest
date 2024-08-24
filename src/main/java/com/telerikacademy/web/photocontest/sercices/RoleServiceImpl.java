package com.telerikacademy.web.photocontest.sercices;

import com.telerikacademy.web.photocontest.models.Role;
import com.telerikacademy.web.photocontest.repositories.RoleRepository;
import com.telerikacademy.web.photocontest.sercices.contracts.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleById(UUID id) {
        return roleRepository.getReferenceById(id);
    }
}
