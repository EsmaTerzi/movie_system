package com.esmaterzi.moviesystem.controller;

import com.esmaterzi.moviesystem.dto.MessageResponse;
import com.esmaterzi.moviesystem.dto.MovieRequest;
import com.esmaterzi.moviesystem.dto.MovieWithRatingDTO;
import com.esmaterzi.moviesystem.models.movies;
import com.esmaterzi.moviesystem.service.MovieService;
import com.esmaterzi.moviesystem.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/public")
    public ResponseEntity<List<MovieWithRatingDTO>> getAllMovies() {
        return ResponseEntity.ok(movieService.findAllMoviesWithRatings());
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<MovieWithRatingDTO> getMovieById(@PathVariable Long id) {
        return movieService.findByIdWithRating(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<movies>> searchMovies(@RequestParam String title) {
        return ResponseEntity.ok(movieService.searchByTitle(title));
    }

    @GetMapping("public/search/advanced")
    public ResponseEntity<List<MovieWithRatingDTO>> searchMoviesAdvanced(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer genreId,
            @RequestParam(required = false) Double minRating) {
        return ResponseEntity.ok(movieService.searchMoviesAdvanced(keyword, year, genreId, minRating));
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createMovie(@RequestBody MovieRequest request) {
        try {
            movies savedMovie = movieService.createMovieWithGenres(request);
            return ResponseEntity.ok(savedMovie);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

   @PreAuthorize("hasRole('ADMIN')")
   @PutMapping("/{id}")
   public ResponseEntity<?> updateMovie(@PathVariable Long id, @RequestBody MovieRequest request) {
      try {
           movies updatedMovie = movieService.updateMovieWithGenres(id, request);
           return ResponseEntity.ok(updatedMovie);
      } catch (Exception e) {
          return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
   }

    @PreAuthorize("hasRole('ADMIN')")
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
