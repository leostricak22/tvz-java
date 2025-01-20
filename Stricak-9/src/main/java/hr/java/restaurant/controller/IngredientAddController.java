package hr.java.restaurant.controller;

import hr.java.restaurant.model.Category;
import hr.java.restaurant.model.Ingredient;
import hr.java.restaurant.repository.CategoryDatabaseRepository;
import hr.java.restaurant.repository.IngredientDatabaseRepository;
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

import static java.util.Objects.isNull;

public class IngredientAddController implements AddController {

    @FXML private TextField nameTextField;
    @FXML private TextField kCalTextField;
    @FXML private TextField preparationMethodTextField;
    @FXML private ComboBox<Category> categoryComboBox;

    private final IngredientDatabaseRepository ingredientRepository = new IngredientDatabaseRepository();
    private final CategoryDatabaseRepository categoryRepository = new CategoryDatabaseRepository();
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

        String error = validateInput(
                ingredientName,
                kCalString,
                preparationMethod,
                category.orElse(null),
                ingredients);

        if (!error.isEmpty()) {
            AlertDialog.showErrorDialog("Ingredient save error", error);
            logger.error("Ingredient save error: {}", error);
            return;
        }

        BigDecimal kcal = new BigDecimal(kCalString);
        Ingredient newIngredient = new Ingredient.Builder(ingredientRepository.findNextId(), ingredientName)
                .setKcal(kcal)
                .setPreparationMethod(preparationMethod)
                .setCategory(category.orElseThrow(() -> new IllegalArgumentException("Category is null.")))
                .build();

        ingredientRepository.save(newIngredient);
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
        if (isNull(category))
            return "Invalid category.";
        if (Validation.isDuplicateByName(ingredients, ingredientName))
            return "Ingredient with that name already exists.";

        return "";
    }
}
