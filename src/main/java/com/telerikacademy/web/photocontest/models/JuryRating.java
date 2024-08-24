package com.telerikacademy.web.photocontest.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "jury_photo_ratings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JuryRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "photo_id", nullable = false)
    private Photo photo;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User jury;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "comment", nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(name = "category_match")
    private Boolean categoryMatch;

    @Column(name = "review_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_active")
    private Boolean isActive;
}
