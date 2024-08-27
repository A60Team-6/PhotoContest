package com.telerikacademy.web.photocontest.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "photos")
@Data
@NoArgsConstructor
//@AllArgsConstructor
//@Builder
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

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
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private int total_score;

    @Column(name = "upload_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_active")
    private Boolean isActive;

    @Builder
    public Photo(String title, String story, String photoUrl, Contest contest) {
        this.title = title;
        this.story = story;
        this.photoUrl = photoUrl;
        this.isActive = true;
        this.total_score = 0;

    }
}