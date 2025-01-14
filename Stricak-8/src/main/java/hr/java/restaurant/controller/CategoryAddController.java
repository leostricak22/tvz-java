package hr.java.restaurant.controller;

import hr.java.restaurant.model.Category;
import hr.java.restaurant.repository.CategoryRepository;
import hr.java.restaurant.util.AlertDialog;
import hr.java.restaurant.util.SceneLoader;
import hr.java.restaurant.util.Validation;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class CategoryAddController implements AddController {
    @FXML private TextField nameTextField;
    @FXML private TextField descriptionTextField;

    private final CategoryRepository<Category> categoryRepository = new CategoryRepository<>();

    @Override
    public void initialize() {}

    @Override
    public void add() {
        List<Category> categories = new ArrayList<>(categoryRepository.findAll());

        String categoryName = nameTextField.getText();
        String categoryDescription = descriptionTextField.getText();

        String error = "";
        if (!Validation.validNonEmptyString(categoryName))
            error = "Invalid category name.";
        else if (!Validation.validNonEmptyString(categoryDescription))
            error = "Invalid category description.";
        else if (Validation.isDuplicateByName(categories, categoryName))
            error = "Category with that name already exists.";

        if (!error.isEmpty()) {
            AlertDialog.showErrorDialog("Category add error", error);
            return;
        }

        Category newCategory = new Category.Builder()
                .id(categoryRepository.getNextId())
                .name(categoryName)
                .description(categoryDescription).build();

        categoryRepository.add(newCategory);
        AlertDialog.showInformationDialog("Category added", "Category successfully added.");

        SceneLoader.loadScene("categorySearch", "Category search");
    }
}