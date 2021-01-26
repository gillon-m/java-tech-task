package com.rezdy.lunch.service;

import com.rezdy.lunch.dto.Ingredient;
import com.rezdy.lunch.dto.Recipe;
import com.rezdy.lunch.repository.RecipeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LunchServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private LunchService lunchService;

    @Test
    public void testGetNonExpiredRecipesOnDate() {
        List<Recipe> recipeList = Arrays.asList(
                new Recipe().setTitle("Roast Beef")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Beef").setBestBefore(LocalDate.parse("2020-05-10")).setUseBy(LocalDate.parse("2022-01-01")),
                                new Ingredient().setTitle("Gravy").setBestBefore(LocalDate.parse("2020-05-11")).setUseBy(LocalDate.parse("2022-01-01"))
                        )),
                new Recipe().setTitle("Parma")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Chicken").setBestBefore(LocalDate.parse("2020-05-13")).setUseBy(LocalDate.parse("2022-01-01")),
                                new Ingredient().setTitle("Cheese").setBestBefore(LocalDate.parse("2020-05-09")).setUseBy(LocalDate.parse("2022-01-01"))
                        )),
                new Recipe().setTitle("Expired Milk")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Milk").setBestBefore(LocalDate.parse("2020-05-13")).setUseBy(LocalDate.parse("2020-01-01")),
                                new Ingredient().setTitle("Sugar").setBestBefore(LocalDate.parse("2020-05-13")).setUseBy(LocalDate.parse("2022-01-01"))
                        ))
        );
        when(recipeRepository.findAll()).thenReturn(recipeList);
        List<Recipe> nonExpiredRecipes = lunchService.getSortedNonExpiredRecipesOnDate(LocalDate.parse("2021-01-01"));
        assertEquals("Parma", nonExpiredRecipes.get(0).getTitle());
        assertEquals("Roast Beef", nonExpiredRecipes.get(1).getTitle());
        assertFalse(nonExpiredRecipes.stream()
                .map(Recipe::getTitle)
                .collect(Collectors.toList())
                .contains("Expired Milk"));
    }

    @Test
    public void testGetNonExpiredRecipesOnDateWithNullBestBefore() {
        List<Recipe> recipeList = Arrays.asList(
                new Recipe().setTitle("Roast Beef")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Beef").setBestBefore(LocalDate.parse("2020-05-10")).setUseBy(LocalDate.parse("2022-01-01")),
                                new Ingredient().setTitle("Gravy").setBestBefore(LocalDate.parse("2020-05-11")).setUseBy(LocalDate.parse("2022-01-01"))
                        )),
                new Recipe().setTitle("Parma")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Chicken").setBestBefore(LocalDate.parse("2020-05-13")).setUseBy(LocalDate.parse("2022-01-01")),
                                new Ingredient().setTitle("Cheese").setBestBefore(LocalDate.parse("2020-05-09")).setUseBy(LocalDate.parse("2022-01-01"))
                        )),
                new Recipe().setTitle("Beans and Rice")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Beans").setBestBefore(null).setUseBy(LocalDate.parse("2022-01-01")),
                                new Ingredient().setTitle("Rice").setBestBefore(LocalDate.parse("2020-05-20")).setUseBy(LocalDate.parse("2022-01-01"))
                        )),
                new Recipe().setTitle("Spam Salad")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Spam").setBestBefore(null).setUseBy(LocalDate.parse("2022-01-01")),
                                new Ingredient().setTitle("Lettuce").setBestBefore(LocalDate.parse("2020-05-02")).setUseBy(LocalDate.parse("2022-01-01"))
                        )),
                new Recipe().setTitle("Expired Milk")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Milk").setBestBefore(LocalDate.parse("2020-05-13")).setUseBy(LocalDate.parse("2020-01-01")),
                                new Ingredient().setTitle("Sugar").setBestBefore(LocalDate.parse("2020-05-13")).setUseBy(LocalDate.parse("2022-01-01"))
                        ))
        );
        when(recipeRepository.findAll()).thenReturn(recipeList);
        List<Recipe> nonExpiredRecipes = lunchService.getSortedNonExpiredRecipesOnDate(LocalDate.parse("2021-01-01"));
        assertEquals("Spam Salad", nonExpiredRecipes.get(0).getTitle());
        assertEquals("Parma", nonExpiredRecipes.get(1).getTitle());
        assertEquals("Roast Beef", nonExpiredRecipes.get(2).getTitle());
        assertEquals("Beans and Rice", nonExpiredRecipes.get(3).getTitle());
        assertFalse(nonExpiredRecipes.stream()
                .map(Recipe::getTitle)
                .collect(Collectors.toList())
                .contains("Expired Milk"));
    }


    @Test
    public void testGetNonExpiredRecipesWithUseByOnCurrentDate(){
        List<Recipe> recipeList = Arrays.asList(
                new Recipe().setTitle("Roast Beef")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Beef").setBestBefore(LocalDate.parse("2020-05-10")).setUseBy(LocalDate.parse("2022-01-01")),
                                new Ingredient().setTitle("Gravy").setBestBefore(LocalDate.parse("2020-05-11")).setUseBy(null)
                        )),
                new Recipe().setTitle("Almost Expired Parma")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Chicken").setBestBefore(LocalDate.parse("2020-05-13")).setUseBy(LocalDate.parse("2022-01-01")),
                                new Ingredient().setTitle("Cheese").setBestBefore(LocalDate.parse("2020-05-09")).setUseBy(LocalDate.parse("2021-01-01"))
                        )),
                new Recipe().setTitle("Beans and Rice")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Beans").setBestBefore(null).setUseBy(null),
                                new Ingredient().setTitle("Rice").setBestBefore(LocalDate.parse("2020-05-20")).setUseBy(null)
                        )),
                new Recipe().setTitle("Spam Salad")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Spam").setBestBefore(null).setUseBy(LocalDate.parse("2022-01-01")),
                                new Ingredient().setTitle("Lettuce").setBestBefore(LocalDate.parse("2020-05-02")).setUseBy(LocalDate.parse("2022-01-01"))
                        )),
                new Recipe().setTitle("Expired Milk")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Milk").setBestBefore(LocalDate.parse("2020-05-13")).setUseBy(LocalDate.parse("2020-01-01")),
                                new Ingredient().setTitle("Sugar").setBestBefore(LocalDate.parse("2020-05-13")).setUseBy(LocalDate.parse("2022-01-01"))
                        ))
        );
        when(recipeRepository.findAll()).thenReturn(recipeList);
        List<Recipe> nonExpiredRecipes = lunchService.getSortedNonExpiredRecipesOnDate(LocalDate.parse("2021-01-01"));
        assertEquals("Spam Salad", nonExpiredRecipes.get(0).getTitle());
        assertEquals("Almost Expired Parma", nonExpiredRecipes.get(1).getTitle());
        assertEquals("Roast Beef", nonExpiredRecipes.get(2).getTitle());
        assertEquals("Beans and Rice", nonExpiredRecipes.get(3).getTitle());
        assertFalse(nonExpiredRecipes.stream()
                .map(Recipe::getTitle)
                .collect(Collectors.toList())
                .contains("Expired Milk"));
    }
}
