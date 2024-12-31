package com.manu.repositories;

import com.manu.entities.WeightEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WeightRepository extends JpaRepository<WeightEntity, Long> {

  @Query(nativeQuery = true, value = "SELECT * FROM weights WHERE owner = ?1")
  Optional<List<WeightEntity>> findByOwner(Long id);
}
