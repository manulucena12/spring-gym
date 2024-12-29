package com.manu.repositories;

import com.manu.entities.ExerciseEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<ExerciseEntity, Long> {

  @Query(
      nativeQuery = true,
      value =
          "SELECT owner FROM (SELECT routine FROM exercises e INNER JOIN days d ON d.id = e.day_id WHERE e.id = ?1) d INNER JOIN routines r ON d.routine = r.id")
  Optional<Long> findOwnerById(Long id);
}
