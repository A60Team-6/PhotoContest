package com.telerikacademy.web.photocontest;

import com.telerikacademy.web.photocontest.entities.Role;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.*;
import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.repositories.UserRepository;
import com.telerikacademy.web.photocontest.services.UserServiceImpl;
import com.telerikacademy.web.photocontest.services.contracts.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConversionService conversionService;

    @Mock
    private RoleService roleService;

    @Mock
    private RankService rankService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private PhotoService photoService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUserEntities_ShouldReturnAllUserEntities() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUserEntities();

        // Assert
        assertEquals(users.size(), result.size());
        assertEquals(users, result);
        verify(userRepository, times(1)).findAll();
    }


    // Тест за метода getAll
    @Test
    void testGetAll_ShouldReturnAllUsers() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(new User());
        when(userRepository.findAllByIsActiveTrue()).thenReturn(users);
        when(conversionService.convert(any(User.class), eq(UserOutput.class))).thenReturn(new UserOutput());

        // Act
        List<UserOutput> result = userService.getAll();

        // Assert
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAllByIsActiveTrue();
    }

    @Test
    void getAllUsersWithJuryRights_shouldReturnOnlyJuryAndOrganizers() {
        // Arrange
        List<User> users = new ArrayList<>();

        // Create mock roles
        Role juryRole = new Role();
        juryRole.setName("Jury");

        Role organizerRole = new Role();
        organizerRole.setName("Organizer");

        Role userRole = new Role();
        userRole.setName("User");

        // Create mock users
        User user1 = new User();
        user1.setRole(juryRole);
        user1.setIsActive(true);

        User user2 = new User();
        user2.setRole(organizerRole);
        user2.setIsActive(true);

        User user3 = new User();
        user3.setRole(userRole);
        user3.setIsActive(true);

        users.add(user1);
        users.add(user2);
        users.add(user3);

        // Mock repository
        when(userRepository.findAllByIsActiveTrue()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUsersWithJuryRights();

        // Assert
        assertEquals(2, result.size()); // Only user1 and user2 should be in the result
        assertEquals("Jury", result.get(0).getRole().getName());
        assertEquals("Organizer", result.get(1).getRole().getName());
    }

    // Тест за метода findUserById
    @Test
    void testFindUserById_UserExists_ShouldReturnUser() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = new User();
        when(userRepository.findByUserIdAndIsActiveTrue(userId)).thenReturn(user);
        when(conversionService.convert(user, UserOutput.class)).thenReturn(new UserOutput());

        // Act
        UserOutput result = userService.findUserById(userId);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findByUserIdAndIsActiveTrue(userId);
    }

    @Test
    void testFindUserById_UserDoesNotExist_ShouldThrowException() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(userRepository.findByUserIdAndIsActiveTrue(userId)).thenReturn(null);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.findUserById(userId));
    }

    // Тест за метода createUser
