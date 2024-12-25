package com.manu.repositories;

import com.manu.entities.RoutineEntity;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepository extends JpaRepository<RoutineEntity, Long> {

  @Query(nativeQuery = true, value = "SELECT * FROM routines WHERE owner = ?1")
  Optional<RoutineEntity> findByOwner(Long id);

  @Modifying
  @Transactional
  @Query(nativeQuery = true, value = "UPDATE routines SET days = ?1, updated = ?2 WHERE id = ?3")
  void updateById(int Days, String date, Long routineId);
}
