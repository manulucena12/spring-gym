package com.manu.utils;

import com.manu.entities.UserEntity;
import com.manu.entities.WeightEntity;
import com.manu.responses.weights.UserWeightContent;
import java.util.List;

public class WeightsUtils {

  public static UserWeightContent getUserWeightContent(
      UserEntity owner, List<WeightEntity> weights) {
    var objective =
        owner.getGoal() > weights.getFirst().getWeight() ? "Gain weight" : "Loose weight";
    var kgLeft =
        objective.equals("Gain weight")
            ? owner.getGoal() <= weights.getLast().getWeight()
                ? "Already reached"
                : owner.getGoal() - weights.getLast().getWeight() + " kg left"
            : owner.getGoal() >= weights.getLast().getWeight()
                ? "Already reached"
                : weights.getLast().getWeight() - owner.getGoal() + " kg left";
    var content =
        new UserWeightContent(
            owner.getGoal(),
            weights.getLast().getCreated(),
            weights.getFirst().getCreated(),
            weights,
            kgLeft,
            objective);
    return content;
  }
}
