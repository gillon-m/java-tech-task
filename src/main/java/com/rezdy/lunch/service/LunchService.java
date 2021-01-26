package com.rezdy.lunch.service;

import com.rezdy.lunch.dto.Recipe;
import javassist.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface LunchService {
    List<Recipe> getRecipes(LocalDate date);

    List<Recipe> getRecipes(Set<String> ingredients, boolean include);

    Recipe getRecipe(String title) throws NotFoundException;
}
