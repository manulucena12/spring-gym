package com.manu.repositories;

import com.manu.entities.RoutineEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepository extends JpaRepository<RoutineEntity, Long> {
  @Query(nativeQuery = true, value = "SELECT * FROM routines WHERE user = ?1")
  Optional<RoutineEntity> findByOwner(Long id);
}
