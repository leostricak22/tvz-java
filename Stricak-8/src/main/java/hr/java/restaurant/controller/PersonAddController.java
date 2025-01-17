package hr.java.restaurant.controller;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.model.Bonus;
import hr.java.restaurant.model.Contract;
import hr.java.restaurant.model.Person;
import hr.java.restaurant.repository.ContractRepository;
import hr.java.restaurant.util.AlertDialog;
import hr.java.restaurant.util.ComboBoxUtil;
import hr.java.restaurant.util.Validation;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.util.Objects.isNull;

public abstract class PersonAddController<T extends Person> implements AddController {

    @FXML private Label titleLabel;
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private ComboBox<Contract> contractComboBox;
    @FXML private TextField bonusTextField;

    private String title;
    private static final Logger logger = LoggerFactory.getLogger(PersonAddController.class);
    private final ContractRepository contractRepository = new ContractRepository();

    abstract T addPerson(String firstName, String lastName, Contract contract, Bonus bonus);
    abstract void savePerson(T person);
    abstract void loadPersonScene();

    @FXML
    public void initialize() {
        ComboBoxUtil.comboBoxStringConverter(contractComboBox);
        contractComboBox.setItems(FXCollections.observableArrayList(contractRepository.findAll()));
        titleLabel.setText(title);
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

        Bonus bonusObject = new Bonus(new BigDecimal(bonus));

        System.out.println("Adding person");
        System.out.println("First name: " + firstName);
        System.out.println("Last name: " + lastName);
        System.out.println("Contract: " + contract);
        System.out.println("Bonus: " + bonusObject);
        T newPerson = addPerson(firstName, lastName, contract, bonusObject);

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
        if (isNull(contract)) {
            return "Contract must be selected";
        }

        return "";
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
