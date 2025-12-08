package com.esmaterzi.moviesystem.repository;

import com.esmaterzi.moviesystem.models.MovieGenreId;
import com.esmaterzi.moviesystem.models.movie_genres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieGenreRepository extends JpaRepository<movie_genres, MovieGenreId> {
    List<movie_genres> findByMovieId(Long movieId);

    @Modifying
    void deleteByMovieId(Long movieId);
}
