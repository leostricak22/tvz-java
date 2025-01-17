package hr.java.restaurant.controller;

import hr.java.restaurant.model.Deliverer;
import hr.java.restaurant.model.Meal;
import hr.java.restaurant.model.Order;
import hr.java.restaurant.model.Restaurant;
import hr.java.restaurant.repository.MealRepository;
import hr.java.restaurant.repository.OrderRepository;
import hr.java.restaurant.repository.RestaurantRepository;
import hr.java.restaurant.util.AlertDialog;
import hr.java.restaurant.util.ComboBoxUtil;
import hr.java.restaurant.util.MultipleCheckBoxSelectUtil;
import hr.java.restaurant.util.SceneLoader;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.isNull;

public class OrderAddController implements AddController {

    @FXML private ComboBox<Restaurant> restaurantComboBox;
    @FXML private ComboBox<Deliverer> delivererComboBox;

    @FXML private Button pickMealsButton;
    @FXML private DatePicker dateDatePicker;
    @FXML private Label mealNameArrayLabel;

    private static final Logger logger = LoggerFactory.getLogger(OrderAddController.class);

    private final OrderRepository<Order> orderRepository = new OrderRepository<>();
    private final RestaurantRepository<Restaurant> restaurantRepository = new RestaurantRepository<>();

    private Set<Meal> selectedMeals = new HashSet<>();

    @Override
    public void initialize() {
        ComboBoxUtil.comboBoxStringConverter(restaurantComboBox);
        ComboBoxUtil.comboBoxStringConverter(delivererComboBox);
        restaurantComboBox.setItems(
                FXCollections.observableArrayList(restaurantRepository.findAll()));
    }

    @Override
    public void add() {
        Restaurant restaurant = restaurantComboBox.getValue();
        Deliverer deliverer = delivererComboBox.getValue();
        String date = dateDatePicker.getValue().toString();

        String error = validateInput(restaurant, deliverer, date, selectedMeals);
        if(!error.isEmpty()) {
            AlertDialog.showErrorDialog("Error while creating an order", error);
            logger.error("Error while creating an order: {}", error);
            return;
        }

        Order newOrder = new Order(orderRepository.getNextId(), restaurant, new ArrayList<>(selectedMeals), deliverer, dateDatePicker.getValue().atStartOfDay());
        orderRepository.add(newOrder);
        logger.info("Order created: {}", newOrder);
        AlertDialog.showInformationDialog("Order created", "Order created successfully.");

        SceneLoader.loadScene("orderSearch", "Order search");
    }

    private String validateInput(Restaurant restaurant, Deliverer deliverer, String date, Set<Meal> selectedMeals) {
        StringBuilder error = new StringBuilder();

        if(isNull(restaurant)) {
            error.append("Restaurant is not selected.\n");
        }

        if(isNull(deliverer)) {
            error.append("Deliverer is not selected.\n");
        }

        if(date.isEmpty()) {
            error.append("Date is not selected.\n");
        }

        if(selectedMeals.isEmpty()) {
            error.append("Meals are not selected.\n");
        }

        return error.toString();

    }

    public void handleSelectRestaurant() {
        delivererComboBox.setDisable(false);
        pickMealsButton.setDisable(false);
        dateDatePicker.setDisable(false);

        delivererComboBox.setItems(
                FXCollections.observableArrayList(restaurantComboBox.getValue().getDeliverers()));
    }

    public void pickMeals() {
        selectedMeals = MultipleCheckBoxSelectUtil.pickMealsFromMultipleCheckBoxWindow(
                restaurantComboBox.getValue().getMeals(),
                selectedMeals,
                mealNameArrayLabel);
    }
}
