package com.esmaterzi.moviesystem.controller;

import com.esmaterzi.moviesystem.dto.JwtResponse;
import com.esmaterzi.moviesystem.dto.LoginRequest;
import com.esmaterzi.moviesystem.dto.MessageResponse;
import com.esmaterzi.moviesystem.dto.SignupRequest;
import com.esmaterzi.moviesystem.models.Role;
import com.esmaterzi.moviesystem.models.users;
import com.esmaterzi.moviesystem.repository.UserRepository;
import com.esmaterzi.moviesystem.security.JwtUtils;
import com.esmaterzi.moviesystem.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Null-safe id extraction to avoid static analysis NPE warning
            Long userId = Optional.ofNullable(userDetails).map(UserDetailsImpl::getId).orElse(null);

            // Kullanıcının rolünü authorities üzerinden al ve "ROLE_" önekini kaldırarak frontend'e daha temiz bir değer ver
            String role = Optional.ofNullable(userDetails)
                    .map(UserDetailsImpl::getAuthorities)
                    .flatMap(auths -> auths.stream().findFirst())
                    .map(Object::toString)
                    .orElse("ROLE_USER");

            // ROLE_ prefix'ini kaldır
            String normalizedRole = role.replaceFirst("^ROLE_", "");

            return ResponseEntity.ok(new JwtResponse(jwt,
                    userId,
                    userDetails != null ? userDetails.getUsername() : null,
                    userDetails != null ? userDetails.getEmail() : null,
                    normalizedRole));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid username or password"));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        users user = new users();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPasswordHash(encoder.encode(signUpRequest.getPassword()));
        user.setRole(Role.ROLE_USER); // Default olarak USER rolü

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
