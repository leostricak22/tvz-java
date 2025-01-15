package hr.java.restaurant.controller;

import hr.java.restaurant.model.Category;
import hr.java.restaurant.repository.CategoryRepository;
import hr.java.restaurant.util.AlertDialog;
import hr.java.restaurant.util.SceneLoader;
import hr.java.restaurant.util.Validation;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.HashSet;
import java.util.Set;

public class CategoryAddController implements AddController {
    @FXML private TextField nameTextField;
    @FXML private TextField descriptionTextField;

    private final CategoryRepository<Category> categoryRepository = new CategoryRepository<>();

    @Override
    public void initialize() {}

    @Override
    public void add() {
        Set<Category> categories = new HashSet<>(categoryRepository.findAll());

        String categoryName = nameTextField.getText();
        String categoryDescription = descriptionTextField.getText();

        String error = validateInput(categoryName, categoryDescription, categories);
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

    private String validateInput(String categoryName, String categoryDescription, Set<Category> categories) {
        if (!Validation.validString(categoryName))
            return "Invalid category name.";
        else if (!Validation.validString(categoryDescription))
            return "Invalid category description.";
        else if (Validation.isDuplicateByName(categories, categoryName))
            return "Category with that name already exists.";

        return "";
    }
}