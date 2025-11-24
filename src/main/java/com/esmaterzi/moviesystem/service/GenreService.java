package com.esmaterzi.moviesystem.service;

import com.esmaterzi.moviesystem.models.genres;
import com.esmaterzi.moviesystem.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public genres saveGenre(genres genre) {
        return genreRepository.save(genre);
    }

    public Optional<genres> findById(Integer id) {
        return genreRepository.findById(id);
    }

    public Optional<genres> findByName(String name) {
        return genreRepository.findByName(name);
    }

    public List<genres> findAllGenres() {
        return genreRepository.findAll();
    }

    public genres findOrCreateGenre(String name) {
        return genreRepository.findByName(name)
                .orElseGet(() -> {
                    genres newGenre = new genres();
                    newGenre.setName(name);
                    return genreRepository.save(newGenre);
                });
    }
}

