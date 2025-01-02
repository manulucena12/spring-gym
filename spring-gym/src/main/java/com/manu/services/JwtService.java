package com.manu.services;

import com.manu.entities.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Autowired private HttpServletRequest request;

  @Value("${jwt.secret-key}")
  protected String secretKey;

  protected Key getKey() {
    byte[] keyBites = Decoders.BASE64URL.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBites);
  }

  protected String createToken(String email, String role, Long id) {
    return Jwts.builder()
        .setClaims(new HashMap<>())
        .setSubject(email)
        .claim("role", role)
        .claim("id", id)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + (604800000)))
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

  public String getRoleFromToken(String token) {
    return getSingleClaim(token, claims -> claims.get("role", String.class));
  }

  public Long getIdFromToken(String token) {
    return getSingleClaim(token, claims -> claims.get("id", Long.class));
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

  public boolean isAdmin() {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    String token;
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      token = authHeader.substring(7);
      var role = getRoleFromToken(token);
      return Objects.equals(role, "admin");
    }
    return false;
  }

  public boolean isOwner(Long id) {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    String token;
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      token = authHeader.substring(7);
      var idFromToken = getIdFromToken(token);
      return Objects.equals(idFromToken, id);
    }
    return false;
  }
}
