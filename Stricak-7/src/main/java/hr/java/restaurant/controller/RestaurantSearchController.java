package hr.java.restaurant.controller;

import hr.java.restaurant.model.Entity;
import hr.java.restaurant.model.Meal;
import hr.java.restaurant.model.Restaurant;
import hr.java.restaurant.repository.RestaurantRepository;
import hr.java.restaurant.util.ComboBoxUtil;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantSearchController implements SearchController {

    @FXML
    TextField restaurantIdTextField;

    @FXML
    TextField restaurantNameTextField;

    @FXML
    TextField restaurantAddressStreetTextField;

    @FXML
    TextField restaurantAddressHouseNumberTextField;

    @FXML
    TextField restaurantAddressCityTextField;

    @FXML
    TextField restaurantAddressPostalCodeTextField;

    @FXML
    ComboBox<Meal> restaurantMealComboBox;

    @FXML
    ComboBox<Meal> restaurantChefComboBox;

    @FXML
    ComboBox<Meal> restaurantWaiterComboBox;

    @FXML
    ComboBox<Meal> restaurantDelivererComboBox;

    @FXML
    TableView<Restaurant> restaurantTableView;

    @FXML
    TableColumn<Restaurant, Long> restaurantIdColumn;

    @FXML
    TableColumn<Restaurant, String> restaurantNameColumn;

    @FXML
    TableColumn<Restaurant, String> restaurantAddressStreetColumn;

    @FXML
    TableColumn<Restaurant, String> restaurantAddressHouseNumberColumn;

    @FXML
    TableColumn<Restaurant, String> restaurantAddressCityColumn;

    @FXML
    TableColumn<Restaurant, String> restaurantAddressPostalCodeColumn;

    @FXML
    TableColumn<Restaurant, String> restaurantMealsColumn;

    @FXML
    TableColumn<Restaurant, String> restaurantChefsColumn;

    @FXML
    TableColumn<Restaurant, String> restaurantWaitersColumn;

    @FXML
    TableColumn<Restaurant, String> restaurantDeliverersColumn;

    @FXML
    Label removeFilterLabel;

    private final RestaurantRepository<Restaurant> restaurantRepository = new RestaurantRepository<>();

    @Override
    public void initialize() {
        restaurantIdColumn.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getId()).asObject());

        restaurantNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        restaurantAddressStreetColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAddress().getStreet()));

        restaurantAddressHouseNumberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAddress().getHouseNumber()));

        restaurantAddressCityColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAddress().getCity()));

        restaurantAddressPostalCodeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAddress().getPostalCode()));

        restaurantMealsColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMeals().stream()
                        .map(Entity::getName)
                        .collect(Collectors.joining("\n"))));

        restaurantChefsColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getChefs().stream()
                        .map(chef -> chef.getFirstName() + " " + chef.getLastName())
                        .collect(Collectors.joining("\n"))));

        restaurantWaitersColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getWaiters().stream()
                        .map(chef -> chef.getFirstName() + " " + chef.getLastName())
                        .collect(Collectors.joining("\n"))));

        restaurantDeliverersColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDeliverers().stream()
                        .map(chef -> chef.getFirstName() + " " + chef.getLastName())
                        .collect(Collectors.joining("\n"))));

        filter();
    }

    @Override
    public void filter() {
        List<Restaurant> restaurants = new ArrayList<>(restaurantRepository.findAll());
        restaurants.sort((r1, r2) -> r1.getId().compareTo(r2.getId()));

        String restaurantIdTextFieldValue = restaurantIdTextField.getText();
        String restaurantNameTextFieldValue = restaurantNameTextField.getText();
        String restaurantAddressStreetTextFieldValue = restaurantAddressStreetTextField.getText();
        String restaurantAddressHouseNumberTextFieldValue = restaurantAddressHouseNumberTextField.getText();
        String restaurantAddressCityTextFieldValue = restaurantAddressCityTextField.getText();
        String restaurantAddressPostalCodeTextFieldValue = restaurantAddressPostalCodeTextField.getText();

        String restaurantMealComboBoxValue = ComboBoxUtil.getComboBoxValue(restaurantMealComboBox)
                .map(Meal::getName).orElse("");
        String restaurantChefComboBoxValue = ComboBoxUtil.getComboBoxValue(restaurantChefComboBox)
                .map(Meal::getName).orElse("");
        String restaurantWaiterComboBoxValue = ComboBoxUtil.getComboBoxValue(restaurantWaiterComboBox)
                .map(Meal::getName).orElse("");
        String restaurantDelivererComboBoxValue = ComboBoxUtil.getComboBoxValue(restaurantDelivererComboBox)
                .map(Meal::getName).orElse("");

        restaurants = restaurants.stream()
                .filter(restaurant -> restaurantIdTextFieldValue.isBlank() ||
                        restaurant.getId().toString().equals(restaurantIdTextFieldValue))
                .filter(restaurant -> restaurantNameTextFieldValue.isBlank() ||
                        restaurant.getName().contains(restaurantNameTextFieldValue))
                .filter(restaurant -> restaurantAddressStreetTextFieldValue.isBlank() ||
                        restaurant.getAddress().getStreet().contains(restaurantAddressStreetTextFieldValue))
                .filter(restaurant -> restaurantAddressHouseNumberTextFieldValue.isBlank() ||
                        restaurant.getAddress().getHouseNumber().equals(restaurantAddressHouseNumberTextFieldValue))
                .filter(restaurant -> restaurantAddressCityTextFieldValue.isBlank() ||
                        restaurant.getAddress().getCity().contains(restaurantAddressCityTextFieldValue))
                .filter(restaurant -> restaurantAddressPostalCodeTextFieldValue.isBlank() ||
                        restaurant.getAddress().getPostalCode().equals(restaurantAddressPostalCodeTextFieldValue))
                .filter(restaurant -> restaurantMealComboBoxValue.isBlank() ||
                        restaurant.getMeals().stream()
                                .map(Entity::getName)
                                .toList()
                                .contains(restaurantMealComboBoxValue))
                .filter(restaurant -> restaurantChefComboBoxValue.isBlank() ||
                        restaurant.getChefs().stream()
                                .map(chef -> chef.getFirstName() + " " + chef.getLastName())
                                .toList()
                                .contains(restaurantChefComboBoxValue))
                .filter(restaurant -> restaurantWaiterComboBoxValue.isBlank() ||
                        restaurant.getWaiters().stream()
                                .map(waiter -> waiter.getFirstName() + " " + waiter.getLastName())
                                .toList()
                                .contains(restaurantWaiterComboBoxValue))
                .filter(restaurant -> restaurantDelivererComboBoxValue.isBlank() ||
                        restaurant.getDeliverers().stream()
                                .map(deliverer -> deliverer.getFirstName() + " " + deliverer.getLastName())
                                .toList()
                                .contains(restaurantDelivererComboBoxValue))
                .toList();

        removeFilterLabel.setVisible(
                !restaurantIdTextFieldValue.isBlank() ||
                !restaurantNameTextFieldValue.isBlank() ||
                !restaurantAddressStreetTextFieldValue.isBlank() ||
                !restaurantAddressHouseNumberTextFieldValue.isBlank() ||
                !restaurantAddressCityTextFieldValue.isBlank() ||
                !restaurantAddressPostalCodeTextFieldValue.isBlank() ||
                !restaurantMealComboBoxValue.isBlank() ||
                !restaurantChefComboBoxValue.isBlank() ||
                !restaurantWaiterComboBoxValue.isBlank() ||
                !restaurantDelivererComboBoxValue.isBlank());

        restaurantTableView.getItems().setAll(restaurants);
    }

    @Override
    public void removeFilter() {
        restaurantIdTextField.clear();
        restaurantNameTextField.clear();
        restaurantAddressStreetTextField.clear();
        restaurantAddressHouseNumberTextField.clear();
        restaurantAddressCityTextField.clear();
        restaurantAddressPostalCodeTextField.clear();
        restaurantMealComboBox.getSelectionModel().clearSelection();
        restaurantChefComboBox.getSelectionModel().clearSelection();
        restaurantWaiterComboBox.getSelectionModel().clearSelection();
        restaurantDelivererComboBox.getSelectionModel().clearSelection();
        removeFilterLabel.setVisible(false);

        filter();
    }
}
