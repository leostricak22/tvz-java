package hr.java.restaurant.controller;

import hr.java.restaurant.model.*;
import hr.java.restaurant.repository.DelivererRepository;
import hr.java.restaurant.repository.MealRepository;
import hr.java.restaurant.repository.OrderRepository;
import hr.java.restaurant.repository.RestaurantRepository;
import hr.java.restaurant.util.ComboBoxUtil;
import hr.java.restaurant.util.Localization;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderSearchController implements SearchController {

    @FXML
    private TextField orderIdTextField;

    @FXML
    private ComboBox<Restaurant> orderRestaurantComboBox;

    @FXML
    private ComboBox<Meal> orderMealComboBox;

    @FXML
    private ComboBox<Deliverer> orderDelivererComboBox;

    @FXML
    private DatePicker orderDeliveryDateFromDatePicker;

    @FXML
    private DatePicker orderDeliveryDateToDatePicker;

    @FXML
    private TableView<Order> orderTableView;

    @FXML
    private TableColumn<Order, Long> orderIdColumn;

    @FXML
    private TableColumn<Order, String> orderRestaurantColumn;

    @FXML
    private TableColumn<Order, String> orderMealsColumn;

    @FXML
    private TableColumn<Order, String> orderDelivererColumn;

    @FXML
    private TableColumn<Order, String> orderDeliveryDateColumn;

    @FXML
    private Label removeFilterLabel;

    private static final OrderRepository<Order> orderRepository = new OrderRepository<>();
    private static final RestaurantRepository<Restaurant> restaurantRepository = new RestaurantRepository<>();
    private static final MealRepository<Meal> mealRepository = new MealRepository<>();
    private static final DelivererRepository<Deliverer> delivererRepository = new DelivererRepository<>();

    public void setRestaurantNameParameter(String restaurantNameParameter) {
        orderRestaurantComboBox.setValue(restaurantRepository.findByName(restaurantNameParameter));

        filter();
    }

    @Override
    public void initialize() {
        ComboBoxUtil.comboBoxStringConverter(orderRestaurantComboBox);
        ComboBoxUtil.comboBoxStringConverter(orderMealComboBox);
        ComboBoxUtil.comboBoxStringConverter(orderDelivererComboBox);

        orderIdColumn.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getId()).asObject());

        orderRestaurantColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRestaurant().getName()));

        orderMealsColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMeals().stream()
                        .map(Entity::getName)
                        .collect(Collectors.joining(", "))));

        orderDelivererColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDeliverer().getName()));

        orderDeliveryDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDeliveryDateAndTime()
                        .format(Localization.DateTimeFormatter())));

        orderRestaurantComboBox.setItems(FXCollections.observableArrayList(restaurantRepository.findAll()));
        orderMealComboBox.setItems(FXCollections.observableArrayList(mealRepository.findAll()));
        orderDelivererComboBox.setItems(FXCollections.observableArrayList(delivererRepository.findAll()));

        filter();
    }

    @Override
    public void filter() {
        List<Order> orders = new ArrayList<>(orderRepository.findAll());
        orders.sort((o1, o2) -> o1.getId().compareTo(o2.getId()));

        String orderIdTextFieldValue = orderIdTextField.getText();
        String orderRestaurantComboBoxValue = ComboBoxUtil.getComboBoxValue(orderRestaurantComboBox)
                .map(Restaurant::getName).orElse("");
        String orderMealComboBoxValue = ComboBoxUtil.getComboBoxValue(orderMealComboBox)
                .map(Entity::getName).orElse("");
        String orderDelivererComboBoxValue = ComboBoxUtil.getComboBoxValue(orderDelivererComboBox)
                .map(Entity::getName).orElse("");
        LocalDate orderDeliveryDateFromDatePickerValue = orderDeliveryDateFromDatePicker.getValue();
        LocalDate orderDeliveryDateToDatePickerValue = orderDeliveryDateToDatePicker.getValue();

        orders = orders.stream()
                .filter(order -> orderIdTextFieldValue.isBlank() || order.getId().toString().equals(orderIdTextFieldValue))
                .filter(order -> orderRestaurantComboBoxValue.isBlank() || order.getRestaurant().getName().contains(orderRestaurantComboBoxValue))
                .filter(order -> orderMealComboBoxValue.isBlank() || order.getMeals().stream().anyMatch(meal -> meal.getName().contains(orderMealComboBoxValue)))
                .filter(order -> orderDelivererComboBoxValue.isBlank() || order.getDeliverer().getName().contains(orderDelivererComboBoxValue))
                .filter(order -> orderDeliveryDateFromDatePickerValue == null || order.getDeliveryDateAndTime().toLocalDate().isAfter(orderDeliveryDateFromDatePickerValue.minusDays(1)))
                .filter(order -> orderDeliveryDateToDatePickerValue == null || order.getDeliveryDateAndTime().toLocalDate().isBefore(orderDeliveryDateToDatePickerValue.plusDays(1)))
                .collect(Collectors.toList());

        removeFilterLabel.setVisible(
                !orderIdTextFieldValue.isBlank() ||
                !orderRestaurantComboBoxValue.isBlank() ||
                !orderMealComboBoxValue.isBlank() ||
                !orderDelivererComboBoxValue.isBlank() ||
                orderDeliveryDateFromDatePickerValue != null ||
                orderDeliveryDateToDatePickerValue != null);

        orderTableView.getItems().setAll(orders);
    }

    @Override
    public void removeFilter() {
        orderIdTextField.clear();
        orderRestaurantComboBox.getSelectionModel().clearSelection();
        orderMealComboBox.getSelectionModel().clearSelection();
        orderDelivererComboBox.getSelectionModel().clearSelection();
        orderDeliveryDateFromDatePicker.setValue(null);
        orderDeliveryDateToDatePicker.setValue(null);
        removeFilterLabel.setVisible(false);

        filter();
    }
}
