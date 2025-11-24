package com.esmaterzi.moviesystem.repository;

import com.esmaterzi.moviesystem.models.genres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<genres, Integer> {
    Optional<genres> findByName(String name);
    Boolean existsByName(String name);
}

