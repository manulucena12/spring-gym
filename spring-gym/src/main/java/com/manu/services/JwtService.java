package com.manu.services;

import com.manu.entities.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Value("${jwt.secret-key}")
  protected String secretKey;

  protected Key getKey() {
    byte[] keyBites = Decoders.BASE64URL.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBites);
  }

  protected String createToken(String email, String role) {
    return Jwts.builder()
        .setClaims(new HashMap<>())
        .setSubject(email)
        .claim("role", role)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
        .signWith(getKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public Claims getAllClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
  }

  public <T> T getSingleClaim(String token, Function<Claims, T> claimsResolver) {
    return claimsResolver.apply(getAllClaims(token));
  }

  public String getEmailWithToken(String token) {
    return getSingleClaim(token, Claims::getSubject);
  }

  public Date getExpirationDate(String token) {
    return getSingleClaim(token, Claims::getExpiration);
  }

  public boolean isTokenExpired(String token) {
    return getExpirationDate(token).before(new Date(System.currentTimeMillis()));
  }

  public boolean isTokenValid(String token, UserEntity user) {
    return (Objects.equals(getSingleClaim(token, Claims::getSubject), user.getEmail())
        && !isTokenExpired(token));
  }
}
