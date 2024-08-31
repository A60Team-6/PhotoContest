package com.telerikacademy.web.photocontest.repositories;

import com.telerikacademy.web.photocontest.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  //  User findByUsernameAndIsActive(String username);

    List<User> findAllByIsActiveTrue();

    User findByUserIdAndIsActiveTrue(UUID userId);

    User findByUsernameAndIsActiveTrue(String username);

    User findByEmailAndIsActiveTrue(String email);
}

