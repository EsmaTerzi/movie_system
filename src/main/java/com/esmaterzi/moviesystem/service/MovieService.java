package com.esmaterzi.moviesystem.service;

import com.esmaterzi.moviesystem.dto.MovieRequest;
import com.esmaterzi.moviesystem.dto.MovieWithRatingDTO;
import com.esmaterzi.moviesystem.models.MovieGenreId;
import com.esmaterzi.moviesystem.models.genres;
import com.esmaterzi.moviesystem.models.movie_genres;
import com.esmaterzi.moviesystem.models.movies;
import com.esmaterzi.moviesystem.repository.GenreRepository;
import com.esmaterzi.moviesystem.repository.MovieGenreRepository;
import com.esmaterzi.moviesystem.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MovieGenreRepository movieGenreRepository;

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


    public List<movies> searchByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<movies> findByGenreId(Integer genreId) {
        return movieRepository.findByGenreId(genreId);
    }

    public List<movies> findByReleaseYear(Integer year) {
        return movieRepository.findByReleaseYear(year);
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

            // Genre isimlerini çek
            List<String> genreNames = movie.getMovieGenres().stream()
                    .map(mg -> mg.getGenre().getName())
                    .collect(Collectors.toList());
            dto.setGenresNames(genreNames);

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

            // Genre isimlerini çek
            List<String> genreNames = movie.getMovieGenres().stream()
                    .map(mg -> mg.getGenre().getName())
                    .collect(Collectors.toList());
            dto.setGenresNames(genreNames);

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

                    // Genre isimlerini çek
                    List<String> genreNames = movie.getMovieGenres().stream()
                            .map(mg -> mg.getGenre().getName())
                            .collect(Collectors.toList());
                    dto.setGenresNames(genreNames);

                    return dto;
                })
                .filter(dto -> minRating == null || (dto.getAverageRating() != null && dto.getAverageRating() >= minRating))
                .collect(Collectors.toList());
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public movies createMovieWithGenres(MovieRequest request) {
        movies movie = new movies();
        movie.setExternalId(request.getExternalId());
        movie.setTitle(request.getTitle());
        movie.setOverview(request.getOverview());
        movie.setReleaseYear(request.getReleaseYear());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setDirector(request.getDirector());
        movie.setDuration(request.getDuration());
        movie.setCreatedAt(LocalDateTime.now());
        movie.setMovieGenres(new HashSet<>()); // Set'i başlat

        // Önce movie'yi kaydet
        movies savedMovie = movieRepository.save(movie);
        movieRepository.flush(); // Değişiklikleri hemen uygula

        // Genre'leri ekle
        if (request.getGenreIds() != null && !request.getGenreIds().isEmpty()) {
            for (Integer genreId : request.getGenreIds()) {
                genres genre = genreRepository.findById(genreId)
                        .orElseThrow(() -> new RuntimeException("Genre not found with id: " + genreId));

                movie_genres movieGenre = new movie_genres();
                MovieGenreId movieGenreId = new MovieGenreId(savedMovie.getId(), genreId);
                movieGenre.setId(movieGenreId);
                movieGenre.setMovie(savedMovie);
                movieGenre.setGenre(genre);

                // Her ikisini de dene: hem Set'e ekle hem de direkt kaydet
                savedMovie.getMovieGenres().add(movieGenre);
                movieGenreRepository.save(movieGenre);
            }
            // Son bir kez daha kaydet ve flush yap
            movieRepository.saveAndFlush(savedMovie);
        }

        return savedMovie;
    }

    public movies updateMovieWithGenres(Long movieId, MovieRequest request) {
        movies movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + movieId));

        // Film bilgilerini güncelle
        movie.setExternalId(request.getExternalId());
        movie.setTitle(request.getTitle());
        movie.setOverview(request.getOverview());
        movie.setReleaseYear(request.getReleaseYear());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setDirector(request.getDirector());
        movie.setDuration(request.getDuration());

        movies updatedMovie = movieRepository.save(movie);
        movieRepository.flush();

        // Genre'leri güncelle
        if (request.getGenreIds() != null && !request.getGenreIds().isEmpty()) {
            // Eski genre'leri sil
            movieGenreRepository.deleteByMovieId(movieId);
            movieGenreRepository.flush();

            // Yeni genre'leri ekle
            if (updatedMovie.getMovieGenres() == null) {
                updatedMovie.setMovieGenres(new HashSet<>());
            } else {
                updatedMovie.getMovieGenres().clear();
            }

            for (Integer genreId : request.getGenreIds()) {
                genres genre = genreRepository.findById(genreId)
                        .orElseThrow(() -> new RuntimeException("Genre not found with id: " + genreId));

                movie_genres movieGenre = new movie_genres();
                MovieGenreId movieGenreId = new MovieGenreId(updatedMovie.getId(), genreId);
                movieGenre.setId(movieGenreId);
                movieGenre.setMovie(updatedMovie);
                movieGenre.setGenre(genre);

                // Her ikisini de dene
                updatedMovie.getMovieGenres().add(movieGenre);
                movieGenreRepository.save(movieGenre);
            }

            movieRepository.saveAndFlush(updatedMovie);
        }

        return updatedMovie;
    }
}
