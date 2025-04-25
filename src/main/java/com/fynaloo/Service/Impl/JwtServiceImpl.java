package com.fynaloo.Service.Impl;

import com.fynaloo.Service.IJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements IJwtService {

    private static final String SECRET_KEY = "11c25bd31f23a2601ab1df4050a5fbb44f5723a37a4ab3bc7c26547beddb326644c00590a71a09ad49aff871de3f4ccac4bc335acf70aeaf879c14023ecf033bf26561540cf7641b74ffb5c1e8698220bd51c2de04df6362dfb1d129900f8858401cb9741e91b889b2fca82e02a7d13b932186875f0049f48f4dcd1e22efef7f6083f42f99ff2570c92dd30180349c7a53b9d6f765e34c76ba0f4ebe944a96294c7a855f876af348407d88d136ef7daeb4cd43bee27f153286ad4d3f10ec4171046c70b334d812d3241276fb9bd7c8540afd9a6d353964362aff3679d9ec67813006657d7268f828ae32491c7f54887f9dce015fe39d62715a736e743c8e78a8";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
