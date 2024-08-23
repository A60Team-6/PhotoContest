package com.telerikacademy.web.photocontest.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "phases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Phase {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String phaseId = UUID.randomUUID().toString();

    @Column(name = "name", nullable = false)
    private String name;
}
