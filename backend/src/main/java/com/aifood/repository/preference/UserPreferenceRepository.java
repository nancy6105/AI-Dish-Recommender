package com.aifood.repository.preference;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aifood.entity.preference.UserPreference;

public interface UserPreferenceRepository extends JpaRepository<UserPreference,Long>{

    Optional <UserPreference> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    
}