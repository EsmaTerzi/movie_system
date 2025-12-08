package com.esmaterzi.moviesystem.service;

import com.esmaterzi.moviesystem.dto.AdminRatingResponse;
import com.esmaterzi.moviesystem.dto.RatingResponse;
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
import java.util.stream.Collectors;

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

    private RatingResponse convertToRatingResponse(ratings rating) {
        RatingResponse response = new RatingResponse();
        response.setId(rating.getId());
        response.setUsername(rating.getUser().getUsername());
        response.setRating(rating.getRating());
        response.setReviewText(rating.getReviewText());
        response.setCreatedAt(rating.getCreatedAt());
        response.setUpdatedAt(rating.getUpdatedAt());
        return response;
    }

    public List<RatingResponse> getUserRatingsResponse(Long userId) {
        return ratingRepository.findByUserId(userId).stream()
                .map(this::convertToRatingResponse)
                .collect(Collectors.toList());
    }

    public List<RatingResponse> getMovieRatingsResponse(Long movieId) {
        return ratingRepository.findByMovieId(movieId).stream()
                .map(this::convertToRatingResponse)
                .collect(Collectors.toList());
    }

    public RatingResponse createOrUpdateRatingResponse(Long userId, Long movieId, Integer ratingValue, String reviewText) {
        ratings rating = createOrUpdateRating(userId, movieId, ratingValue, reviewText);
        return convertToRatingResponse(rating);
    }

    public List<AdminRatingResponse> getAllRatingsForAdmin() {
        return ratingRepository.findAll().stream()
                .map(rating -> {
                    AdminRatingResponse.MovieInfo movieInfo = new AdminRatingResponse.MovieInfo(
                            rating.getMovie().getTitle()
                    );
                    return new AdminRatingResponse(
                            rating.getId(),
                            movieInfo,
                            rating.getRating(),
                            rating.getReviewText()
                    );
                })
                .collect(Collectors.toList());
    }
}
