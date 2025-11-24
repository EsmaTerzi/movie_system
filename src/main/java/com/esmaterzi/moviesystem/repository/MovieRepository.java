package com.esmaterzi.moviesystem.repository;

import com.esmaterzi.moviesystem.models.movies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<movies, Long> {
    Optional<movies> findByExternalId(String externalId);
    List<movies> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT m FROM movies m JOIN m.movieGenres mg WHERE mg.genre.id = :genreId")
    List<movies> findByGenreId(@Param("genreId") Integer genreId);

    List<movies> findByReleaseYear(Integer year);
}

