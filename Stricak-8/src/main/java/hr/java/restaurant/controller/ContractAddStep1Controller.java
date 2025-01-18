package hr.java.restaurant.controller;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.util.AlertDialog;
import hr.java.restaurant.util.ComboBoxUtil;
import hr.java.restaurant.util.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class ContractAddStep1Controller {

    @FXML private TextField nameTextField;
    @FXML private TextField salaryTextField;
    @FXML private DatePicker startDateDatePicker;
    @FXML private DatePicker endDateDatePicker;
    @FXML private ComboBox<ContractType> contractTypeComboBox;

    private List<String> files = new ArrayList<>();

    public void initialize(String name, String salary, LocalDate startDate, LocalDate endDate, ContractType contractType, List<String> files) {
        nameTextField.setText(name);
        salaryTextField.setText(salary);
        startDateDatePicker.setValue(startDate);
        endDateDatePicker.setValue(endDate);

        this.files = files;

        ComboBoxUtil.comboBoxContractTypeConverter(contractTypeComboBox);
        contractTypeComboBox.getItems().setAll(ContractType.values());

        contractTypeComboBox.setValue(contractType);
    }

    public void initialize() {
        ComboBoxUtil.comboBoxContractTypeConverter(contractTypeComboBox);
        contractTypeComboBox.getItems().setAll(ContractType.values());
    }


    private String validateInput(String name, String salary, LocalDate startDate, LocalDate endDate, ContractType contractType) {
        if (name.isEmpty() || salary.isEmpty() || isNull(startDate) || isNull(endDate) || isNull(contractType)) {
            return "All fields must be filled!";
        }

        if (startDate.isAfter(endDate)) {
            return "Start date must be before end date!";
        }
        return "";
    }

    public void handleNextScene() {
        String name = nameTextField.getText();
        String salary = salaryTextField.getText();
        LocalDate startDate = startDateDatePicker.getValue();
        LocalDate endDate = endDateDatePicker.getValue();
        ContractType contractType = contractTypeComboBox.getValue();

        String error = validateInput(name, salary, startDate, endDate, contractType);
        if(!error.isEmpty()) {
            AlertDialog.showErrorDialog("Error", error);
            return;
        }

        SceneLoader.loadSceneContract("contractAddStep2", "Add contract step 2", name, salary, startDate, endDate, contractType, files);
    }
}
