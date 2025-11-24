package com.esmaterzi.moviesystem.repository;

import com.esmaterzi.moviesystem.models.users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<users, Long> {
    Optional<users> findByUsername(String username);
    Optional<users> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}

