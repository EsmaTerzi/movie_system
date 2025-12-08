package com.esmaterzi.moviesystem.dto;

public class StatisticsResponse {
    private Long totalMovies;
    private Long totalGenres;
    private Long totalReviews;
    private Long totalWatchlists;
    private Long totalUsers;

    public StatisticsResponse() {
    }

    public StatisticsResponse(Long totalMovies, Long totalGenres, Long totalReviews, Long totalWatchlists, Long totalUsers) {
        this.totalMovies = totalMovies;
        this.totalGenres = totalGenres;
        this.totalReviews = totalReviews;
        this.totalWatchlists = totalWatchlists;
        this.totalUsers = totalUsers;
    }

    public Long getTotalMovies() {
        return totalMovies;
    }

    public void setTotalMovies(Long totalMovies) {
        this.totalMovies = totalMovies;
    }

    public Long getTotalGenres() {
        return totalGenres;
    }

    public void setTotalGenres(Long totalGenres) {
        this.totalGenres = totalGenres;
    }

    public Long getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(Long totalReviews) {
        this.totalReviews = totalReviews;
    }

    public Long getTotalWatchlists() {
        return totalWatchlists;
    }

    public void setTotalWatchlists(Long totalWatchlists) {
        this.totalWatchlists = totalWatchlists;
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }
}

