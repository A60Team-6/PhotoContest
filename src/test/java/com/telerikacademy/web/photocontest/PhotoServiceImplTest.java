package com.telerikacademy.web.photocontest;

import com.telerikacademy.web.photocontest.entities.*;
import com.telerikacademy.web.photocontest.entities.dtos.*;
import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.repositories.ContestRepository;
import com.telerikacademy.web.photocontest.repositories.PhotoRepository;
import com.telerikacademy.web.photocontest.services.PhotoServiceImpl;
import com.telerikacademy.web.photocontest.services.contracts.CloudinaryService;
import com.telerikacademy.web.photocontest.services.contracts.ContestParticipationService;
import com.telerikacademy.web.photocontest.services.contracts.ContestService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PhotoServiceImplTest {

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private ConversionService conversionService;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private ContestRepository contestRepository;

    @Mock
    private ContestParticipationService contestParticipationService;

    @InjectMocks
    private PhotoServiceImpl photoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPhotosOfUser_ShouldReturnAllPhotosOfUser() {
        // Arrange
        User user = new User();
        List<Photo> photos = List.of(new Photo(), new Photo());
        photos.forEach(photo -> photo.setUser(user));

        when(photoRepository.findAllByIsActiveTrue()).thenReturn(photos);
        when(conversionService.convert(any(Photo.class), eq(PhotoOutput.class))).thenReturn(new PhotoOutput());

        // Act
        List<PhotoOutput> result = photoService.getAllPhotosOfUser(user);

        // Assert
        assertEquals(photos.size(), result.size());
        verify(photoRepository, times(1)).findAllByIsActiveTrue();
    }

    @Test
    void testGetAllPhotosEntityOfUser_ShouldReturnAllPhotosEntityOfUser() {
        // Arrange
        User user = new User();
        List<Photo> photos = List.of(new Photo(), new Photo());
        photos.forEach(photo -> photo.setUser(user));

        when(photoRepository.findAllByIsActiveTrue()).thenReturn(photos);

        // Act
        List<Photo> result = photoService.getAllPhotosEntityOfUser(user);

        // Assert
        assertEquals(photos.size(), result.size());
        verify(photoRepository, times(1)).findAllByIsActiveTrue();
    }

    @Test
    void testGetAllPhotosOfContest_ShouldReturnAllPhotosOfContest() {
        // Arrange
        Contest contest = new Contest();
        List<Photo> photos = List.of(new Photo(), new Photo());
        photos.forEach(photo -> photo.setContest(contest));

        when(photoRepository.findAllByIsActiveTrue()).thenReturn(photos);
        when(conversionService.convert(any(Photo.class), eq(PhotoOutput.class))).thenReturn(new PhotoOutput());

        // Act
        List<PhotoOutput> result = photoService.getAllPhotosOfContest(contest);

        // Assert
        assertEquals(photos.size(), result.size());
        verify(photoRepository, times(1)).findAllByIsActiveTrue();
    }

    @Test
    void testSoftDeletePhoto_ShouldMarkPhotoAsInactive() {
        // Arrange
        UUID photoId = UUID.randomUUID();
        Photo photo = new Photo();
        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));

        // Act
        photoService.softDeletePhoto(photoId);

        // Assert
        assertFalse(photo.getIsActive());
        verify(photoRepository, times(1)).save(photo);
    }

    @Test
    void testSoftDeletePhoto_ShouldThrowEntityNotFoundException_WhenPhotoNotFound() {
        // Arrange
        UUID photoId = UUID.randomUUID();
        when(photoRepository.findById(photoId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> photoService.softDeletePhoto(photoId));
        verify(photoRepository, never()).save(any(Photo.class));
    }



    @Test
    void testGetPhotoById_ShouldReturnPhotoOutput() {
        UUID photoId = UUID.randomUUID();
        Photo photo = new Photo();
        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));
        when(conversionService.convert(photo, PhotoOutput.class)).thenReturn(new PhotoOutput());

        PhotoOutput result = photoService.getPhotoById(photoId);

        assertNotNull(result);
        verify(photoRepository, times(1)).findById(photoId);
    }

    @Test
    void testGetPhotoById_ShouldThrowEntityNotFoundException() {
        UUID photoId = UUID.randomUUID();
        when(photoRepository.findById(photoId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> photoService.getPhotoById(photoId));
        verify(photoRepository, times(1)).findById(photoId);
    }

    @Test
    void testGetAll_ShouldReturnAllActivePhotos() {
        List<Photo> photos = List.of(new Photo(), new Photo());
        when(photoRepository.findAllByIsActiveTrue()).thenReturn(photos);
        when(conversionService.convert(any(Photo.class), eq(PhotoOutput.class)))
                .thenAnswer(invocation -> new PhotoOutput());

        List<PhotoOutput> result = photoService.getAll();

        assertEquals(photos.size(), result.size());
        verify(photoRepository, times(1)).findAllByIsActiveTrue();
    }

    @Test
    void testGetByTitle_ShouldReturnPhotoWhenTitleExists() {
        String title = "Title";
        Photo photo = new Photo();
        when(photoRepository.findByTitleAndIsActiveTrue(title)).thenReturn(photo);
        when(conversionService.convert(photo, PhotoOutput.class)).thenReturn(new PhotoOutput());

        PhotoOutput result = photoService.getByTitle(title);

        assertNotNull(result);
        verify(photoRepository, times(1)).findByTitleAndIsActiveTrue(title);
    }

    @Test
    void testGetByTitle_ShouldThrowExceptionWhenTitleNotExists() {
        String title = "NonExistentTitle";
        when(photoRepository.findByTitleAndIsActiveTrue(title)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> photoService.getByTitle(title));
        verify(photoRepository, times(1)).findByTitleAndIsActiveTrue(title);
    }

    @Test
    void testSearchByTitle_ShouldReturnPhotosContainingTitle() {
        String title = "Title";
        List<Photo> photos = List.of(new Photo(), new Photo());
        when(photoRepository.findByTitleContainingIgnoreCase(title)).thenReturn(photos);
        when(conversionService.convert(any(Photo.class), eq(PhotoOutput.class)))
                .thenAnswer(invocation -> new PhotoOutput());

        List<PhotoOutput> result = photoService.searchByTitle(title);

        assertEquals(photos.size(), result.size());
        verify(photoRepository, times(1)).findByTitleContainingIgnoreCase(title);
    }

    @Test
    void testUploadPhoto_ShouldUploadPhotoAndReturnOutput() throws IOException {
        UUID photoId = UUID.randomUUID();
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});

        UploadFileInput uploadFileInput = new UploadFileInput(photoId.toString(), file);
        Photo photo = new Photo();
        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));
        when(cloudinaryService.uploadFile(file)).thenReturn("http://cloudinary.com/photo.png");

        UploadFileOutput result = photoService.uploadPhoto(uploadFileInput);

        assertNotNull(result);
        assertEquals("http://cloudinary.com/photo.png", photo.getPhotoUrl());
        verify(photoRepository, times(1)).save(any(Photo.class));
    }

    @Test
    void testUploadPhoto_ShouldUploadSuccessfully() throws IOException {
        // Arrange
        UUID photoId = UUID.randomUUID();
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});

        UploadFileInput uploadFileInput = new UploadFileInput(photoId.toString(), file);
        Photo photo = new Photo();
        Contest contest = new Contest();
        photo.setContest(contest);

        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));
        when(cloudinaryService.uploadFile(file)).thenReturn("http://cloudinary.com/photo.png");
        when(photoRepository.save(any(Photo.class))).thenReturn(photo);

        // Act
        UploadFileOutput result = photoService.uploadPhoto(uploadFileInput);

        // Assert
        assertNotNull(result);
        assertEquals("http://cloudinary.com/photo.png", photo.getPhotoUrl());
        verify(photoRepository, times(1)).save(photo);
    }

    @Test
    void testUploadPhoto_ShouldThrowIllegalArgumentException_WhenInvalidInput() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> photoService.uploadPhoto(null));
        assertThrows(IllegalArgumentException.class, () -> photoService.uploadPhoto(new UploadFileInput(UUID.randomUUID().toString(), null)));
    }

    @Test
    void testUpdatePhoto_ShouldUpdatePhotoDetails() {
        UUID photoId = UUID.randomUUID();
        PhotoUpdate photoUpdate = new PhotoUpdate("NewTitle", "NewStory");
        User user = new User();
        Photo photo = new Photo();
        photo.setUser(user);
        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));
        when(photoRepository.existsByTitleAndIsActiveTrue(anyString())).thenReturn(false);

        photoService.updatePhoto(photoId, photoUpdate, user);

        assertEquals(photoUpdate.getTitle(), photo.getTitle());
        assertEquals(photoUpdate.getStory(), photo.getStory());
        verify(photoRepository, times(1)).save(photo);
    }

    @Test
    void testUpdatePhoto_ShouldThrowExceptionWhenTitleExists() {
        UUID photoId = UUID.randomUUID();
        PhotoUpdate photoUpdate = new PhotoUpdate("ExistingTitle", "NewStory");
        User user = new User();
        Photo photo = new Photo();
        photo.setUser(user);
        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));
        when(photoRepository.existsByTitleAndIsActiveTrue(photoUpdate.getTitle())).thenReturn(true);

        assertThrows(DuplicateEntityException.class, () -> photoService.updatePhoto(photoId, photoUpdate, user));
        verify(photoRepository, never()).save(photo);
    }

    @Test
    void testCreatePhoto_ShouldThrowDuplicateEntityException_WhenPhotoAlreadyExists() {
        // Arrange
        PhotoInput photoInput = new PhotoInput("Title", "Story", UUID.randomUUID().toString());
        User user = new User();
        user.setIsActive(true);
        Contest contest = new Contest();
        Phase phase = new Phase();
        phase.setName("Phase 1");
        contest.setPhase(phase);

        Photo existingPhoto = new Photo();
        existingPhoto.setTitle("Title");
        existingPhoto.setContest(contest);

        when(contestRepository.findByContestIdAndIsActiveTrue(any(UUID.class))).thenReturn(contest);
        when(photoService.getAllPhotosEntityOfContest(contest)).thenReturn(List.of(existingPhoto));

        // Act & Assert
        assertThrows(DuplicateEntityException.class, () -> photoService.createPhoto(photoInput, user));
        verify(photoRepository, never()).save(any(Photo.class));
    }

}
