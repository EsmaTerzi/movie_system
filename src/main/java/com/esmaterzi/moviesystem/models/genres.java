package com.esmaterzi.moviesystem.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "genres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class genres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // maps to `id` in SQL (INT UNSIGNED)

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<movie_genres> movieGenres;
}
