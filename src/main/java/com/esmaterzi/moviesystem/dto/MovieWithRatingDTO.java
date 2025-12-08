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
public class MovieWithRatingDTO {
    private Long id;
    private String externalId;
    private String title;
    private String overview;
    private Integer releaseYear;
    private String posterUrl;
    private String director;
    private Integer duration;
    private LocalDateTime createdAt;
    private Double averageRating;
    private Long totalRatings;
    private List<String> genresNames;
}
