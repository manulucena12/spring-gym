package com.manu.repositories;

import com.manu.entities.DayEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DayRepository extends JpaRepository<DayEntity, Long> {
  @Query(nativeQuery = true, value = "SELECT * FROM days WHERE routine = ?1")
  Optional<List<DayEntity>> findByRoutine(Long routineId);
}
