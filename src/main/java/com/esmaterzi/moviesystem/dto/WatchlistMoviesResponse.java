package com.esmaterzi.moviesystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistMoviesResponse {
    private Long id;
    private String name;
    private List<MovieInfo> movies;
    private LocalDateTime createdAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MovieInfo {
        private Long id;
        private String title;
        private String overview;
        private Integer releaseYear;
        private String posterUrl;
        private String director;
        private Integer duration;
        private LocalDateTime addedAt;
    }
}

