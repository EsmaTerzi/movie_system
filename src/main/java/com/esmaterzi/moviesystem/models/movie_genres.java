package com.esmaterzi.moviesystem.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movie_genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class movie_genres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_genre_id")
    private Long movieGenreId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private movies movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    private genres genre;
}
