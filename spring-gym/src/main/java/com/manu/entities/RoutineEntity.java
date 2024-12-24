package com.manu.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "routines")
public class RoutineEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "owner", nullable = false)
  private Long owner;

  @Column(name = "days", nullable = false)
  private int days;

  @Column(name = "created", nullable = false)
  private String created;

  @Column(name = "updated", nullable = false)
  private String updated;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUpdated() {
    return updated;
  }

  public void setUpdated(String updated) {
    this.updated = updated;
  }

  public String getCreated() {
    return created;
  }

  public void setCreated(String created) {
    this.created = created;
  }

  public int getDays() {
    return days;
  }

  public void setDays(int days) {
    this.days = days;
  }

  public Long getOwner() {
    return owner;
  }

  public void setUser(Long owner) {
    this.owner = owner;
  }

  public RoutineEntity(Long owner, int days, String created, String updated) {
    this.owner = owner;
    this.days = days;
    this.created = created;
    this.updated = updated;
  }

  public RoutineEntity() {}
}
