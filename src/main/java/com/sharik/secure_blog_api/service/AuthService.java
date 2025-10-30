package com.sharik.secure_blog_api.service;

import com.sharik.secure_blog_api.dto.AuthResponse;
import com.sharik.secure_blog_api.dto.LoginRequest;
import com.sharik.secure_blog_api.dto.RegisterRequest;
import com.sharik.secure_blog_api.model.User;
import com.sharik.secure_blog_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // creates a constructor for all final fields
public class AuthService {

    // all these beans that created in config files
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void register(RegisterRequest request) {
        // Create a new User object from the request
        User user = new User();
        user.setUsername(request.username());

        // must HASH the password before saving
        user.setPassword(passwordEncoder.encode(request.password()));

        // Save the new user to database
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        // checks if the username and password are correct
        // If not, it will throw an exception
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        // If authentication successful, find user
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate a token for that user
        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}
