package com.esmaterzi.moviesystem.controller;

import com.esmaterzi.moviesystem.dto.SignupRequest;
import com.esmaterzi.moviesystem.models.Role;
import com.esmaterzi.moviesystem.models.users;
import com.esmaterzi.moviesystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Value("${frontend.url}")
    private String frontendUrl;

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("frontendUrl", frontendUrl);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {

        model.addAttribute("frontendUrl", frontendUrl);

        // Şifre eşleşme kontrolü
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Şifreler eşleşmiyor!");
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            return "register";
        }

        // Kullanıcı adı kontrolü
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "Bu kullanıcı adı zaten kullanılıyor!");
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            return "register";
        }

        // Email kontrolü
        if (userRepository.existsByEmail(email)) {
            model.addAttribute("error", "Bu e-posta adresi zaten kullanılıyor!");
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            return "register";
        }

        try {
            // Yeni kullanıcı oluştur
            users user = new users();
            user.setUsername(username);
            user.setEmail(email);
            user.setPasswordHash(encoder.encode(password));
            user.setRole(Role.ROLE_USER);

            userRepository.save(user);

            model.addAttribute("success", "Kayıt başarılı! Giriş yapabilirsiniz.");
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "Kayıt sırasında bir hata oluştu: " + e.getMessage());
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            return "register";
        }
    }
}
