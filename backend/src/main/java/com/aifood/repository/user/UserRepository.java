package com.aifood.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aifood.entity.user.User;


public interface UserRepository extends JpaRepository<User,Long> {
    
    Optional<User> findByEmail(String email);
}
