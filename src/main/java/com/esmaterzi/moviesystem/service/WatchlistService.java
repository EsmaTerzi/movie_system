package com.esmaterzi.moviesystem.service;

import com.esmaterzi.moviesystem.models.*;
import com.esmaterzi.moviesystem.repository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WatchlistService {

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;

    public watchlists createWatchlist(Long userId, String name) {
        users user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        watchlists watchlist = new watchlists();
        watchlist.setUser(user);
        watchlist.setName(name);
        watchlist.setCreatedAt(LocalDateTime.now());
        watchlist.setWatchlistMovies(new HashSet<>());

        return watchlistRepository.save(watchlist);
    }

    public watchlists addMovieToWatchlist(Long watchlistId, Long movieId) {
        watchlists watchlist = watchlistRepository.findById(watchlistId)
                .orElseThrow(() -> new RuntimeException("Watchlist not found"));

        movies movie = movieService.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        watchlist_movies wm = new watchlist_movies();
        WatchlistMovieId wmId = new WatchlistMovieId(watchlistId, movieId);
        wm.setId(wmId);
        wm.setWatchlist(watchlist);
        wm.setMovie(movie);
        wm.setAddedAt(LocalDateTime.now());

        if (watchlist.getWatchlistMovies() == null) {
            watchlist.setWatchlistMovies(new HashSet<>());
        }
        watchlist.getWatchlistMovies().add(wm);

        return watchlistRepository.save(watchlist);
    }

    public void removeMovieFromWatchlist(Long watchlistId, Long movieId) {
        watchlists watchlist = watchlistRepository.findById(watchlistId)
                .orElseThrow(() -> new RuntimeException("Watchlist not found"));

        if (watchlist.getWatchlistMovies() != null) {
            watchlist.getWatchlistMovies().removeIf(wm ->
                wm.getMovie().getId().equals(movieId));
            watchlistRepository.save(watchlist);
        }
    }

    public List<watchlists> getUserWatchlists(Long userId) {
        return watchlistRepository.findByUserId(userId);
    }

    public Optional<watchlists> findById(Long id) {
        return watchlistRepository.findById(id);
    }

    public void deleteWatchlist(Long id) {
        watchlistRepository.deleteById(id);
    }

    public List<watchlists> searchUserWatchlists(Long userId, String name) {
        return watchlistRepository.findByUserIdAndNameContainingIgnoreCase(userId, name);
    }
}
