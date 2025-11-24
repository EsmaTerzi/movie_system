package com.esmaterzi.moviesystem.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class WatchlistMovieId implements Serializable {
    private Long watchlistId;
    private Long movieId;

    public WatchlistMovieId() {}

    public WatchlistMovieId(Long watchlistId, Long movieId) {
        this.watchlistId = watchlistId;
        this.movieId = movieId;
    }

    public Long getWatchlistId() { return watchlistId; }
    public void setWatchlistId(Long watchlistId) { this.watchlistId = watchlistId; }
    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WatchlistMovieId that = (WatchlistMovieId) o;
        return Objects.equals(watchlistId, that.watchlistId) && Objects.equals(movieId, that.movieId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(watchlistId, movieId);
    }
}

