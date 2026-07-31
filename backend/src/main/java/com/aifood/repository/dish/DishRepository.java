package com.aifood.repository.dish;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aifood.entity.dish.Dish;

public interface DishRepository extends JpaRepository<Dish, Long>{

}
