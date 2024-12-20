package com.manu.repositories;

import com.manu.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM users WHERE email = ?1")
    Optional<UserEntity> findByEmail();

}
