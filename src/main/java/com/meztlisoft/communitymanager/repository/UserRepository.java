package com.meztlisoft.communitymanager.repository;

import com.meztlisoft.communitymanager.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("FROM UserEntity u WHERE u.userName = :username")
    Optional<UserEntity> findByUserName(String username);

    @Query("FROM UserEntity u WHERE u.citizen.id = :citizenId")
    UserEntity findByCitizenId(long citizenId);
}
