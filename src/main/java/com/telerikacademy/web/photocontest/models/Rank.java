package com.telerikacademy.web.photocontest.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "rankings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rank {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String userId = UUID.randomUUID().toString();

    @Column(name = "name", nullable = false, length = 36)
    private String firstName;
}
