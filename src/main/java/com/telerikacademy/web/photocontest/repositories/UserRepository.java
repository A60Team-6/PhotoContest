package com.telerikacademy.web.photocontest.repositories;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> findAllByIsActiveTrue();

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User findByUserIdAndIsActiveTrue(UUID userId);

    User findByUsernameAndIsActiveTrue(String username);

    User findByEmailAndIsActiveTrue(String email);

    @Query("SELECT u FROM User u " +
            "WHERE u.isActive = true " +
            "AND (:username IS NULL OR u.username like :username) " +
            "AND (:firstName IS NULL OR u.firstName like :firstName) " +
            "AND (:email IS NULL OR u.email like :email) ")
    Page<User> findUsersByMultipleFields(
            @Param("username") String username,
            @Param("firstName") String firstName,
            @Param("email") String email,
            Pageable pageable);
}

