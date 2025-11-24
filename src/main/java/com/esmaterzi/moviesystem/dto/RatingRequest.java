package com.esmaterzi.moviesystem.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingRequest {
    @NotNull(message = "Movie ID is required")
    private Long movieId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 10, message = "Rating must not exceed 10")
    private Integer rating;

    @Size(max = 2000, message = "Review text must not exceed 2000 characters")
    private String reviewText;
}
