package com.rezdy.lunch.controller;

import com.rezdy.lunch.dto.Recipe;
import com.rezdy.lunch.service.LunchService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class LunchController {

    private LunchService lunchService;

    @Autowired
    public LunchController(LunchService lunchService) {
        this.lunchService = lunchService;
    }

    @GetMapping("/lunch")
    public List<Recipe> getRecipes(@RequestParam(value = "date") String date) {
        return lunchService.getSortedNonExpiredRecipesOnDate(LocalDate.parse(date));
    }

    @GetMapping("/lunch/recipe")
    public Recipe getRecipe(@RequestParam(value = "title") String title) throws NotFoundException {
        return lunchService.getRecipe(title);
    }
}
