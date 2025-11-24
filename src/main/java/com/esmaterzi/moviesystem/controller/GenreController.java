package com.esmaterzi.moviesystem.controller;

import com.esmaterzi.moviesystem.dto.MessageResponse;
import com.esmaterzi.moviesystem.models.genres;
import com.esmaterzi.moviesystem.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @GetMapping
    public ResponseEntity<List<genres>> getAllGenres() {
        return ResponseEntity.ok(genreService.findAllGenres());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGenreById(@PathVariable Integer id) {
        return genreService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getGenreByName(@PathVariable String name) {
        return genreService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createGenre(@RequestBody genres genre) {
        try {
            genres savedGenre = genreService.saveGenre(genre);
            return ResponseEntity.ok(savedGenre);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}

