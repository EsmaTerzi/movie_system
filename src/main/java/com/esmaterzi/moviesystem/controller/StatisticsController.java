package com.esmaterzi.moviesystem.controller;

import com.esmaterzi.moviesystem.dto.StatisticsResponse;
import com.esmaterzi.moviesystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/overview")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatisticsResponse> getStatistics() {
        Long totalMovies = movieRepository.count();
        Long totalGenres = genreRepository.count();
        Long totalReviews = ratingRepository.count();
        Long totalWatchlists = watchlistRepository.count();
        Long totalUsers = userRepository.count();

        StatisticsResponse response = new StatisticsResponse(
                totalMovies,
                totalGenres,
                totalReviews,
                totalWatchlists,
                totalUsers
        );

        return ResponseEntity.ok(response);
    }
}

