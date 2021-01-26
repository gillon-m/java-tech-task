package com.rezdy.lunch.service;

import com.rezdy.lunch.dto.Ingredient;
import com.rezdy.lunch.dto.Recipe;
import com.rezdy.lunch.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class LunchService {

    @Autowired
    private RecipeRepository recipeRepository;

    private List<Recipe> recipesSorted;

    public List<Recipe> getNonExpiredRecipesOnDate(LocalDate date) {
        List<Recipe> recipes = recipeRepository.loadRecipes(date);

        sortRecipes(recipes);

        return recipesSorted;
    }

    private void sortRecipes(List<Recipe> recipes) {
        recipes.sort((r1, r2) -> {
            LocalDate r1BestBefore = getLowestBestBefore(r1.getIngredients());
            LocalDate r2BestBefore = getLowestBestBefore(r2.getIngredients());
            return r1BestBefore.compareTo(r2BestBefore);
        });
        recipesSorted = recipes;
    }

    private LocalDate getLowestBestBefore(Set<Ingredient> ingredients) {
        return ingredients
                .stream()
                .map(Ingredient::getBestBefore)
                .filter(Objects::nonNull)
                .min(LocalDate::compareTo)
                .orElse(LocalDate.MAX);
    }
}
