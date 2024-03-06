package com.example.backendkhoaluan.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtUtilsHelper {
    @Value("${jwt.privateKey}")
    private String privateKey;

    private long expiredTime=24*60*60*1000;

    public String generateToken(String email){
        SecretKey key=Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiredTime*30))
                .subject(email)
                .signWith(key).compact();
    }

    public String generateRefreshToken(HashMap<String,Object> claims, String email){
        SecretKey key=Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiredTime*30))
                .subject(email)
                .signWith(key).compact();
    }

    public String extractEmail(String token){
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims,T> claimsTFunction) {
        SecretKey key=Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
        return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
    }

    public boolean isTokenValid(String token, String emailUser){
        final String email=extractEmail(token);
        return (email.equals(emailUser)&&!isTokenExpried(token));
    }

    private boolean isTokenExpried(String token) {
        return extractClaims(token,Claims::getExpiration).before(new Date());
    }

    public String verifyToken(String token){
        try {
            SecretKey key= Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));

            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
        }catch (Exception e){
            return null;
        }
    }
}
