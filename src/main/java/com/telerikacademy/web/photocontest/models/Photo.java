package com.telerikacademy.web.photocontest.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "photos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Photo {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "story", nullable = false, columnDefinition = "TEXT")
    private String story;

    @Column(name = "photo_url", nullable = false, length = 255)
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "upload_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_active")
    private Boolean isActive;
}