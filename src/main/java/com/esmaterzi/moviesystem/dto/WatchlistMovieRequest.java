package com.esmaterzi.moviesystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WatchlistMovieRequest {
    @NotNull(message = "Movie ID is required")
    private Long movieId;
}
