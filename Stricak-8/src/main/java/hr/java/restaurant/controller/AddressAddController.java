package hr.java.restaurant.controller;

import hr.java.restaurant.model.Address;
import hr.java.restaurant.repository.AddressRepository;
import hr.java.restaurant.util.AlertDialog;
import hr.java.restaurant.util.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddressAddController implements AddController {

    @FXML private TextField streetTextField;
    @FXML private TextField houseNumberTextField;
    @FXML private TextField cityTextField;
    @FXML private TextField postalCodeTextField;

    private final AddressRepository addressRepository = new AddressRepository();
    private static final Logger logger = LoggerFactory.getLogger(AddressAddController.class);

    @Override
    public void initialize() {

    }

    @Override
    public void add() {
        String street = streetTextField.getText();
        String houseNumber = houseNumberTextField.getText();
        String city = cityTextField.getText();
        String postalCode = postalCodeTextField.getText();

        String errorMessage = validateInput(street, houseNumber, city, postalCode);
        if (!errorMessage.isEmpty()) {
            AlertDialog.showErrorDialog("Address add error", errorMessage);
            logger.error("Address add error: {}", errorMessage);
            return;
        }

        Address address = new Address.Builder(addressRepository.getNextId())
                .setStreet(street)
                .setHouseNumber(houseNumber)
                .setCity(city)
                .setPostalCode(postalCode)
                .build();


        addressRepository.add(address);
        logger.info("Address added: {}", address);
        AlertDialog.showInformationDialog("Address add", "Address added successfully!");

        //SceneLoader.loadScene("addressSearch", "Address search");
    }

    private String validateInput(String street, String houseNumber, String city, String postalCode) {
        if (street.isEmpty() || houseNumber.isEmpty() || city.isEmpty() || postalCode.isEmpty()) {
            return "Sva polja moraju biti popunjena!";
        }
        return "";
    }
}
