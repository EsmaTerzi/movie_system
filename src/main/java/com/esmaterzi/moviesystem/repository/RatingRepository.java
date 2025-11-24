package com.esmaterzi.moviesystem.repository;

import com.esmaterzi.moviesystem.models.ratings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<ratings, Long> {
    List<ratings> findByUserId(Long userId);
    List<ratings> findByMovieId(Long movieId);
    Optional<ratings> findByUserIdAndMovieId(Long userId, Long movieId);

    @Query("SELECT AVG(r.rating) FROM ratings r WHERE r.movie.id = :movieId AND r.rating IS NOT NULL")
    Double getAverageRatingByMovieId(@Param("movieId") Long movieId);

    @Query("SELECT COUNT(r) FROM ratings r WHERE r.movie.id = :movieId AND r.rating IS NOT NULL")
    Long countRatingsByMovieId(@Param("movieId") Long movieId);
}
