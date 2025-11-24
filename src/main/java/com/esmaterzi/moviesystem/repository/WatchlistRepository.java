package com.esmaterzi.moviesystem.repository;

import com.esmaterzi.moviesystem.models.watchlists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchlistRepository extends JpaRepository<watchlists, Long> {
    List<watchlists> findByUserId(Long userId);
    List<watchlists> findByUserIdAndNameContainingIgnoreCase(Long userId, String name);
}

