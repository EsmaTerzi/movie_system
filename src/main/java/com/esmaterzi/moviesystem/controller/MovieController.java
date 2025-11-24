package com.esmaterzi.moviesystem.controller;

import com.esmaterzi.moviesystem.dto.MessageResponse;
import com.esmaterzi.moviesystem.models.movies;
import com.esmaterzi.moviesystem.service.MovieService;
import com.esmaterzi.moviesystem.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private RatingService ratingService;

    @GetMapping
    public ResponseEntity<List<movies>> getAllMovies() {
        return ResponseEntity.ok(movieService.findAllMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable Long id) {
        return movieService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<movies>> searchMovies(@RequestParam String title) {
        return ResponseEntity.ok(movieService.searchByTitle(title));
    }

    @GetMapping("/external/{externalId}")
    public ResponseEntity<?> getMovieByExternalId(@PathVariable String externalId) {
        return movieService.findByExternalId(externalId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/genre/{genreId}")
    public ResponseEntity<List<movies>> getMoviesByGenre(@PathVariable Integer genreId) {
        return ResponseEntity.ok(movieService.findByGenreId(genreId));
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<List<movies>> getMoviesByYear(@PathVariable Integer year) {
        return ResponseEntity.ok(movieService.findByReleaseYear(year));
    }

    @GetMapping("/{id}/ratings")
    public ResponseEntity<Map<String, Object>> getMovieRatingStats(@PathVariable Long id) {
        return ResponseEntity.ok(ratingService.getMovieRatingStats(id));
    }

    @PostMapping
    public ResponseEntity<?> createMovie(@RequestBody movies movie) {
        try {
            movies savedMovie = movieService.saveMovie(movie);
            return ResponseEntity.ok(savedMovie);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable Long id, @RequestBody movies movieDetails) {
        return movieService.findById(id)
                .map(movie -> {
                    movie.setTitle(movieDetails.getTitle());
                    movie.setOverview(movieDetails.getOverview());
                    movie.setReleaseYear(movieDetails.getReleaseYear());
                    movie.setPosterUrl(movieDetails.getPosterUrl());
                    movie.setExternalId(movieDetails.getExternalId());
                    return ResponseEntity.ok(movieService.saveMovie(movie));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        try {
            movieService.deleteMovie(id);
            return ResponseEntity.ok(new MessageResponse("Movie deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}

