package com.manu.services;

import com.manu.entities.DayEntity;
import com.manu.entities.RoutineEntity;
import com.manu.entities.UserEntity;
import com.manu.repositories.DayRepository;
import com.manu.repositories.RoutineRepository;
import com.manu.repositories.UserRepository;
import com.manu.requests.auth.LoginRequest;
import com.manu.requests.auth.RegisterRequest;
import com.manu.responses.HttpServiceResponse;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired UserRepository userRepository;

  @Autowired RoutineRepository routineRepository;

  @Autowired private DayRepository dayRepository;

  @Autowired PasswordEncoder passwordEncoder;

  @Autowired JwtService jwtService;

  @Autowired AuthenticationManager authenticationManager;

  public HttpServiceResponse<Object> register(RegisterRequest body) {
    try {
      if (userRepository.findByEmail(body.getEmail()).isPresent()) {
        return new HttpServiceResponse<>(
            400, null, "This email has already been registered, use another one");
      }
      if (body.getDays() == 0) {
        return new HttpServiceResponse<>(
            400, null, "You have to set a minimum days greater than 0");
      }
      UserEntity newUser =
          new UserEntity(
              body.getName(),
              passwordEncoder.encode(body.getPassword()),
              body.getEmail(),
              body.getAge(),
              "user",
              body.getGoal(),
              body.getWeight());
      var user = userRepository.save(newUser);
      var newRoutine =
          new RoutineEntity(
              user.getId(), body.getDays(), new Date().toString(), new Date().toString());
      var routine = routineRepository.save(newRoutine);
      var routineId = routine.getId();
      for (int i = 0; i < body.getDays(); i++) {
        var newDay = new DayEntity(routine, "Undefined", "Unnamed Workout", List.of());
        dayRepository.save(newDay);
      }
      return new HttpServiceResponse<>(
          201,
          "Bearer " + jwtService.createToken(user.getEmail(), user.getRole(), user.getId()),
          null);
    } catch (Exception e) {
      if (e instanceof DataIntegrityViolationException) {
        return new HttpServiceResponse<>(400, null, "Insufficient data to register");
      }
      System.out.println(e);
      return new HttpServiceResponse<>(500, null, "Internal server error");
    }
  }

  public HttpServiceResponse<Object> login(LoginRequest body) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword()));
      var user = userRepository.findByEmail(body.getEmail());
      if (user.isEmpty()) {
        return new HttpServiceResponse<>(400, null, "Bad credentials");
      }
      return new HttpServiceResponse<>(
          200,
          "Bearer "
              + jwtService.createToken(body.getEmail(), user.get().getRole(), user.get().getId()),
          null);
    } catch (Exception e) {
      if (e instanceof BadCredentialsException) {
        return new HttpServiceResponse<>(400, null, "Bad credentials");
      }
      System.out.println(e);
      return new HttpServiceResponse<>(500, null, "Internal server error");
    }
  }
}
