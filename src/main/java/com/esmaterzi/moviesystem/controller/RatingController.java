package com.esmaterzi.moviesystem.controller;

import com.esmaterzi.moviesystem.dto.AdminRatingResponse;
import com.esmaterzi.moviesystem.dto.MessageResponse;
import com.esmaterzi.moviesystem.dto.RatingRequest;
import com.esmaterzi.moviesystem.dto.RatingResponse;
import com.esmaterzi.moviesystem.models.ratings;
import com.esmaterzi.moviesystem.security.UserDetailsImpl;
import com.esmaterzi.moviesystem.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminRatingResponse>> getAllRatings() {
        return ResponseEntity.ok(ratingService.getAllRatingsForAdmin());
    }

    @GetMapping("/public/movie/{movieId}")
    public ResponseEntity<List<RatingResponse>> getMovieRatings(@PathVariable Long movieId) {
        return ResponseEntity.ok(ratingService.getMovieRatingsResponse(movieId));
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
            RatingResponse response = ratingService.createOrUpdateRatingResponse(
                    userDetails.getId(),
                    request.getMovieId(),
                    request.getRating(),
                    request.getReviewText()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRating(
            @PathVariable Long id,
            @Valid @RequestBody RatingRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            ratings existingRating = ratingService.findById(id)
                    .filter(r -> r.getUser().getId().equals(userDetails.getId()))
                    .orElseThrow(() -> new RuntimeException("Rating not found or access denied"));

            existingRating.setRating(request.getRating());
            existingRating.setReviewText(request.getReviewText());

            ratings updatedRating = ratingService.createOrUpdateRating(
                    userDetails.getId(),
                    existingRating.getMovie().getId(),
                    request.getRating(),
                    request.getReviewText()
            );

            return ResponseEntity.ok(updatedRating);
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
