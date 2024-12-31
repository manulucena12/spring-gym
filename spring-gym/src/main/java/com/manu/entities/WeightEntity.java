package com.manu.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "weights")
public class WeightEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "weight", nullable = false)
  private double weight;

  @Column(name = "owner", nullable = false)
  @JsonIgnore
  private Long owner;

  @Column(name = "created", nullable = false)
  private String created;

  @Column(name = "updated", nullable = false)
  private String updated;

  public WeightEntity(double weight, Long owner, String created, String updated) {
    this.weight = weight;
    this.owner = owner;
    this.created = created;
    this.updated = updated;
  }

  public WeightEntity() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  public String getCreated() {
    return created;
  }

  public void setCreated(String created) {
    this.created = created;
  }

  public String getUpdated() {
    return updated;
  }

  public void setUpdated(String updated) {
    this.updated = updated;
  }

  public Long getOwner() {
    return owner;
  }

  public void setOwner(Long owner) {
    this.owner = owner;
  }
}
