package com.esmaterzi.moviesystem.repository;

import com.esmaterzi.moviesystem.models.WatchlistMovieId;
import com.esmaterzi.moviesystem.models.watchlist_movies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchlistMovieRepository extends JpaRepository<watchlist_movies, WatchlistMovieId> {
    void deleteByWatchlistIdAndMovieId(Long watchlistId, Long movieId);
}

