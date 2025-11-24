package com.esmaterzi.moviesystem.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class movies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // maps to `id` in SQL

    @Column(name = "external_id", length = 50)
    private String externalId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "overview", columnDefinition = "TEXT")
    private String overview;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "poster_url", length = 500)
    private String posterUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<movie_genres> movieGenres;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ratings> ratings;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<watchlist_movies> watchlistMovies;
}
