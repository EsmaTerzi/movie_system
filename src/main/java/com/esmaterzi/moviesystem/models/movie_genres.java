package com.esmaterzi.moviesystem.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movie_genres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class movie_genres {

    @EmbeddedId
    private MovieGenreId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movieId")
    @JoinColumn(name = "movie_id", nullable = false)
    private movies movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("genreId")
    @JoinColumn(name = "genre_id", nullable = false)
    private genres genre;
}
