package com.manu.filter;

import com.manu.repositories.UserRepository;
import com.manu.services.JwtService;
import com.manu.services.UserDetailsForFilterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

  @Autowired private JwtService jwtService;

  @Autowired private UserDetailsForFilterService userDetailsForFilterService;

  @Autowired private UserRepository userRepository;

  @Override
  public void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (request.getRequestURI().startsWith("/auth")) {
      filterChain.doFilter(request, response);
      return;
    }
    final String token = getTokenFromRequest(request);
    if (token == null) {
      filterChain.doFilter(request, response);
      return;
    }

    String email = jwtService.getEmailWithToken(token);

    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      var dbUser = userRepository.findByEmail(email);
      if (jwtService.isTokenValid(token, dbUser.get())) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(dbUser, null, dbUser.get().getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }
    }

    filterChain.doFilter(request, response);
  }

  public String getTokenFromRequest(HttpServletRequest request) {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      return authHeader.substring(7);
    }
    return null;
  }
}
