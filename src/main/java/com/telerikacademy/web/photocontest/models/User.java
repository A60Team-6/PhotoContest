package com.telerikacademy.web.photocontest.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data // Генерира автоматично getters, setters, equals, hashCode, toString
@NoArgsConstructor // Генерира празен конструктор
@AllArgsConstructor // Генерира конструктор с всички полета
@Builder // Генерира Builder pattern за създаване на обекти
public class User {

    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId = UUID.randomUUID().toString();

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "profile_photo", nullable = false, length = 256)
    private String profilePhoto;

    @Column(name = "points")
    private Integer points;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "ranking_id", nullable = false)
    private Rank rank;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_active")
    private Boolean isActive;
}
