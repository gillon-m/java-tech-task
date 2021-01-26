package com.rezdy.lunch.service;

import com.rezdy.lunch.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        recipesSorted = recipes; //TODO sort recipes considering best-before
    }
}
