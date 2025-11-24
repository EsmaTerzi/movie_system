package com.esmaterzi.moviesystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WatchlistRequest {
    @NotBlank(message = "Watchlist name is required")
    @Size(min = 1, max = 100, message = "Watchlist name must be between 1 and 100 characters")
    private String name;
}
