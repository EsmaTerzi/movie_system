package com.esmaterzi.moviesystem.service;

import com.esmaterzi.moviesystem.models.movies;
import com.esmaterzi.moviesystem.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

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

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}

