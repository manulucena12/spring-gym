package com.manu.services;

import com.manu.repositories.UserRepository;
import com.manu.responses.HttpServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired private UserRepository userRepository;

  @Autowired private JwtService jwtService;

  public HttpServiceResponse<Object> getAllUsers() {
    try {
      if (!jwtService.isAdmin()) {
        return new HttpServiceResponse<>(401, null, "Only admins can access to this information");
      }
      return new HttpServiceResponse<>(200, userRepository.findAll(), null);
    } catch (Exception e) {
      return new HttpServiceResponse<>(500, null, "Internal server error");
    }
  }
}
