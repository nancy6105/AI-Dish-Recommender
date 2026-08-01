package com.aifood.repository.cuisine;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aifood.entity.cuisine.Cuisine;

public interface CuisineRepository
        extends JpaRepository<Cuisine, Long> {

    Optional<Cuisine> findByName(String name);

}