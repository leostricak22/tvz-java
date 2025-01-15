package hr.java.restaurant.controller;

import hr.java.restaurant.main.RestaurantApplication;
import hr.java.restaurant.model.Ingredient;
import hr.java.restaurant.repository.IngredientRepository;
import hr.java.restaurant.util.AlertDialog;
import hr.java.restaurant.util.FXMLLoaderHelper;
import hr.java.restaurant.util.MultipleCheckBoxSelectUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MealAddController implements AddController {

    private final IngredientRepository<Ingredient> ingredientRepository = new IngredientRepository<>();
    private List<Ingredient> selectedIngredients = new ArrayList<>();

    @Override
    public void initialize() {
    }

    @Override
    public void add() {
        for (Ingredient ingredient : selectedIngredients) {
            System.out.println(ingredient.getName());
        }
    }

    public void openMultipleCheckBoxSelectWindow() {
        selectedIngredients = MultipleCheckBoxSelectUtil.openWindow(ingredientRepository.findAll());
    }
}