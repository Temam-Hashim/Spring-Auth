package com.security.auth.config;

import com.security.auth.entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.util.function.Function;


@Component
@RequiredArgsConstructor
public class JwtUtils {
    //    @Value("${jwt.secretKey}")
    private String secretKey="secret";

    //    @Value("${jwt.expirationDateInMs}")
    private int jwtExpirationInMs= 86400000;

    private final Long refreshTokenExpirationInMs = 2592000000L; // 30 days


    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    public boolean hasClaim(String token, String claimName){
        final Claims claims = extractAllClaims(token);
        return claims.get(claimName) != null;
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver)  {
        final Claims claims  = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }

    public  Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, Users userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }


    // generate token for user
    // Generate token with specified expiration time
    private String createToken(Map<String, Object> claims, Users userDetails, long expirationTime) {
        claims.put("mobile", userDetails.getMobile());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    private String generateToken(Map<String, Object> claims, Users userDetails) {
        return createToken(claims,userDetails,jwtExpirationInMs);
    }
    public String generateToken(Users userDetails){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,userDetails,jwtExpirationInMs);

    }

    // Generate refresh token for user
    public String generateRefreshToken(Users userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails, refreshTokenExpirationInMs);
    }

    public boolean isRefreshTokenValid(String refreshToken, Users userDetails) {
        final String username = extractUsername(refreshToken);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(refreshToken));
    }


}
