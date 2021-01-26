package com.rezdy.lunch.service;

import com.rezdy.lunch.dto.Ingredient;
import com.rezdy.lunch.dto.Recipe;
import com.rezdy.lunch.repository.RecipeRepository;
import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LunchServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private LunchServiceImpl lunchService;

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
        List<Recipe> nonExpiredRecipes = lunchService.getRecipes(LocalDate.parse("2021-01-01"));
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
        List<Recipe> nonExpiredRecipes = lunchService.getRecipes(LocalDate.parse("2021-01-01"));
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
    public void testGetNonExpiredRecipesWithUseByOnCurrentDate() {
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
        List<Recipe> nonExpiredRecipes = lunchService.getRecipes(LocalDate.parse("2021-01-01"));
        assertEquals("Spam Salad", nonExpiredRecipes.get(0).getTitle());
        assertEquals("Almost Expired Parma", nonExpiredRecipes.get(1).getTitle());
        assertEquals("Roast Beef", nonExpiredRecipes.get(2).getTitle());
        assertEquals("Beans and Rice", nonExpiredRecipes.get(3).getTitle());
        assertFalse(nonExpiredRecipes.stream()
                .map(Recipe::getTitle)
                .collect(Collectors.toList())
                .contains("Expired Milk"));
    }

    @Test
    public void testGetRecipe() throws NotFoundException {
        final Recipe EXPECTED_RECIPE = new Recipe().setTitle("Dumplings");
        when(recipeRepository.findById(any())).thenReturn(Optional.of(EXPECTED_RECIPE));
        Recipe recipe = lunchService.getRecipe("Dumplings");
        assertEquals("Dumplings", recipe.getTitle());
    }

    @Test
    public void testGetRecipeNotFound() {
        try {
            when(recipeRepository.findById(any())).thenReturn(Optional.ofNullable(null));
            Recipe recipe = lunchService.getRecipe("Dumplings");
            fail("NotFoundException not thrown");
        } catch (NotFoundException e) {
            assertEquals("Cannot find recipe for 'Dumplings'", e.getMessage());
        }

    }

    @Test
    public void testGetRecipesIncludingIngredients() {
        List<Recipe> recipeList = Arrays.asList(
                new Recipe().setTitle("Roast Beef")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Beef"),
                                new Ingredient().setTitle("Cheese")
                        )),
                new Recipe().setTitle("Parma")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Chicken"),
                                new Ingredient().setTitle("Cheese")
                        )),
                new Recipe().setTitle("Beans and Rice")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Beans"),
                                new Ingredient().setTitle("Rice")
                        )),
                new Recipe().setTitle("Expired Milk")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Milk"),
                                new Ingredient().setTitle("Sugar")
                        ))
        );
        when(recipeRepository.findAll()).thenReturn(recipeList);
        List<Recipe> foundRecipes = lunchService.getRecipes(Set.of("Beans", "Cheese"), true);
        List<String> recipeNames = foundRecipes.stream().map(Recipe::getTitle).collect(Collectors.toList());
        assertTrue(recipeNames.contains("Roast Beef"));
        assertTrue(recipeNames.contains("Parma"));
        assertTrue(recipeNames.contains("Beans and Rice"));
        assertFalse(recipeNames.contains("Expired Milk"));
    }

    @Test
    public void testGetRecipesExcludingIngredients() {
        List<Recipe> recipeList = Arrays.asList(
                new Recipe().setTitle("Roast Beef")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Beef"),
                                new Ingredient().setTitle("Cheese")
                        )),
                new Recipe().setTitle("Parma")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Chicken"),
                                new Ingredient().setTitle("Cheese")
                        )),
                new Recipe().setTitle("Beans and Rice")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Beans"),
                                new Ingredient().setTitle("Rice")
                        )),
                new Recipe().setTitle("Expired Milk")
                        .setIngredients(Set.of(
                                new Ingredient().setTitle("Milk"),
                                new Ingredient().setTitle("Sugar")
                        ))
        );
        when(recipeRepository.findAll()).thenReturn(recipeList);
        List<Recipe> foundRecipes = lunchService.getRecipes(Set.of("Beans", "Cheese"), false);
        List<String> recipeNames = foundRecipes.stream().map(Recipe::getTitle).collect(Collectors.toList());
        assertFalse(recipeNames.contains("Roast Beef"));
        assertFalse(recipeNames.contains("Parma"));
        assertFalse(recipeNames.contains("Beans and Rice"));
        assertTrue(recipeNames.contains("Expired Milk"));
    }
}
