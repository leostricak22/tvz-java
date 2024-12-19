package hr.java.restaurant.controller;

import hr.java.restaurant.enumeration.MealTypeEnum;
import hr.java.restaurant.model.Category;
import hr.java.restaurant.model.Ingredient;
import hr.java.restaurant.model.Meal;
import hr.java.restaurant.repository.CategoryRepository;
import hr.java.restaurant.repository.IngredientRepository;
import hr.java.restaurant.repository.MealRepository;
import hr.java.restaurant.util.ComboBoxUtil;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MealSearchController implements SearchController {
    private static final MealRepository<Meal> mealRepository = new MealRepository<>();
    private static final CategoryRepository<Category> categoryRepository = new CategoryRepository<>();
    private static final IngredientRepository<Ingredient> ingredientRepository = new IngredientRepository<>();

    @FXML
    TextField mealIdTextField;

    @FXML
    TextField mealNameTextField;

    @FXML
    ComboBox<MealTypeEnum> mealTypeComboBox;

    @FXML
    ComboBox<Category> mealCategoryComboBox;

    @FXML
    ComboBox<Ingredient> mealIngredientComboBox;

    @FXML
    TextField mealPriceFromTextField;

    @FXML
    TextField mealPriceToTextField;

    @FXML
    TableView<Meal> mealTableView;

    @FXML
    TableColumn<Meal, Long> mealIdColumn;

    @FXML
    TableColumn<Meal, String> mealNameColumn;

    @FXML
    TableColumn<Meal, String> mealTypeColumn;

    @FXML
    TableColumn<Meal, String> mealCategoryNameColumn;

    @FXML
    TableColumn<Meal, String> mealIngredientsColumn;

    @FXML
    TableColumn<Meal, String> mealPriceColumn;

    @FXML
    Label removeFilterLabel;


    public void initialize() {
        ComboBoxUtil.comboBoxMealTypeConverter(mealTypeComboBox);
        ComboBoxUtil.comboBoxStringConverter(mealCategoryComboBox);
        ComboBoxUtil.comboBoxStringConverter(mealIngredientComboBox);

        mealIdColumn.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getId()).asObject());

        mealNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        mealTypeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMealType()));

        mealCategoryNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategory().getName()));

        mealIngredientsColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(String.join(", ",
                    cellData.getValue().getIngredients().stream().map(Ingredient::getName).toList()))
        );

        mealPriceColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPrice().toString()));

        mealTypeComboBox.setItems(FXCollections.observableArrayList(MealTypeEnum.values()));
        mealCategoryComboBox.setItems(
                FXCollections.observableArrayList(new ArrayList<>(categoryRepository.findAll())));
        mealIngredientComboBox.setItems(
                FXCollections.observableArrayList(new ArrayList<>(ingredientRepository.findAll())));
        filter();
    }

    public void filter() {
        List<Meal> meals = new ArrayList<>(mealRepository.findAll());
        meals.sort((m1, m2) -> m1.getId().compareTo(m2.getId()));

        String mealIdTextFieldValue = mealIdTextField.getText();
        String mealNameTextFieldValue = mealNameTextField.getText();
        String mealPriceToTextFieldValue = mealPriceToTextField.getText();
        String mealPriceFromTextFieldValue = mealPriceFromTextField.getText();

        String mealTypeComboBoxValue = ComboBoxUtil.getComboBoxValue(mealTypeComboBox)
                .map(MealTypeEnum::getName).orElse("");
        String mealCategoryComboBoxValue = ComboBoxUtil.getComboBoxValue(mealCategoryComboBox)
                .map(Category::getName).orElse("");
        String mealIngredientComboBoxValue = ComboBoxUtil.getComboBoxValue(mealIngredientComboBox)
                .map(Ingredient::getName).orElse("");

        meals = meals.stream()
                .filter(meal -> mealIdTextFieldValue.isBlank() ||
                        meal.getId().toString().contains(mealIdTextFieldValue))
                .filter(meal -> mealNameTextFieldValue.isBlank() ||
                        meal.getName().contains(mealNameTextFieldValue))
                .filter(meal -> mealPriceToTextFieldValue.isBlank() ||
                        meal.getPrice().compareTo(new BigDecimal(mealPriceToTextFieldValue)) <= 0)
                .filter(meal -> mealPriceFromTextFieldValue.isBlank() ||
                        meal.getPrice().compareTo(new BigDecimal(mealPriceFromTextFieldValue)) >= 0)
                .filter(meal -> mealTypeComboBoxValue.isBlank() ||
                        meal.getMealType().contains(mealTypeComboBoxValue))
                .filter(meal -> mealCategoryComboBoxValue.isBlank() ||
                        meal.getCategory().getName().contains(mealCategoryComboBoxValue))
                .filter(meal -> mealIngredientComboBoxValue.isBlank() ||
                        meal.getIngredients().stream()
                                .map(Ingredient::getName)
                                .toList()
                                .contains(mealIngredientComboBoxValue))
                .toList();

        removeFilterLabel.setVisible(
                !mealIdTextFieldValue.isBlank() ||
                !mealNameTextFieldValue.isBlank() ||
                !mealPriceToTextFieldValue.isBlank() ||
                !mealPriceFromTextFieldValue.isBlank() ||
                !mealTypeComboBoxValue.isBlank() ||
                !mealCategoryComboBoxValue.isBlank() ||
                !mealIngredientComboBoxValue.isBlank());

        mealTableView.setItems(FXCollections.observableArrayList(meals));
    }

    public void removeFilter() {
        mealIdTextField.clear();
        mealNameTextField.clear();
        mealPriceToTextField.clear();
        mealPriceFromTextField.clear();
        mealTypeComboBox.getSelectionModel().clearSelection();
        mealCategoryComboBox.getSelectionModel().clearSelection();
        mealIngredientComboBox.getSelectionModel().clearSelection();
        filter();
    }
}
