package com.rezdy.lunch.controller;

import com.rezdy.lunch.dto.Recipe;
import com.rezdy.lunch.service.LunchService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("lunch")
public class LunchController {

    private LunchService lunchService;

    @Autowired
    public LunchController(LunchService lunchService) {
        this.lunchService = lunchService;
    }

    @GetMapping("recipes")
    public List<Recipe> getRecipes(@RequestParam(value = "date") String date) {
        return lunchService.getRecipes(LocalDate.parse(date));
    }

    @GetMapping("recipe")
    public Recipe getRecipe(@RequestParam(value = "title") String title) throws NotFoundException {
        return lunchService.getRecipe(title);
    }

    @PostMapping("recipes")
    public List<Recipe> getRecipesIncludeAndExcluding(@RequestBody Set<String> ingredients, @RequestParam(value = "include") boolean include) {
        return lunchService.getRecipes(ingredients, include);
    }
}
