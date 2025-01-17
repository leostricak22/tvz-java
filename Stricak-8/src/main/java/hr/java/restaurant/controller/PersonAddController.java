package hr.java.restaurant.controller;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.model.Contract;
import hr.java.restaurant.model.Person;
import hr.java.restaurant.util.AlertDialog;
import hr.java.restaurant.util.Validation;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class PersonAddController<T extends Person> implements AddController {
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private ComboBox<Contract> contractComboBox;
    @FXML private TextField bonusTextField;

    private static final Logger logger = LoggerFactory.getLogger(PersonAddController.class);

    abstract T addPerson(String firstName, String lastName, Contract contract, BigDecimal bonus);
    abstract void savePerson(T person);
    abstract void loadPersonScene();

    @FXML
    public void initialize() {
        // TODO: load contracts to combobox
    }

    @Override
    public void add() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        Contract contract = contractComboBox.getValue();
        String bonus = bonusTextField.getText();

        String error = validateInput(firstName, lastName, contract, bonus);
        if(!error.isEmpty()) {
            AlertDialog.showErrorDialog("Error creating a Person", error);
            logger.error("Error creating a Person: {}", error);
            return;
        }

        BigDecimal bonusBigDecimal = new BigDecimal(bonusTextField.getText());

        T newPerson = addPerson(firstName, lastName,
                new Contract(BigDecimal.TEN,
                        LocalDate.parse("2024-12-12"),
                        LocalDate.parse("2024-12-12"),
                        ContractType.FULL_TIME), bonusBigDecimal);

        savePerson(newPerson);
        AlertDialog.showInformationDialog("Person added", "Person added successfully");
        logger.info("Person {} added successfully", newPerson.getName());

        loadPersonScene();
    }

    private String validateInput(String firstName, String lastName, Contract contract, String bonus) {
        if (!Validation.validString(firstName)) {
            return "First name must be filled";
        }
        if (!Validation.validString(lastName)) {
            return "Last name must be filled";
        }
        if (!Validation.validBigDecimal(bonus)) {
            return "Bonus must be a number";
        }
        if (new BigDecimal(bonus).compareTo(BigDecimal.ZERO) < 0) {
            return "Bonus must be a positive number";
        }

        // TODO: add contract validation

        return "";
    }
}
