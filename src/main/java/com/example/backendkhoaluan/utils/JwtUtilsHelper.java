package com.example.backendkhoaluan.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtilsHelper {
    @Value("${jwt.privateKey}")
    private String privateKey;

    private long expiredTime=8*60*60*1000;

    public String generateToken(String data){
        SecretKey key= Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
        Date date=new Date();
        long futureMilis=date.getTime()+expiredTime;
        Date futureDate=new Date(futureMilis);
        String jws = Jwts.builder().expiration(futureDate).subject(data).signWith(key).compact();

        return jws;
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
