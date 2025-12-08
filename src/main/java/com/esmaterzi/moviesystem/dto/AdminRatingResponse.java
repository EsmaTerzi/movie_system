package com.esmaterzi.moviesystem.dto;

public class AdminRatingResponse {
    private Long id;
    private MovieInfo movie;
    private Integer rating;
    private String reviewText;

    public AdminRatingResponse() {
    }

    public AdminRatingResponse(Long id, MovieInfo movie, Integer rating, String reviewText) {
        this.id = id;
        this.movie = movie;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MovieInfo getMovie() {
        return movie;
    }

    public void setMovie(MovieInfo movie) {
        this.movie = movie;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public static class MovieInfo {
        private String title;

        public MovieInfo() {
        }

        public MovieInfo(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}

