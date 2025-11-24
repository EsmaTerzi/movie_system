package com.esmaterzi.moviesystem.service;

import com.esmaterzi.moviesystem.models.movies;
import com.esmaterzi.moviesystem.models.ratings;
import com.esmaterzi.moviesystem.models.users;
import com.esmaterzi.moviesystem.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    public ratings createOrUpdateRating(Long userId, Long movieId, Integer ratingValue, String reviewText) {
        users user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        movies movie = movieService.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        if (ratingValue == null && (reviewText == null || reviewText.trim().isEmpty())) {
            throw new RuntimeException("Either rating or review text must be provided");
        }

        Optional<ratings> existingRating = ratingRepository.findByUserIdAndMovieId(userId, movieId);

        ratings rating;
        if (existingRating.isPresent()) {
            rating = existingRating.get();
            rating.setRating(ratingValue);
            rating.setReviewText(reviewText);
            rating.setUpdatedAt(LocalDateTime.now());
        } else {
            rating = new ratings();
            rating.setUser(user);
            rating.setMovie(movie);
            rating.setRating(ratingValue);
            rating.setReviewText(reviewText);
            rating.setCreatedAt(LocalDateTime.now());
            rating.setUpdatedAt(LocalDateTime.now());
        }

        return ratingRepository.save(rating);
    }

    public List<ratings> getUserRatings(Long userId) {
        return ratingRepository.findByUserId(userId);
    }

    public List<ratings> getMovieRatings(Long movieId) {
        return ratingRepository.findByMovieId(movieId);
    }

    public Optional<ratings> getUserMovieRating(Long userId, Long movieId) {
        return ratingRepository.findByUserIdAndMovieId(userId, movieId);
    }

    public Map<String, Object> getMovieRatingStats(Long movieId) {
        Double avgRating = ratingRepository.getAverageRatingByMovieId(movieId);
        Long count = ratingRepository.countRatingsByMovieId(movieId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("averageRating", avgRating != null ? avgRating : 0.0);
        stats.put("totalRatings", count);

        return stats;
    }

    public void deleteRating(Long id) {
        ratingRepository.deleteById(id);
    }

    public Optional<ratings> findById(Long id) {
        return ratingRepository.findById(id);
    }
}
