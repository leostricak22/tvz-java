package hr.java.restaurant.controller;

import hr.java.restaurant.enumeration.MealTypeEnum;
import hr.java.restaurant.model.*;
import hr.java.restaurant.repository.CategoryRepository;
import hr.java.restaurant.repository.IngredientRepository;
import hr.java.restaurant.repository.MealRepository;
import hr.java.restaurant.util.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

import static java.util.Objects.isNull;

public class MealAddController implements AddController {

    @FXML private TextField nameTextField;
    @FXML private ComboBox<MealTypeEnum> mealTypeComboBox;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private TextField priceTextField;
    @FXML private Label ingredientNameArrayLabel;
    @FXML private Label additionalAttribute1Label;
    @FXML private Label additionalAttribute2Label;
    @FXML private Label additionalAttribute3Label;
    @FXML private CheckBox additionalAttribute1CheckBox;
    @FXML private CheckBox additionalAttribute2CheckBox;
    @FXML private TextField additionalAttribute3TextField;

    private final IngredientRepository<Ingredient> ingredientRepository = new IngredientRepository<>();
    private final CategoryRepository<Category> categoryRepository = new CategoryRepository<>();
    private final MealRepository<Meal> mealRepository = new MealRepository<>();

    private Set<Ingredient> selectedIngredients = new HashSet<>();

    private static final Logger logger = LoggerFactory.getLogger(MealAddController.class);

    @Override
    public void initialize() {
        ComboBoxUtil.comboBoxMealTypeConverter(mealTypeComboBox);
        ComboBoxUtil.comboBoxStringConverter(categoryComboBox);

        mealTypeComboBox.setItems(
                FXCollections.observableArrayList(MealTypeEnum.values()));
        categoryComboBox.setItems(
                FXCollections.observableArrayList(new ArrayList<>(categoryRepository.findAll())));
    }

    @Override
    public void add() {
        String name = nameTextField.getText();
        MealTypeEnum mealType = mealTypeComboBox.getValue();
        Category category = categoryComboBox.getValue();
        String price = priceTextField.getText();
        boolean additionalAttribute1 = additionalAttribute1CheckBox.isSelected();
        boolean additionalAttribute2 = additionalAttribute2CheckBox.isSelected();
        String additionalAttribute3 = additionalAttribute3TextField.getText();

        String error = validateInput(name, mealType, category, price, selectedIngredients, additionalAttribute3);
        if (!error.isEmpty()) {
            AlertDialog.showErrorDialog("Meal add error", error);
            logger.error("Meal add error: {}", error);
            return;
        }

        BigDecimal priceBigDecimal = new BigDecimal(price);
        Meal newMeal = MealInputUtil.createMeal(
                mealType,
                name,
                category,
                priceBigDecimal,
                selectedIngredients,
                additionalAttribute1,
                additionalAttribute2,
                additionalAttribute3);

        mealRepository.add(newMeal);
        logger.info("Meal added: {}", newMeal.getName());
        AlertDialog.showInformationDialog("Meal add", "Meal added successfully.");

        SceneLoader.loadScene("mealSearch", "Meal search");
    }

    public void handleMultipleCheckBoxSelectWindow() {
        Set<Ingredient> selectedIngredientsFromMultipleCheckBoxWindow =
                MultipleCheckBoxSelectUtil.openWindow(ingredientRepository.findAll(), selectedIngredients);

        if (selectedIngredientsFromMultipleCheckBoxWindow.isEmpty())
            return;

        selectedIngredients = selectedIngredientsFromMultipleCheckBoxWindow;
        ingredientNameArrayLabel.setText(selectedIngredients.stream()
                .map(Ingredient::getName)
                .reduce("", (a, b) -> a + ", " + b)
                .substring(2));
    }

    private String validateInput(String name,
                                 MealTypeEnum mealType,
                                 Category category,
                                 String price,
                                 Set<Ingredient> ingredients,
                                 String additionalAttribute3) {
        if (name.isEmpty())
            return "Meal name is empty.";
        else if (Validation.isDuplicateByName(mealRepository.findAll(), name))
            return "Meal name already exists.";
        else if (isNull(mealType))
            return "Meal type is not selected.";
        else if (isNull(category))
            return "Category is not selected.";
        else if (!Validation.validBigDecimal(price))
            return "Invalid price format.";
        else if ((new BigDecimal(price)).compareTo(BigDecimal.ZERO) < 0)
            return "Invalid price.";
        else if (ingredients.isEmpty())
            return "Ingredients are not selected.";
        else if (additionalAttribute3.isEmpty())
            return "Additional attribute 3 is empty.";

        return "";
    }

    public void handleMealTypeChange() {
        if (isNull(mealTypeComboBox.getValue()))
            return;

        additionalAttribute1CheckBox.setVisible(true);
        additionalAttribute2CheckBox.setVisible(true);
        additionalAttribute3TextField.setVisible(true);

        if (mealTypeComboBox.getValue().equals(MealTypeEnum.VEGAN)) {
            additionalAttribute1Label.setText("Is gluten free:");
            additionalAttribute2Label.setText("Is organic:");
            additionalAttribute3Label.setText("Protein source:");
        } else if (mealTypeComboBox.getValue().equals(MealTypeEnum.MEAT)) {
            additionalAttribute1Label.setText("Is organic:");
            additionalAttribute2Label.setText("Is eco friendly:");
            additionalAttribute3Label.setText("Meat type:");
        } else if (mealTypeComboBox.getValue().equals(MealTypeEnum.VEGETARIAN)) {
            additionalAttribute1Label.setText("Contains dairy:");
            additionalAttribute2Label.setText("Contains eggs:");
            additionalAttribute3Label.setText("Protein source:");
        }
    }
}