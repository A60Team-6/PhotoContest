package com.telerikacademy.web.photocontest;
import com.telerikacademy.web.photocontest.entities.Role;
import com.telerikacademy.web.photocontest.repositories.RoleRepository;
import com.telerikacademy.web.photocontest.services.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRoleById_ShouldReturnRole_WhenRoleExists() {
        // Arrange
        UUID roleId = UUID.randomUUID();
        Role role = new Role();
        role.setRoleId(roleId);

        when(roleRepository.getReferenceById(roleId)).thenReturn(role);

        // Act
        Role result = roleService.getRoleById(roleId);

        // Assert
        assertNotNull(result);
        assertEquals(roleId, result.getRoleId());
        verify(roleRepository, times(1)).getReferenceById(roleId);
    }

    @Test
    void testGetRoleById_ShouldReturnNull_WhenRoleDoesNotExist() {
        // Arrange
        UUID roleId = UUID.randomUUID();
        when(roleRepository.getReferenceById(roleId)).thenReturn(null);

        // Act
        Role result = roleService.getRoleById(roleId);

        // Assert
        assertNull(result);
        verify(roleRepository, times(1)).getReferenceById(roleId);
    }

    @Test
    void testGetRoleByName_ShouldReturnRole_WhenRoleExists() {
        // Arrange
        String roleName = "Admin";
        Role role = new Role();
        role.setName(roleName);

        when(roleRepository.findByName(roleName)).thenReturn(role);

        // Act
        Role result = roleService.getRoleByName(roleName);

        // Assert
        assertNotNull(result);
        assertEquals(roleName, result.getName());
        verify(roleRepository, times(1)).findByName(roleName);
    }

    @Test
    void testGetRoleByName_ShouldReturnNull_WhenRoleDoesNotExist() {
        // Arrange
        String roleName = "NonExistentRole";
        when(roleRepository.findByName(roleName)).thenReturn(null);

        // Act
        Role result = roleService.getRoleByName(roleName);

        // Assert
        assertNull(result);
        verify(roleRepository, times(1)).findByName(roleName);
    }
}
