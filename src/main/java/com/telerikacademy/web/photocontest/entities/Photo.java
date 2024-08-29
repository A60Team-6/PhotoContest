package com.telerikacademy.web.photocontest.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "photos")
@Data
@NoArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "story", nullable = false)
    private String story;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(nullable = false)
    private String hash;  // Поле за хеша

    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private int total_score;

    @Column(name = "upload_date")
    private LocalDateTime createdAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @Builder
    public Photo(String title, String story, Contest contest) {
        this.title = title;
        this.story = story;
        this.contest = contest;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
        this.total_score = 0;
    }
}