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
    void getAllUserEntities_ShouldReturnAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUserEntities();

        assertEquals(users.size(), result.size());
        assertEquals(users, result);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getAll_ShouldReturnAllActiveUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        when(userRepository.findAllByIsActiveTrue()).thenReturn(users);
        when(conversionService.convert(any(User.class), eq(UserOutput.class))).thenReturn(new UserOutput());

        List<UserOutput> result = userService.getAll();

        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAllByIsActiveTrue();
    }

    @Test
    void invite_ShouldSetIsInvitedToTrue() {
        User user = new User();
        user.setIsInvited(false);

        userService.invite(user);

        assertTrue(user.getIsInvited());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void acceptInvitation_ShouldSetRoleToJuryAndDeactivateInvitation() {
        User user = new User();
        user.setIsInvited(true);
        Role juryRole = new Role();
        juryRole.setName("Jury");

        when(roleService.getRoleByName("Jury")).thenReturn(juryRole);

        userService.acceptInvitation(user);

        assertFalse(user.getIsInvited());
        assertEquals("Jury", user.getRole().getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void declineInvitation_ShouldDeactivateInvitation() {
        User user = new User();
        user.setIsInvited(true);

        userService.declineInvitation(user);

        assertFalse(user.getIsInvited());
        verify(userRepository, times(1)).save(user);
    }

//    @Test
//    void getUsersWithFilters_ShouldReturnFilteredUsers() {
//        // Arrange
//        Page<User> mockPage = mock(Page.class);
//        String username = "user";
//        String firstName = "John";
//        String email = "john@example.com";
//        int page = 0;
//        int size = 10;
//        String sortBy = "username";
//        String sortDirection = "asc";
//
//        when(userRepository.findUsersByMultipleFields(anyString(), anyString(), anyString(), any(Pageable.class)))
//                .thenReturn(mockPage);
//
//        // Act
//        Page<User> result = userService.getUsersWithFilters(username, firstName, email, page, size, sortBy, sortDirection);
//
//        // Assert
//        assertNotNull(result);
//        verify(userRepository, times(1)).findUsersByMultipleFields(anyString(), anyString(), anyString(), any(Pageable.class));
//    }

//    @Test
//    void findUserById_ShouldReturnUser_WhenUserExists() {
//        // Arrange
//        UUID userId = UUID.randomUUID();
//        User user = new User();
//        when(userRepository.findByUserIdAndIsActiveTrue(userId)).thenReturn(user);
//        when(conversionService.convert(user, UserOutput.class)).thenReturn(new UserOutput());
//
//        // Act
//        UserOutput result = userService.findUserById(userId);
//
//        // Assert
//        assertNotNull(result);
//        verify(userRepository, times(1)).findByUserIdAndIsActiveTrue(userId);
//    }

//    @Test
//    void findUserById_ShouldThrowException_WhenUserDoesNotExist() {
//        // Arrange
//        UUID userId = UUID.randomUUID();
//        when(userRepository.findByUserIdAndIsActiveTrue(userId)).thenReturn(null);
//
//        // Act & Assert
//        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
//            userService.findUserById(userId);
//        });
//
//        assertEquals("User with ID " + userId + " not found.", exception.getMessage());
//        verify(userRepository, times(1)).findByUserIdAndIsActiveTrue(userId);
//    }

    @Test
    void createUser_ShouldCreateUserSuccessfully() throws IOException {
        Register register = new Register();
        register.setUsername("testuser");
        register.setEmail("test@example.com");
        register.setPassword("password");

        User user = new User();
        when(userRepository.findByUsernameAndIsActiveTrue(anyString())).thenReturn(null);
        when(userRepository.findByEmailAndIsActiveTrue(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        UserOutputId userOutputId = new UserOutputId();
        when(conversionService.convert(any(User.class), eq(UserOutputId.class))).thenReturn(userOutputId);

        UserOutputId result = userService.createUser(register);

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
        verify(conversionService, times(1)).convert(any(User.class), eq(UserOutputId.class));
    }

    @Test
    void createUser_ShouldThrowDuplicateEntityException_WhenUsernameExists() {
        Register register = new Register();
        register.setUsername("testuser");
        when(userRepository.findByUsernameAndIsActiveTrue(anyString())).thenReturn(new User());

        assertThrows(DuplicateEntityException.class, () -> userService.createUser(register));
    }

    @Test
    void createUser_ShouldThrowDuplicateEntityException_WhenEmailExists() {
        Register register = new Register();
        register.setEmail("test@example.com");
        when(userRepository.findByEmailAndIsActiveTrue(anyString())).thenReturn(new User());

        assertThrows(DuplicateEntityException.class, () -> userService.createUser(register));
    }

    @Test
    void deactivateUser_ShouldSetIsActiveToFalse() {
        UUID userId = UUID.randomUUID();
        User authenticatedUser = new User();
        authenticatedUser.setUsername("testuser"); // Симулиране на текущия потребител
        authenticatedUser.setIsActive(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(authenticatedUser));
        when(userRepository.findByUsernameAndIsActiveTrue(authenticatedUser.getUsername())).thenReturn(authenticatedUser);

        userService.deactivateUser(userId, authenticatedUser);

        assertFalse(authenticatedUser.getIsActive());
        verify(userRepository, times(1)).save(authenticatedUser);
    }

//    @Test
//    void uploadPhoto_ShouldUploadPhotoSuccessfully() throws IOException {
//        // Arrange
//        UUID userId = UUID.randomUUID(); // използваме UUID тук вместо String
//        MultipartFile file = mock(MultipartFile.class);
//
//        // Симулиране на съществуващ потребител
//        User user = new User();
//        user.setUsername("testuser");
//
//        // Mock метода на userRepository, за да върне съществуващ потребител
//        when(userRepository.findByUserIdAndIsActiveTrue(userId)).thenReturn(user);
//
//        // Mock метода на Cloudinary за успешен ъплоуд на файл
//        when(cloudinaryService.uploadFile(file)).thenReturn("http://cloudinary.com/photo.png");
//
//        // Act
//        UploadFileOutput result = userService.uploadPhoto(userId.toString(), file);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("http://cloudinary.com/photo.png", user.getProfilePhoto());
//        verify(userRepository, times(1)).save(user);
//    }



    @Test
    void uploadPhoto_ShouldThrowIllegalArgumentException_WhenInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> userService.uploadPhoto(null, mock(MultipartFile.class)));
        assertThrows(IllegalArgumentException.class, () -> userService.uploadPhoto(UUID.randomUUID().toString(), null));
    }

//    @Test
//    void getUsersWithFilters_ShouldReturnFilteredUsers() {
//        // Arrange
//        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "username"));
//        List<User> users = new ArrayList<>();
//        users.add(new User());
//        Page<User> userPage = new PageImpl<>(users);
//
//        when(userRepository.findUsersByMultipleFields(anyString(), anyString(), anyString(), eq(pageable)))
//                .thenReturn(userPage);
//
//        // Act
//        Page<User> result = userService.getUsersWithFilters("test", "first", "email", 0, 10, "username", "asc");
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(1, result.getContent().size());
//        verify(userRepository, times(1)).findUsersByMultipleFields(anyString(), anyString(), anyString(), eq(pageable));
//    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        boolean result = userService.existsByEmail("test@example.com");

        assertTrue(result);
        verify(userRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    void existsByUsername_ShouldReturnTrue_WhenUsernameExists() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        boolean result = userService.existsByUsername("testuser");

        assertTrue(result);
        verify(userRepository, times(1)).existsByUsername(anyString());
    }

//    @Test
//    void getAllUsersWithJuryRights_ShouldReturnOnlyJuryAndOrganizers() {
//        // Arrange
//        List<User> users = new ArrayList<>();
//        User juryUser = new User();
//        juryUser.setRole(new Role("Jury"));
//        juryUser.setIsActive(true);
//
//        User organizerUser = new User();
//        organizerUser.setRole(new Role("Organizer"));
//        organizerUser.setIsActive(true);
//
//        User regularUser = new User();
//        regularUser.setRole(new Role("User"));
//        regularUser.setIsActive(true);
//
//        users.add(juryUser);
//        users.add(organizerUser);
//        users.add(regularUser);
//
//        when(userRepository.findAllByIsActiveTrue()).thenReturn(users);
//
//        // Act
//        List<User> result = userService.getAllUsersWithJuryRights();
//
//        // Assert
//        assertEquals(2, result.size());
//        assertEquals("Jury", result.get(0).getRole().getName());
//        assertEquals("Organizer", result.get(1).getRole().getName());
//    }

    @Test
    void findUserById_ShouldReturnUser_WhenUserExists() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        when(userRepository.findByUserIdAndIsActiveTrue(userId)).thenReturn(user);
        when(conversionService.convert(any(User.class), eq(UserOutput.class))).thenReturn(new UserOutput());

        UserOutput result = userService.findUserById(userId, user);

        assertNotNull(result);
        verify(userRepository, times(1)).findByUserIdAndIsActiveTrue(userId);
    }

    @Test
    void findUserById_ShouldThrowException_WhenUserNotFound() {
        UUID userId = UUID.randomUUID();

        User authenticatedUser = new User();
        authenticatedUser.setRole(new Role());
        authenticatedUser.getRole().setName("Organizer");

        when(userRepository.findByUserIdAndIsActiveTrue(userId)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> userService.findUserById(userId, authenticatedUser));
    }

    @Test
    void findUserEntityById_ShouldReturnUser_WhenUserExists() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        when(userRepository.findByUserIdAndIsActiveTrue(userId)).thenReturn(user);

        User result = userService.findUserEntityById(userId);

        assertNotNull(result);
        verify(userRepository, times(1)).findByUserIdAndIsActiveTrue(userId);
    }

    @Test
    void findUserEntityById_ShouldThrowException_WhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findByUserIdAndIsActiveTrue(userId)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> userService.findUserEntityById(userId));
    }

    @Test
    void findUserByUsername_ShouldReturnUserOutput_WhenUserExists() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsernameAndIsActiveTrue(username)).thenReturn(user);
        when(conversionService.convert(any(User.class), eq(UserOutput.class))).thenReturn(new UserOutput());

        UserOutput result = userService.findUserByUsername(username);

        assertNotNull(result);
        verify(userRepository, times(1)).findByUsernameAndIsActiveTrue(username);
    }

    @Test
    void findUserEntityByUsername_ShouldReturnUser_WhenUserExists() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsernameAndIsActiveTrue(username)).thenReturn(user);

        User result = userService.findUserEntityByUsername(username);

        assertNotNull(result);
        verify(userRepository, times(1)).findByUsernameAndIsActiveTrue(username);
    }

    @Test
    void findUserEntityByUsername_ShouldReturnNull_WhenUserNotFound() {
        String username = "testuser";
        when(userRepository.findByUsernameAndIsActiveTrue(username)).thenReturn(null);

        User result = userService.findUserEntityByUsername(username);

        assertNull(result);
        verify(userRepository, times(1)).findByUsernameAndIsActiveTrue(username);
    }
}
