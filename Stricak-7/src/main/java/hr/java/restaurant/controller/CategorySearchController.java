package hr.java.restaurant.controller;

import hr.java.restaurant.model.Category;
import hr.java.restaurant.repository.CategoryRepository;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class CategorySearchController {

    private static final CategoryRepository<Category> categoryRepository = new CategoryRepository<>();

    @FXML
    private TextField categoryIdTextField;

    @FXML
    private TextField categoryNameTextField;

    @FXML
    private TextField categoryDescriptionTextField;

    @FXML
    private TableView<Category> categoryTableView;

    @FXML
    private TableColumn<Category, Long> categoryIdColumn;

    @FXML
    private TableColumn<Category, String> categoryNameColumn;

    @FXML
    private TableColumn<Category, String> categoryDescriptionColumn;

    @FXML
    private Label removeFilterLabel;

    public void initialize() {
        categoryIdColumn.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getId()).asObject());

        categoryNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        categoryDescriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));

        filterCategories();
    }

    public void filterCategories() {
        List<Category> categories = new ArrayList<>(categoryRepository.findAll());
        categories.sort((category1, category2) -> category1.getId().compareTo(category2.getId()));

        String categoryIdTextFieldValue = categoryIdTextField.getText();
        String categoryNameTextFieldValue = categoryNameTextField.getText();
        String categoryDescriptionTextFieldValue = categoryDescriptionTextField.getText();

        categories = categories.stream()
                .filter(category -> categoryIdTextFieldValue.isEmpty() || category.getId().toString().contains(categoryIdTextFieldValue))
                .filter(category -> categoryNameTextFieldValue.isEmpty() || category.getName().contains(categoryNameTextFieldValue))
                .filter(category -> categoryDescriptionTextFieldValue.isEmpty() || category.getDescription().contains(categoryDescriptionTextFieldValue))
                .toList();

        removeFilterLabel.setVisible(!categoryIdTextFieldValue.isEmpty() || !categoryNameTextFieldValue.isEmpty() || !categoryDescriptionTextFieldValue.isEmpty());

        ObservableList<Category> categoryObservableList =  FXCollections.observableList(categories);
        categoryTableView.setItems(categoryObservableList);
    }

    @FXML
    public void handleRemoveFilterLabelClick(MouseEvent event) {
        categoryIdTextField.clear();
        categoryNameTextField.clear();
        categoryDescriptionTextField.clear();
        removeFilterLabel.setVisible(false);
        filterCategories();
    }
}
