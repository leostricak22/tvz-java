package hr.java.restaurant.controller;

import hr.java.restaurant.model.*;
import hr.java.restaurant.repository.*;
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
    private TextField restaurantIdTextField;

    @FXML
    private TextField restaurantNameTextField;

    @FXML
    private TextField restaurantAddressStreetTextField;

    @FXML
    private TextField restaurantAddressHouseNumberTextField;

    @FXML
    private TextField restaurantAddressCityTextField;

    @FXML
    private TextField restaurantAddressPostalCodeTextField;

    @FXML
    private ComboBox<Meal> restaurantMealComboBox;

    @FXML
    private ComboBox<Chef> restaurantChefComboBox;

    @FXML
    private ComboBox<Waiter> restaurantWaiterComboBox;

    @FXML
    private ComboBox<Deliverer> restaurantDelivererComboBox;

    @FXML
    private TableView<Restaurant> restaurantTableView;

    @FXML
    private TableColumn<Restaurant, Long> restaurantIdColumn;

    @FXML
    private TableColumn<Restaurant, String> restaurantNameColumn;

    @FXML
    private TableColumn<Restaurant, String> restaurantAddressStreetColumn;

    @FXML
    private TableColumn<Restaurant, String> restaurantAddressHouseNumberColumn;

    @FXML
    private TableColumn<Restaurant, String> restaurantAddressCityColumn;

    @FXML
    private TableColumn<Restaurant, String> restaurantAddressPostalCodeColumn;

    @FXML
    private TableColumn<Restaurant, String> restaurantMealsColumn;

    @FXML
    private TableColumn<Restaurant, String> restaurantChefsColumn;

    @FXML
    private TableColumn<Restaurant, String> restaurantWaitersColumn;

    @FXML
    private TableColumn<Restaurant, String> restaurantDeliverersColumn;

    @FXML
    private Label removeFilterLabel;

    private final RestaurantRepository<Restaurant> restaurantRepository = new RestaurantRepository<>();
    private final MealRepository<Meal> mealRepository = new MealRepository<>();
    private final ChefRepository<Chef> chefRepository = new ChefRepository<>();
    private final WaiterRepository<Waiter> waiterRepository = new WaiterRepository<>();
    private final DelivererRepository<Deliverer> delivererRepository = new DelivererRepository<>();

    @Override
    public void initialize() {
        ComboBoxUtil.comboBoxStringConverter(restaurantMealComboBox);
        ComboBoxUtil.comboBoxStringConverter(restaurantChefComboBox);
        ComboBoxUtil.comboBoxStringConverter(restaurantWaiterComboBox);
        ComboBoxUtil.comboBoxStringConverter(restaurantDelivererComboBox);

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

        restaurantChefComboBox.getItems().setAll(chefRepository.findAll());
        restaurantWaiterComboBox.getItems().setAll(waiterRepository.findAll());
        restaurantDelivererComboBox.getItems().setAll(delivererRepository.findAll());
        restaurantMealComboBox.getItems().setAll(mealRepository.findAll());

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
                .map(Chef::getName).orElse("");
        String restaurantWaiterComboBoxValue = ComboBoxUtil.getComboBoxValue(restaurantWaiterComboBox)
                .map(Waiter::getName).orElse("");
        String restaurantDelivererComboBoxValue = ComboBoxUtil.getComboBoxValue(restaurantDelivererComboBox)
                .map(Deliverer::getName).orElse("");

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
