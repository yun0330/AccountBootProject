package com.walletstory.server.repository;

import com.walletstory.server.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsById(String userId);
    UserEntity findByUserId(String userId);
}
