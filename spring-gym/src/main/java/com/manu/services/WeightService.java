package com.manu.services;

import com.manu.entities.WeightEntity;
import com.manu.exceptions.WeightsNotFoundException;
import com.manu.repositories.UserRepository;
import com.manu.repositories.WeightRepository;
import com.manu.requests.weights.NewWeightRequest;
import com.manu.requests.weights.UpdateWeightRequest;
import com.manu.responses.HttpServiceResponse;
import com.manu.utils.WeightsUtils;
import java.util.Date;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeightService {

  @Autowired private WeightRepository weightRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private JwtService jwtService;

  public HttpServiceResponse<Object> getAllWeights(Long id) {
    try {
      var weights =
          weightRepository
              .findByOwner(id)
              .orElseThrow(() -> new WeightsNotFoundException("Not found"));
      if (weights.isEmpty()) {
        return new HttpServiceResponse<>(400, null, "Weights not found");
      }
      var owner =
          userRepository
              .findById(weights.getFirst().getOwner())
              .orElseThrow(() -> new WeightsNotFoundException("Not found"));
      if (!jwtService.isOwner(owner.getId())) {
        return new HttpServiceResponse<>(
            401, null, "You cannot access to weights those are not yours");
      }
      var content = WeightsUtils.getUserWeightContent(owner, weights);
      return new HttpServiceResponse<>(200, content, null);
    } catch (Exception e) {
      if (e instanceof WeightsNotFoundException) {
        return new HttpServiceResponse<>(400, null, "Weights not found");
      }
      System.out.println(e);
      return new HttpServiceResponse<>(500, null, "Internal server error");
    }
  }

  public HttpServiceResponse<Object> getSingleWeight(Long id) {
    try {
      var weight =
          weightRepository
              .findById(id)
              .orElseThrow(() -> new WeightsNotFoundException("Not found"));
      if (!jwtService.isOwner(weight.getOwner())) {
        return new HttpServiceResponse<>(
            401, null, "You cannot access to a weight those are not yours");
      }
      return new HttpServiceResponse<>(200, weight, null);
    } catch (Exception e) {
      if (e instanceof WeightsNotFoundException) {
        return new HttpServiceResponse<>(400, null, "Weight not found");
      }
      System.out.println(e);
      return new HttpServiceResponse<>(500, null, "Internal server error");
    }
  }

  public HttpServiceResponse<Object> updateWeight(Long id, UpdateWeightRequest body) {
    try {
      var weight =
          weightRepository
              .findById(id)
              .orElseThrow(() -> new WeightsNotFoundException("Not found"));
      if (!jwtService.isOwner(weight.getOwner())) {
        return new HttpServiceResponse<>(400, null, "You cannot update a weight that is not yours");
      }
      weight.setWeight(body.getWeight());
      weight.setUpdated(new Date().toString());
      var updatedWeight = weightRepository.save(weight);
      return new HttpServiceResponse<>(200, updatedWeight, null);
    } catch (Exception e) {
      if (e instanceof WeightsNotFoundException) {
        return new HttpServiceResponse<>(400, null, "Weight not found");
      }
      System.out.println(e);
      return new HttpServiceResponse<>(500, null, "Internal server error");
    }
  }

  public HttpServiceResponse<Object> deleteWeight(Long id) {
    try {
      var weight =
          weightRepository
              .findById(id)
              .orElseThrow(() -> new WeightsNotFoundException("Not found"));
      if (!jwtService.isOwner(weight.getOwner())) {
        return new HttpServiceResponse<>(400, null, "You cannot delete a weight that is not yours");
      }
      var weights =
          weightRepository
              .findByOwner(weight.getOwner())
              .orElseThrow(() -> new WeightsNotFoundException("Not found"));
      if (Objects.equals(weight.getId(), weights.getFirst().getId())) {
        return new HttpServiceResponse<>(
            400, null, "You cannot delete your first weight, delete the others or update this");
      }
      weightRepository.deleteById(id);
      return new HttpServiceResponse<>(200, null, "Your weight has been deleted successfully");
    } catch (Exception e) {
      if (e instanceof WeightsNotFoundException) {
        return new HttpServiceResponse<>(400, null, "Weight not found");
      }
      System.out.println(e);
      return new HttpServiceResponse<>(500, null, "Internal server error");
    }
  }

  public HttpServiceResponse<Object> createWeight(NewWeightRequest body) {
    try {
      if (!jwtService.isOwner(body.getOwner())) {
        return new HttpServiceResponse<>(
            400, null, "You cannot create a weight with an id that is not yours");
      }
      var newWeight =
          new WeightEntity(body.getWeight(), body.getOwner(), new Date().toString(), "Not yet");
      var createdWeight = weightRepository.save(newWeight);
      return new HttpServiceResponse<>(201, createdWeight, null);
    } catch (Exception e) {
      System.out.println(e);
      return new HttpServiceResponse<>(500, null, "Internal server error");
    }
  }
}
