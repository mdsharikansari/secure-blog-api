package com.sharik.secure_blog_api.config;

import com.sharik.secure_blog_api.model.User;
import com.sharik.secure_blog_api.repository.UserRepository;
import com.sharik.secure_blog_api.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Tells Spring this is a bean it should manage
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter { // Runs one time for every request

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Get "Authorization" header from request
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // If there's no header, or it doesn't start with "Bearer ", pass the request along
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the token
        jwt = authHeader.substring(7);

        try {
            // Extract the username from the token
            username = jwtService.extractUsername(jwt);

            // If a username and the user is not already authenticated
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Find user in the database
                User user = this.userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                // If the token is valid for this user
                if (jwtService.isTokenValid(jwt, user)) {

                    // create an "auth token" and tell Spring Security's context that this user is now authenticated.
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null, // We don't need credentials
                            user.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // Update
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            // Pass the request to the next filter in the chain
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // If the token is invalid, send a 403 Forbidden error
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid Token");
        }
    }
}
