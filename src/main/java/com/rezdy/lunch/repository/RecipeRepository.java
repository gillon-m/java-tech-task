package com.rezdy.lunch.repository;

import com.rezdy.lunch.dto.Recipe;

import java.time.LocalDate;
import java.util.List;

public interface RecipeRepository {
    List<Recipe> loadRecipes(LocalDate date);
}
