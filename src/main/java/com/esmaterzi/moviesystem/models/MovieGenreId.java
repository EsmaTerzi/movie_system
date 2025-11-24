package com.esmaterzi.moviesystem.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MovieGenreId implements Serializable {
    private Long movieId;
    private Integer genreId;

    public MovieGenreId() {}

    public MovieGenreId(Long movieId, Integer genreId) {
        this.movieId = movieId;
        this.genreId = genreId;
    }

    // getters/setters
    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }
    public Integer getGenreId() { return genreId; }
    public void setGenreId(Integer genreId) { this.genreId = genreId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieGenreId that = (MovieGenreId) o;
        return Objects.equals(movieId, that.movieId) && Objects.equals(genreId, that.genreId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, genreId);
    }
}

