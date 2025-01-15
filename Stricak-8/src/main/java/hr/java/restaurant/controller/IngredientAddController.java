package hr.java.restaurant.controller;

import hr.java.restaurant.model.Category;
import hr.java.restaurant.model.Ingredient;
import hr.java.restaurant.model.Meal;
import hr.java.restaurant.repository.CategoryRepository;
import hr.java.restaurant.repository.IngredientRepository;
import hr.java.restaurant.util.AlertDialog;
import hr.java.restaurant.util.ComboBoxUtil;
import hr.java.restaurant.util.SceneLoader;
import hr.java.restaurant.util.Validation;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

public class IngredientAddController implements AddController {

    @FXML private TextField nameTextField;
    @FXML private TextField kCalTextField;
    @FXML private TextField preparationMethodTextField;
    @FXML private ComboBox<Category> categoryComboBox;

    private final IngredientRepository<Ingredient> ingredientRepository = new IngredientRepository<>();
    private final CategoryRepository<Category> categoryRepository = new CategoryRepository<>();
    private static final Logger logger = LoggerFactory.getLogger(IngredientAddController.class);

    @Override
    public void initialize() {
        ComboBoxUtil.comboBoxStringConverter(categoryComboBox);
        categoryComboBox.getItems().setAll(categoryRepository.findAll());
    }

    @Override
    public void add() {
        Set<Ingredient> ingredients = ingredientRepository.findAll();

        String ingredientName = nameTextField.getText();
        String kCalString = kCalTextField.getText();
        String preparationMethod = preparationMethodTextField.getText();
        Optional<Category> category = Optional.ofNullable(categoryComboBox.getValue());

        String error = validateInput(ingredientName, kCalString, preparationMethod, category.orElse(null), ingredients);

        if (!error.isEmpty()) {
            AlertDialog.showErrorDialog("Ingredient add error", error);
            logger.error("Ingredient add error: {}", error);
            return;
        }

        BigDecimal kcal = new BigDecimal(kCalString);
        Ingredient newIngredient = new Ingredient.Builder(ingredientRepository.getNextId(), ingredientName)
                .setKcal(kcal)
                .setPreparationMethod(preparationMethod)
                .setCategory(category.orElse(null))
                .build();

        ingredientRepository.add(newIngredient);
        AlertDialog.showInformationDialog("Ingredient added", "Ingredient successfully added.");
        logger.info("Ingredient \"{}\" added.", newIngredient.getName());

        SceneLoader.loadScene("ingredientSearch", "Ingredient search");
    }

    private String validateInput(String ingredientName,
                                String kCalString,
                                String preparationMethod,
                                Category category,
                                Set<Ingredient> ingredients) {
        if (!Validation.validString(ingredientName))
            return "Invalid ingredient name.";
        if (!Validation.validBigDecimal(kCalString))
            return "Invalid kCal.";
        if ((new BigDecimal(kCalString)).compareTo(BigDecimal.ZERO) < 0)
            return "kCal cannot be negative.";
        if (!Validation.validString(preparationMethod))
            return "Invalid preparation method.";
        if (category == null)
            return "Invalid category.";
        if (Validation.isDuplicateByName(ingredients, ingredientName))
            return "Ingredient with that name already exists.";

        return "";
    }
}
