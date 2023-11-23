package com.user.demo.service;

import com.user.demo.model.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
@Service
public class TokenService {
    private static final String KEY;

    static {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA512");
            SecretKey secretKey = keyGen.generateKey();
            KEY = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Error al generar la clave secreta", e);
        }
    }
    public String generateToken(Users users) {
        long now = System.currentTimeMillis();
        return Jwts.builder().subject(users.getEmail()).issuedAt(new Date(now)).expiration(new Date(now + 900000)) // Token válido por 15 minutos
                .signWith(Keys.hmacShaKeyFor(KEY.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}

