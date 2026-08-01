package com.aifood.service.cuisine;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aifood.dto.cuisine.CreateCuisineRequest;
import com.aifood.dto.cuisine.CuisineResponse;
import com.aifood.entity.cuisine.Cuisine;
import com.aifood.repository.cuisine.CuisineRepository;

@Service
public class CuisineServiceImpl implements CuisineService {

    private final CuisineRepository cuisineRepository;

    public CuisineServiceImpl(CuisineRepository cuisineRepository) {
        this.cuisineRepository = cuisineRepository;
    }

    @Override
    public CuisineResponse createCuisine(CreateCuisineRequest request) {

        Cuisine cuisine = new Cuisine();
        cuisine.setName(request.getName());

        Cuisine savedCuisine = cuisineRepository.save(cuisine);

        CuisineResponse response = new CuisineResponse();
        response.setId(savedCuisine.getId());
        response.setName(savedCuisine.getName());

        return response;
    }

    @Override
    public List<CuisineResponse> getAllCuisines() {

        return cuisineRepository.findAll()
                .stream()
                .map(cuisine -> {
                    CuisineResponse response = new CuisineResponse();
                    response.setId(cuisine.getId());
                    response.setName(cuisine.getName());
                    return response;
                })
                .toList();
    }
}