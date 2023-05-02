package com.example.back_end_mobile_project.utils.config.security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

@Component
public class JwtProvider implements Serializable {

    private static final String ROLE_USER = "ROLE_USER";

    private final String JWT_SECRET = "secret";

    private int jwtExpirationInMs;

    private int refreshExpirationDateInMs;

    @Value("${jwt.expirationDateInMs}")
    public void setJwtExpirationInMs(int jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    @Value("${jwt.refreshExpirationDateInMs}")
    public void setRefreshExpirationDateInMs(int refreshExpirationDateInMs) {
        this.refreshExpirationDateInMs = refreshExpirationDateInMs;
    }

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        Collection<? extends GrantedAuthority> collection = userDetails.getAuthorities();
        if(collection.contains(new SimpleGrantedAuthority(ROLE_USER))){
            claims.put("isUser", true);
        }
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject){
        Date expirationDate = new Date(System.currentTimeMillis() + jwtExpirationInMs);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String doGenerateRefreshToken(Map<String, Object> claims, String subject){
        Date expirationDate = new Date(System.currentTimeMillis() + refreshExpirationDateInMs);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String getUsernameFromToken(String token){
        final Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public Boolean validateToken(String token, UserDetails userDetails){
       try{
           String username = getUsernameFromToken(token);
           Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
           boolean isTokenExpired = claims.getExpiration().before(new Date());
           return (username.equals(userDetails.getUsername()) && !isTokenExpired);
       }catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
           throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
       } catch (ExpiredJwtException e) {
           throw e;
       }
    }

    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
        List<SimpleGrantedAuthority> roles = null;
        Boolean isUser = claims.get("isUser", Boolean.class);
        if (isUser != null) {
            roles = List.of(new SimpleGrantedAuthority(ROLE_USER));
        }
        return roles;
    }

    public Claims getClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
    }

}
