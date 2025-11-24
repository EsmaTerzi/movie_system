package com.esmaterzi.moviesystem.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "watchlist_movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class watchlist_movies {

    @EmbeddedId
    private WatchlistMovieId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("watchlistId")
    @JoinColumn(name = "watchlist_id", nullable = false)
    private watchlists watchlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movieId")
    @JoinColumn(name = "movie_id", nullable = false)
    private movies movie;

    @CreationTimestamp
    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;
}
