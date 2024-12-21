package com.manu.services;

import com.manu.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class UserDetailsForFilterService {

  @Autowired UserRepository userRepository;

  public UserDetails getUserByUsername(String email) {
    return userDetailsService().loadUserByUsername(email);
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return email -> {
      var user =
          userRepository
              .findByEmail(email)
              .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
      return user;
    };
  }
}
