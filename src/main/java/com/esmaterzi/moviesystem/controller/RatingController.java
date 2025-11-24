package com.esmaterzi.moviesystem.controller;

import com.esmaterzi.moviesystem.dto.MessageResponse;
import com.esmaterzi.moviesystem.dto.RatingRequest;
import com.esmaterzi.moviesystem.models.ratings;
import com.esmaterzi.moviesystem.security.UserDetailsImpl;
import com.esmaterzi.moviesystem.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping
    public ResponseEntity<List<ratings>> getUserRatings(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(ratingService.getUserRatings(userDetails.getId()));
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ratings>> getMovieRatings(@PathVariable Long movieId) {
        return ResponseEntity.ok(ratingService.getMovieRatings(movieId));
    }

    @GetMapping("/movie/{movieId}/user")
    public ResponseEntity<?> getUserMovieRating(
            @PathVariable Long movieId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ratingService.getUserMovieRating(userDetails.getId(), movieId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createOrUpdateRating(
            @Valid @RequestBody RatingRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            ratings rating = ratingService.createOrUpdateRating(
                    userDetails.getId(),
                    request.getMovieId(),
                    request.getRating(),
                    request.getReviewText()
            );
            return ResponseEntity.ok(rating);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            ratings rating = ratingService.findById(id)
                    .filter(r -> r.getUser().getId().equals(userDetails.getId()))
                    .orElseThrow(() -> new RuntimeException("Rating not found or access denied"));

            ratingService.deleteRating(id);
            return ResponseEntity.ok(new MessageResponse("Rating deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}
