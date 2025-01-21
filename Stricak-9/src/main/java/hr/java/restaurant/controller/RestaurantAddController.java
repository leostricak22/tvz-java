package hr.java.restaurant.controller;

import hr.java.restaurant.model.*;
import hr.java.restaurant.repository.*;
import hr.java.restaurant.util.AlertDialog;
import hr.java.restaurant.util.ComboBoxUtil;
import hr.java.restaurant.util.MultipleCheckBoxSelectUtil;
import hr.java.restaurant.util.SceneLoader;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.isNull;

public class RestaurantAddController implements AddController {

    @FXML private ComboBox<Address> addressComboBox;

    @FXML private TextField nameTextField;

    @FXML private Label mealNameArrayLabel;
    @FXML private Label chefNameArrayLabel;
    @FXML private Label waiterNameArrayLabel;
    @FXML private Label delivererNameArrayLabel;

    private final AddressDatabaseRepository addressRepository = new AddressDatabaseRepository();
    private final RestaurantRepository<Restaurant> restaurantRepository = new RestaurantRepository<>();
    private final MealDatabaseRepository mealRepository = new MealDatabaseRepository();
    private final ChefRepository<Chef> chefRepository = new ChefRepository<>();
    private final WaiterRepository<Waiter> waiterRepository = new WaiterRepository<>();
    private final DelivererRepository<Deliverer> delivererRepository = new DelivererRepository<>();

    private Set<Meal> selectedMeals = new HashSet<>();
    private Set<Chef> selectedChefs = new HashSet<>();
    private Set<Waiter> selectedWaiters = new HashSet<>();
    private Set<Deliverer> selectedDeliverers = new HashSet<>();


    private static final Logger logger = LoggerFactory.getLogger(RestaurantAddController.class);

    @Override
    public void initialize() {
        ComboBoxUtil.comboBoxStringConverter(addressComboBox);
        addressComboBox.setItems(
                FXCollections.observableArrayList(addressRepository.findAll()));
    }

    @Override
    public void add() {
        Address address = addressComboBox.getValue();
        String name = nameTextField.getText();

        String error = validateInput(name, address, selectedMeals, selectedChefs, selectedWaiters, selectedDeliverers);
        if (!error.isEmpty()) {
            AlertDialog.showErrorDialog("Error while creating a restaurant", "Address is not selected.");
            logger.error("Error while creating a restaurant, Address is not selected.");
            return;
        }

        Restaurant restaurant = new Restaurant(restaurantRepository.findNextId(), name, address, selectedMeals, selectedChefs, selectedWaiters, selectedDeliverers);

        restaurantRepository.save(restaurant);
        logger.info("Restaurant {} added", restaurant.getName());
        AlertDialog.showInformationDialog("Restaurant added", "Restaurant " + restaurant.getName() + " added.");

        SceneLoader.loadScene("restaurantSearch", "Search restaurants");
    }

    public void pickMeals() {
        selectedMeals = MultipleCheckBoxSelectUtil.pickMealsFromMultipleCheckBoxWindow(mealRepository.findAll(),
                selectedMeals,
                mealNameArrayLabel);
    }

    public void pickChefs() {
        Set<Chef> selectedChefsFromMultipleCheckBoxWindow =
                MultipleCheckBoxSelectUtil.openWindow(chefRepository.findAll(), selectedChefs);

        if (selectedChefsFromMultipleCheckBoxWindow.isEmpty())
            return;

        selectedChefs = selectedChefsFromMultipleCheckBoxWindow;
        chefNameArrayLabel.setText(selectedChefs.stream()
                .map(Chef::getName)
                .reduce("", (a, b) -> a + ", " + b)
                .substring(2));
    }

    public void pickWaiters() {
        Set<Waiter> selectedWaitersFromMultipleCheckBoxWindow =
                MultipleCheckBoxSelectUtil.openWindow(waiterRepository.findAll(), selectedWaiters);

        if (selectedWaitersFromMultipleCheckBoxWindow.isEmpty())
            return;

        selectedWaiters = selectedWaitersFromMultipleCheckBoxWindow;
        waiterNameArrayLabel.setText(selectedWaiters.stream()
                .map(Waiter::getName)
                .reduce("", (a, b) -> a + ", " + b)
                .substring(2));
    }

    public void pickDeliverers() {
        Set<Deliverer> selectedDeliverersFromMultipleCheckBoxWindow =
                MultipleCheckBoxSelectUtil.openWindow(delivererRepository.findAll(), selectedDeliverers);

        if (selectedDeliverersFromMultipleCheckBoxWindow.isEmpty())
            return;

        selectedDeliverers = selectedDeliverersFromMultipleCheckBoxWindow;
        delivererNameArrayLabel.setText(selectedDeliverers.stream()
                .map(Deliverer::getName)
                .reduce("", (a, b) -> a + ", " + b)
                .substring(2));
    }

    private String validateInput(String name,
                                 Address address,
                                 Set<Meal> meals,
                                 Set<Chef> chefs,
                                 Set<Waiter> waiters,
                                 Set<Deliverer> deliverers) {
        if (isNull(address))
            return "Address is not selected.";
        else if (name.isEmpty())
            return "Restaurant name is empty.";
        else if (meals.isEmpty())
            return "Meals are not selected.";
        else if (chefs.isEmpty())
            return "Chefs are not selected.";
        else if (waiters.isEmpty())
            return "Waiters are not selected.";
        else if (deliverers.isEmpty())
            return "Deliverers are not selected.";
        return "";
    }
}
