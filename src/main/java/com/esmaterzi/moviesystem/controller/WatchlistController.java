package com.esmaterzi.moviesystem.controller;

import com.esmaterzi.moviesystem.dto.MessageResponse;
import com.esmaterzi.moviesystem.dto.WatchlistMovieRequest;
import com.esmaterzi.moviesystem.dto.WatchlistRequest;
import com.esmaterzi.moviesystem.models.watchlists;
import com.esmaterzi.moviesystem.security.UserDetailsImpl;
import com.esmaterzi.moviesystem.service.WatchlistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    @Autowired
    private WatchlistService watchlistService;

    @GetMapping
    public ResponseEntity<List<watchlists>> getUserWatchlists(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(watchlistService.getUserWatchlists(userDetails.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWatchlistById(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return watchlistService.findById(id)
                .filter(watchlist -> watchlist.getUser().getId().equals(userDetails.getId()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<watchlists>> searchWatchlists(
            @RequestParam String name,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(watchlistService.searchUserWatchlists(userDetails.getId(), name));
    }

    @PostMapping
    public ResponseEntity<?> createWatchlist(
            @Valid @RequestBody WatchlistRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            watchlists watchlist = watchlistService.createWatchlist(userDetails.getId(), request.getName());
            return ResponseEntity.ok(watchlist);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/movies")
    public ResponseEntity<?> addMovieToWatchlist(
            @PathVariable Long id,
            @Valid @RequestBody WatchlistMovieRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            watchlists watchlist = watchlistService.findById(id)
                    .filter(wl -> wl.getUser().getId().equals(userDetails.getId()))
                    .orElseThrow(() -> new RuntimeException("Watchlist not found or access denied"));

            watchlists updatedWatchlist = watchlistService.addMovieToWatchlist(id, request.getMovieId());
            return ResponseEntity.ok(updatedWatchlist);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{watchlistId}/movies/{movieId}")
    public ResponseEntity<?> removeMovieFromWatchlist(
            @PathVariable Long watchlistId,
            @PathVariable Long movieId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            watchlists watchlist = watchlistService.findById(watchlistId)
                    .filter(wl -> wl.getUser().getId().equals(userDetails.getId()))
                    .orElseThrow(() -> new RuntimeException("Watchlist not found or access denied"));

            watchlistService.removeMovieFromWatchlist(watchlistId, movieId);
            return ResponseEntity.ok(new MessageResponse("Movie removed from watchlist successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWatchlist(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            watchlists watchlist = watchlistService.findById(id)
                    .filter(wl -> wl.getUser().getId().equals(userDetails.getId()))
                    .orElseThrow(() -> new RuntimeException("Watchlist not found or access denied"));

            watchlistService.deleteWatchlist(id);
            return ResponseEntity.ok(new MessageResponse("Watchlist deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}
