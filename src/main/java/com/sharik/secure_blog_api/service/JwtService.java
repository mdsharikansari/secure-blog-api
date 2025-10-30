package com.sharik.secure_blog_api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sharik.secure_blog_api.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    // set this secret key in our application.properties
    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    // Generates a token for a user
    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        return JWT.create()
                .withSubject(user.getUsername()) // Set the user's name as the "subject"
                .withIssuedAt(new Date(System.currentTimeMillis())) // Set the "issued at" time to now
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Set to expire in 24 hours
                .sign(algorithm); // Sign the token with our secret key
    }

    // Validates a token and extracts the username
    public String extractUsername(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT decodedJWT = verifier.verify(token); // throws an error if the token is invalid

        return decodedJWT.getSubject();
    }

    // Helper method
    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    // Helper method to check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Helper method to get the expiration date from the token
    private Date extractExpiration(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT decodedJWT = verifier.verify(token);

        return decodedJWT.getExpiresAt();
    }
}