//    @Test
//    void testCreateUser_ShouldCreateUserSuccessfully() {
//        // Arrange
//        UserInput userInput = new UserInput("username", "firstName", "lastName", "email@example.com", "password");
//        when(userRepository.findByUsernameAndIsActiveTrue(userInput.getUsername())).thenReturn(null);
//        when(userRepository.findByEmailAndIsActiveTrue(userInput.getEmail())).thenReturn(null);
//        when(passwordEncoder.encode(userInput.getPassword())).thenReturn("hashedPassword");
//        when(roleService.getRoleByName("User")).thenReturn(null); // Симулиране на връщане на роля
//        when(rankService.getRankByName("Junkie")).thenReturn(null); // Симулиране на връщане на ранг
//        when(conversionService.convert(any(User.class), eq(UserOutputId.class))).thenReturn(new UserOutputId());
//
//        // Act
//        UserOutputId result = userService.createUser(userInput);
//
//        // Assert
//        assertNotNull(result);
//        verify(userRepository, times(1)).save(any(User.class));
//    }

    @Test
    void testCreateUser_DuplicateUsername_ShouldThrowException() {
        // Arrange
        UserInput userInput = new UserInput("username", "firstName", "lastName", "email@example.com", "password");
        when(userRepository.findByUsernameAndIsActiveTrue(userInput.getUsername())).thenReturn(new User());

        // Act & Assert
        assertThrows(DuplicateEntityException.class, () -> userService.createUser(userInput));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUser_DuplicateEmail_ShouldThrowException() {
        // Arrange
        UserInput userInput = new UserInput("username", "firstName", "lastName", "email@example.com", "password");
        when(userRepository.findByEmailAndIsActiveTrue(userInput.getEmail())).thenReturn(new User());

        // Act & Assert
        assertThrows(DuplicateEntityException.class, () -> userService.createUser(userInput));
        verify(userRepository, never()).save(any(User.class));
    }

    // Тест за метода editUser
    @Test
    void testEditUser_ShouldUpdateUserSuccessfully() {
        // Arrange
        User user = new User();
        user.setPassword("oldPassword");
        UserUpdate userUpdate = new UserUpdate("firstName", "lastName", "email@example.com", "newPassword");
        when(passwordEncoder.matches(userUpdate.getPassword(), user.getPassword())).thenReturn(false);
        when(passwordEncoder.encode(userUpdate.getPassword())).thenReturn("hashedNewPassword");
        when(conversionService.convert(user, UserUpdate.class)).thenReturn(userUpdate);

        // Act
        UserUpdate result = userService.editUser(user, userUpdate);

        // Assert
        assertNotNull(result);
        assertEquals("hashedNewPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testEditUser_PasswordMatches_ShouldNotEncodePasswordAgain() {
        // Arrange
        User user = new User();
        user.setPassword("oldPassword");
        UserUpdate userUpdate = new UserUpdate("firstName", "lastName", "email@example.com", "oldPassword");
        when(passwordEncoder.matches(userUpdate.getPassword(), user.getPassword())).thenReturn(true);
        when(conversionService.convert(user, UserUpdate.class)).thenReturn(userUpdate);

        // Act
        UserUpdate result = userService.editUser(user, userUpdate);

        // Assert
        assertNotNull(result);
        assertEquals("oldPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    // Тест за метода deactivateUser
    @Test
    void testDeactivateUser_ShouldDeactivateUser() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setUserId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByUsernameAndIsActiveTrue(user.getUsername())).thenReturn(user);
        when(photoService.getAllPhotosEntityOfUser(user)).thenReturn(new ArrayList<>());

        // Act
        userService.deactivateUser(userId, user);

        // Assert
        assertFalse(user.getIsActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeactivateUser_UserNotFound_ShouldThrowException() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setUserId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.deactivateUser(userId, user));
    }

    @Test
    void testFindUserEntityById_ShouldReturnUser_WhenUserExists() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setUserId(userId);
        when(userRepository.findByUserIdAndIsActiveTrue(userId)).thenReturn(user);

        // Act
        User result = userService.findUserEntityById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(userRepository, times(1)).findByUserIdAndIsActiveTrue(userId);
    }

    @Test
    void testFindUserEntityById_ShouldThrowException_WhenUserDoesNotExist() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(userRepository.findByUserIdAndIsActiveTrue(userId)).thenReturn(null);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.findUserEntityById(userId);
        });

        assertEquals("User with ID " + userId + " not found.", exception.getMessage());
        verify(userRepository, times(1)).findByUserIdAndIsActiveTrue(userId);
    }

    @Test
    void testFindUserByUsername_ShouldReturnUserOutput_WhenUserExists() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        UserOutput userOutput = new UserOutput(); // Може да бъде заменен с реалния UserOutput обект
        when(userRepository.findByUsernameAndIsActiveTrue(username)).thenReturn(user);
        when(conversionService.convert(user, UserOutput.class)).thenReturn(userOutput);

        // Act
        UserOutput result = userService.findUserByUsername(username);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findByUsernameAndIsActiveTrue(username);
        verify(conversionService, times(1)).convert(user, UserOutput.class);
    }

    @Test
    void testFindUserByUsername_ShouldReturnNull_WhenUserDoesNotExist() {
        // Arrange
        String username = "nonexistentuser";
        when(userRepository.findByUsernameAndIsActiveTrue(username)).thenReturn(null);

        // Act
        UserOutput result = userService.findUserByUsername(username);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findByUsernameAndIsActiveTrue(username);
    }

    @Test
    void testFindUserByUsernameAuth_ShouldReturnUser_WhenUserExists() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsernameAndIsActiveTrue(username)).thenReturn(user);

        // Act
        User result = userService.findUserByUsernameAuth(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository, times(1)).findByUsernameAndIsActiveTrue(username);
    }

    @Test
    void testFindUserByUsernameAuth_ShouldReturnNull_WhenUserDoesNotExist() {
        // Arrange
        String username = "nonexistentuser";
        when(userRepository.findByUsernameAndIsActiveTrue(username)).thenReturn(null);

        // Act
        User result = userService.findUserByUsernameAuth(username);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findByUsernameAndIsActiveTrue(username);
    }

    @Test
    void testUploadPhoto_ShouldUploadPhotoAndReturnOutput() throws IOException {
        // Arrange
        String userId = UUID.randomUUID().toString();
        MultipartFile file = mock(MultipartFile.class);
        User user = new User();

        // Трябва да се използва findByUserIdAndIsActiveTrue, тъй като това е методът, използван в findUserEntityById
        when(userRepository.findByUserIdAndIsActiveTrue(UUID.fromString(userId))).thenReturn(user);

        when(cloudinaryService.uploadFile(file)).thenReturn("http://cloudinary.com/photo.png");

        // Act
        UploadFileOutput result = userService.uploadPhoto(userId, file);

        // Assert
        assertNotNull(result);
        assertEquals("http://cloudinary.com/photo.png", user.getProfilePhoto());
        verify(userRepository, times(1)).save(user);
    }


    @Test
    void testUploadPhoto_ShouldThrowExceptionWhenInvalidInput() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.uploadPhoto(null, mock(MultipartFile.class)));
        assertThrows(IllegalArgumentException.class, () -> userService.uploadPhoto(UUID.randomUUID().toString(), null));
    }
}
