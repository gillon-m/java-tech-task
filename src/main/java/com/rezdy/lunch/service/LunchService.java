package com.rezdy.lunch.service;

import com.rezdy.lunch.dto.Ingredient;
import com.rezdy.lunch.dto.Recipe;
import com.rezdy.lunch.repository.RecipeRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LunchService {

    @Autowired
    private RecipeRepository recipeRepository;

    private List<Recipe> recipesSorted;

    public List<Recipe> getSortedNonExpiredRecipesOnDate(LocalDate date) {
        List<Recipe> recipes = getNonExpiredRecipesOnDate(date);
        sortRecipes(recipes);
        return recipesSorted;
    }

    public Recipe getRecipe(String title) throws NotFoundException {
        return recipeRepository.findById(title)
                .orElseThrow(() -> new NotFoundException("Cannot find recipe for '" + title +"'"));
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

    private List<Recipe> getNonExpiredRecipesOnDate(LocalDate date) {
        return recipeRepository.findAll().stream()
                .filter(recipe -> {
                    Set<Ingredient> ingredients = recipe.getIngredients();
                    for (Ingredient i : ingredients) {
                        if (i.getUseBy() != null && i.getUseBy().compareTo(date) < 0) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }
}
