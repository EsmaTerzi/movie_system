package com.esmaterzi.moviesystem.service;

import com.esmaterzi.moviesystem.dto.MovieWithRatingDTO;
import com.esmaterzi.moviesystem.models.movies;
import com.esmaterzi.moviesystem.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Lazy
    @Autowired
    private RatingService ratingService;

    public movies saveMovie(movies movie) {
        if (movie.getCreatedAt() == null) {
            movie.setCreatedAt(LocalDateTime.now());
        }
        return movieRepository.save(movie);
    }

    public Optional<movies> findById(Long id) {
        return movieRepository.findById(id);
    }

    public Optional<movies> findByExternalId(String externalId) {
        return movieRepository.findByExternalId(externalId);
    }

    public List<movies> searchByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<movies> findByGenreId(Integer genreId) {
        return movieRepository.findByGenreId(genreId);
    }

    public List<movies> findByReleaseYear(Integer year) {
        return movieRepository.findByReleaseYear(year);
    }

    public List<movies> findAllMovies() {
        return movieRepository.findAll();
    }

    public List<MovieWithRatingDTO> findAllMoviesWithRatings() {
        List<movies> allMovies = movieRepository.findAll();
        return allMovies.stream().map(movie -> {
            var ratingStats = ratingService.getMovieRatingStats(movie.getId());
            MovieWithRatingDTO dto = new MovieWithRatingDTO();
            dto.setId(movie.getId());
            dto.setExternalId(movie.getExternalId());
            dto.setTitle(movie.getTitle());
            dto.setOverview(movie.getOverview());
            dto.setReleaseYear(movie.getReleaseYear());
            dto.setPosterUrl(movie.getPosterUrl());
            dto.setDirector(movie.getDirector());
            dto.setDuration(movie.getDuration());
            dto.setCreatedAt(movie.getCreatedAt());
            dto.setAverageRating((Double) ratingStats.get("averageRating"));
            dto.setTotalRatings((Long) ratingStats.get("totalRatings"));

            // Genre bilgilerini çek
            List<String> genreNames = movie.getMovieGenres().stream()
                    .map(mg -> mg.getGenre().getName())
                    .collect(Collectors.toList());
            dto.setGenres(genreNames);

            return dto;
        }).collect(Collectors.toList());
    }

    public Optional<MovieWithRatingDTO> findByIdWithRating(Long id) {
        return movieRepository.findById(id).map(movie -> {
            var ratingStats = ratingService.getMovieRatingStats(movie.getId());
            MovieWithRatingDTO dto = new MovieWithRatingDTO();
            dto.setId(movie.getId());
            dto.setExternalId(movie.getExternalId());
            dto.setTitle(movie.getTitle());
            dto.setOverview(movie.getOverview());
            dto.setReleaseYear(movie.getReleaseYear());
            dto.setPosterUrl(movie.getPosterUrl());
            dto.setDirector(movie.getDirector());
            dto.setDuration(movie.getDuration());
            dto.setCreatedAt(movie.getCreatedAt());
            dto.setAverageRating((Double) ratingStats.get("averageRating"));
            dto.setTotalRatings((Long) ratingStats.get("totalRatings"));

            // Genre bilgilerini çek
            List<String> genreNames = movie.getMovieGenres().stream()
                    .map(mg -> mg.getGenre().getName())
                    .collect(Collectors.toList());
            dto.setGenres(genreNames);

            return dto;
        });
    }

    public List<MovieWithRatingDTO> searchMoviesAdvanced(String keyword, Integer year, Integer genreId, Double minRating) {
        List<movies> filteredMovies;

        // Önce keyword, year ve genre ile ara
        if (keyword != null || year != null || genreId != null) {
            filteredMovies = movieRepository.searchMovies(keyword, year, genreId);
        } else {
            filteredMovies = movieRepository.findAll();
        }

        // Sonuçları DTO'ya dönüştür ve rating filtresi uygula
        return filteredMovies.stream()
                .map(movie -> {
                    var ratingStats = ratingService.getMovieRatingStats(movie.getId());
                    MovieWithRatingDTO dto = new MovieWithRatingDTO();
                    dto.setId(movie.getId());
                    dto.setExternalId(movie.getExternalId());
                    dto.setTitle(movie.getTitle());
                    dto.setOverview(movie.getOverview());
                    dto.setReleaseYear(movie.getReleaseYear());
                    dto.setPosterUrl(movie.getPosterUrl());
                    dto.setDirector(movie.getDirector());
                    dto.setDuration(movie.getDuration());
                    dto.setCreatedAt(movie.getCreatedAt());
                    dto.setAverageRating((Double) ratingStats.get("averageRating"));
                    dto.setTotalRatings((Long) ratingStats.get("totalRatings"));

                    List<String> genreNames = movie.getMovieGenres().stream()
                            .map(mg -> mg.getGenre().getName())
                            .collect(Collectors.toList());
                    dto.setGenres(genreNames);

                    return dto;
                })
                .filter(dto -> minRating == null || (dto.getAverageRating() != null && dto.getAverageRating() >= minRating))
                .collect(Collectors.toList());
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}
