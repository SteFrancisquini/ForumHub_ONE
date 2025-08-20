package dev.stefrancisquini.forumhub.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class TokenService {
    private final String SECRET_KEY = "8908e07a-32e5-4d8b-867f-5174c9ad80eb";
    private final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

    public String gerarToken(String subject) {
        // 1 day
        long EXPIRATION = 86400000;
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION))
                .sign(algorithm);
    }

    public String extractSubject(String token) {
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return jwt.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
            return jwt.getExpiresAt().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}

