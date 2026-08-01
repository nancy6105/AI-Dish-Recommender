package com.aifood.service.cuisine;

import java.util.List;

import com.aifood.dto.cuisine.CreateCuisineRequest;
import com.aifood.dto.cuisine.CuisineResponse;

public interface CuisineService {

    CuisineResponse createCuisine(CreateCuisineRequest request);

    List<CuisineResponse> getAllCuisines();

}