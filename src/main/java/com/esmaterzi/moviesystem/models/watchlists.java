package com.esmaterzi.moviesystem.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "watchlists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class watchlists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // maps to `id` in SQL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private users user;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "watchlist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<watchlist_movies> watchlistMovies;
}
