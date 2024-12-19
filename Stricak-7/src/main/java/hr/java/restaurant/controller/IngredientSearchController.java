package hr.java.restaurant.controller;

import hr.java.restaurant.model.Category;
import hr.java.restaurant.model.Ingredient;
import hr.java.restaurant.repository.CategoryRepository;
import hr.java.restaurant.repository.IngredientRepository;
import hr.java.restaurant.util.ComboBoxUtil;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class IngredientSearchController {
    private static final CategoryRepository<Category> categoryRepository = new CategoryRepository<>();
    private static final IngredientRepository<Ingredient> ingredientRepository = new IngredientRepository<>();

    @FXML
    private TextField ingredientIdTextField;

    @FXML
    private TextField ingredientNameTextField;

    @FXML
    private ComboBox<Category> ingredientCategoryComboBox;

    @FXML
    private TextField ingredientKcalTextField;

    @FXML
    private TextField ingredientPreparationMethodTextField;

    @FXML
    private TableView<Ingredient> ingredientTableView;

    @FXML
    private TableColumn<Ingredient, Long> ingredientIdColumn;

    @FXML
    private TableColumn<Ingredient, String> ingredientNameColumn;

    @FXML
    private TableColumn<Ingredient, String> ingredientCategoryNameColumn;

    @FXML
    private TableColumn<Ingredient, String> ingredientKcalColumn;

    @FXML
    private TableColumn<Ingredient, String> ingredientPreparationMethodColumn;

    @FXML
    private Label removeFilterLabel;

    public void initialize() {
        ComboBoxUtil.comboBoxStringConverter(ingredientCategoryComboBox);

        ingredientIdColumn.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getId()).asObject());

        ingredientNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        ingredientCategoryNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategory().getName()));

        ingredientKcalColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getKcal().toString()));

        ingredientPreparationMethodColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPreparationMethod()));

        ingredientCategoryComboBox.setItems(FXCollections.observableArrayList(categoryRepository.findAll()));
        filterIngredients();
    }

    public void filterIngredients() {
        List<Ingredient> ingredients = new ArrayList<>(ingredientRepository.findAll());
        ingredients.sort((i1, i2) -> i1.getId().compareTo(i2.getId()));

        String ingredientIdTextFieldValue = ingredientIdTextField.getText();
        String ingredientNameTextFieldValue = ingredientNameTextField.getText();

        String ingredientCategoryComboBoxValue;
        if(ingredientCategoryComboBox.getValue() != null)
            ingredientCategoryComboBoxValue = ingredientCategoryComboBox.getValue().getName();
        else {
            ingredientCategoryComboBoxValue = null;
        }

        String ingredientKcalTextFieldValue = ingredientKcalTextField.getText();
        String ingredientPreparationMethodTextFieldValue = ingredientPreparationMethodTextField.getText();

        ingredients = ingredients.stream()
                .filter(ingredient -> ingredientIdTextFieldValue.isEmpty() || ingredient.getId().toString().contains(ingredientIdTextFieldValue))
                .filter(ingredient -> ingredientNameTextFieldValue.isEmpty() || ingredient.getName().contains(ingredientNameTextFieldValue))
                .filter(ingredient -> ingredientCategoryComboBoxValue == null || ingredient.getCategory().getName().contains(ingredientCategoryComboBoxValue))
                .filter(ingredient -> ingredientKcalTextFieldValue.isEmpty() || ingredient.getKcal().toString().contains(ingredientKcalTextFieldValue))
                .filter(ingredient -> ingredientPreparationMethodTextFieldValue.isEmpty() || ingredient.getPreparationMethod().contains(ingredientPreparationMethodTextFieldValue))
                .toList();

        removeFilterLabel.setVisible(
                !ingredientIdTextFieldValue.isEmpty() ||
                !ingredientNameTextFieldValue.isEmpty() ||
                ingredientCategoryComboBoxValue != null ||
                !ingredientKcalTextFieldValue.isEmpty() ||
                !ingredientPreparationMethodTextFieldValue.isEmpty());

        ObservableList<Ingredient> categoryObservableList =  FXCollections.observableList(ingredients);
        ingredientTableView.setItems(categoryObservableList);
    }

    @FXML
    public void handleRemoveFilterLabelClick(MouseEvent event) {
        ingredientIdTextField.clear();
        ingredientNameTextField.clear();
        ingredientCategoryComboBox.setValue(null);
        ingredientKcalTextField.clear();
        ingredientPreparationMethodTextField.clear();

        removeFilterLabel.setVisible(false);
        filterIngredients();
    }
}
