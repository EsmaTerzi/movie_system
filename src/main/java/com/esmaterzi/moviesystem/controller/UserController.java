package com.esmaterzi.moviesystem.controller;

import com.esmaterzi.moviesystem.dto.UpdateUserRequest;
import com.esmaterzi.moviesystem.dto.UserResponse;
import com.esmaterzi.moviesystem.models.users;
import com.esmaterzi.moviesystem.security.UserDetailsImpl;
import com.esmaterzi.moviesystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.findById(userDetails.getId())
                .map(user -> {
                    UserResponse response = new UserResponse(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getRole(),
                            user.getCreatedAt()
                    );
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UpdateUserRequest updateRequest) {
        try {
            users updatedUser = userService.updateUser(
                    userDetails.getId(),
                    updateRequest.getUsername(),
                    updateRequest.getEmail()
            );

            UserResponse response = new UserResponse(
                    updatedUser.getId(),
                    updatedUser.getUsername(),
                    updatedUser.getEmail(),
                    updatedUser.getRole(),
                    updatedUser.getCreatedAt()
            );

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
