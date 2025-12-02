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

    // Gelişmiş arama query'leri
    @Query("SELECT DISTINCT m FROM movies m " +
           "LEFT JOIN m.movieGenres mg " +
           "LEFT JOIN m.ratings r " +
           "WHERE (:keyword IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "   OR LOWER(m.overview) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "   OR LOWER(m.director) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:year IS NULL OR m.releaseYear = :year) " +
           "AND (:genreId IS NULL OR mg.genre.id = :genreId)")
    List<movies> searchMovies(@Param("keyword") String keyword,
                              @Param("year") Integer year,
                              @Param("genreId") Integer genreId);

    @Query("SELECT DISTINCT m FROM movies m " +
           "LEFT JOIN m.ratings r " +
           "GROUP BY m.id " +
           "HAVING AVG(r.rating) >= :minRating")
    List<movies> findByMinimumRating(@Param("minRating") Double minRating);
}
