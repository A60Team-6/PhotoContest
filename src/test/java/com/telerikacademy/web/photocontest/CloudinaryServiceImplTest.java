package com.telerikacademy.web.photocontest;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.telerikacademy.web.photocontest.services.CloudinaryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CloudinaryServiceImplTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private CloudinaryServiceImpl cloudinaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(cloudinary.uploader()).thenReturn(uploader);
    }

    @Test
    void uploadFile_shouldReturnUrl_whenUploadIsSuccessful() throws IOException {
        String expectedUrl = "http://res.cloudinary.com/demo/image/upload/sample.jpg";

        // Mock the upload result map
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("url", expectedUrl);

        // Mock the multipart file and cloudinary upload behavior
        when(multipartFile.getBytes()).thenReturn(new byte[0]);
        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(uploadResult);

        // Call the method under test
        String actualUrl = cloudinaryService.uploadFile(multipartFile);

        // Verify the result
        assertEquals(expectedUrl, actualUrl);
        verify(uploader, times(1)).upload(any(byte[].class), anyMap());
    }

    @Test
    void uploadFile_shouldThrowRuntimeException_whenUploadFails() throws IOException {
        // Mock an IOException when trying to upload
        when(multipartFile.getBytes()).thenReturn(new byte[0]);
        when(uploader.upload(any(byte[].class), anyMap())).thenThrow(new IOException("Upload failed"));

        // Verify that the correct exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cloudinaryService.uploadFile(multipartFile);
        });

        assertEquals("Failed to upload photo", exception.getMessage());
        verify(uploader, times(1)).upload(any(byte[].class), anyMap());
    }
}