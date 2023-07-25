package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "SELECT * FROM users WHERE user_name = ?", nativeQuery = true)
    Optional<UserEntity> findByUserName(String username);
}
