package hr.java.restaurant.controller;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.model.Bonus;
import hr.java.restaurant.model.Contract;
import hr.java.restaurant.model.Person;
import hr.java.restaurant.util.ComboBoxUtil;
import hr.java.restaurant.util.Localization;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public abstract class PersonSearchController<T extends Person> implements SearchController {

    @FXML
    private TextField personIdTextField;
    @FXML
    private TextField personFirstNameTextField;
    @FXML
    private TextField personLastNameTextField;
    @FXML
    private ComboBox<ContractType> personContractTypeComboBox;
    @FXML
    private DatePicker personContractStartDateFromDatePicker;
    @FXML
    private DatePicker personContractStartDateToDatePicker;
    @FXML
    private DatePicker personContractEndDateFromDatePicker;
    @FXML
    private DatePicker personContractEndDateToDatePicker;
    @FXML
    private TextField personContractSalaryFromTextField;
    @FXML
    private TextField personContractSalaryToTextField;
    @FXML
    private TextField personContractBonusFromTextField;
    @FXML
    private TextField personContractBonusToTextField;
    @FXML
    private TableView<Person> personTableView;
    @FXML
    private TableColumn<Person, Long> personIdColumn;
    @FXML
    private TableColumn<Person, String> personFirstNameColumn;
    @FXML
    private TableColumn<Person, String> personLastNameColumn;
    @FXML
    private TableColumn<Person, String> personContractTypeColumn;
    @FXML
    private TableColumn<Person, String> personContractStartDateColumn;
    @FXML
    private TableColumn<Person, String> personContractEndDateColumn;
    @FXML
    private TableColumn<Person, String> personContractSalaryColumn;
    @FXML
    private TableColumn<Person, String> personContractBonusColumn;

    @FXML
    private Label removeFilterLabel;

    protected abstract List<T> fetchAllPeople();

    @Override
    public void initialize() {
        ComboBoxUtil.comboBoxContractTypeConverter(personContractTypeComboBox);

        personIdColumn.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getId()).asObject());

        personFirstNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirstName()));

        personLastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLastName()));

        personContractTypeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContract().getContractType().getName()));

        personContractStartDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContract().getStartDate()
                        .format(Localization.DateFormatter())));

        personContractEndDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContract().getEndDate()
                        .format(Localization.DateFormatter())));

        personContractSalaryColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContract().getSalary().toString()));

        personContractBonusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBonus().amount().toString()));

        personContractTypeComboBox.setItems(FXCollections.observableArrayList(ContractType.values()));
        filter();
    }

    @Override
    public void filter() {
        List<T> people = fetchAllPeople();
        people.sort((c1, c2) -> c1.getId().compareTo(c2.getId()));

        String personIdTextFieldValue = personIdTextField.getText();
        String personFirstNameTextFieldValue = personFirstNameTextField.getText();
        String personLastNameTextFieldValue = personLastNameTextField.getText();
        String personContractTypeComboBoxValue = ComboBoxUtil.getComboBoxValue(personContractTypeComboBox)
                .map(ContractType::getName).orElse("");
        LocalDate personContractStartDateFromValue = personContractStartDateFromDatePicker.getValue();
        LocalDate personContractStartDateToValue = personContractStartDateToDatePicker.getValue();
        LocalDate personContractEndDateFromValue = personContractEndDateFromDatePicker.getValue();
        LocalDate personContractEndDateToValue = personContractEndDateToDatePicker.getValue();
        String personContractSalaryFromTextFieldValue = personContractSalaryFromTextField.getText();
        String personContractSalaryToTextFieldValue = personContractSalaryToTextField.getText();
        String personContractBonusFromTextFieldValue = personContractBonusFromTextField.getText();
        String personContractBonusToTextFieldValue = personContractBonusToTextField.getText();

        people = people.stream()
                .filter(person -> person.getId().toString().equals(personIdTextFieldValue) || personIdTextFieldValue.isBlank())
                .filter(person -> person.getFirstName().contains(personFirstNameTextFieldValue))
                .filter(person -> person.getLastName().contains(personLastNameTextFieldValue))
                .filter(person -> person.getContract().getContractType().getName().contains(personContractTypeComboBoxValue))
                .filter(person -> {
                    Contract contract = person.getContract();
                    return (personContractStartDateFromValue == null ||
                            contract.getStartDate().isAfter(personContractStartDateFromValue.minusDays(1)))
                            && (personContractStartDateToValue == null ||
                            contract.getStartDate().isBefore(personContractStartDateToValue.plusDays(1)));
                })
                .filter(person -> {
                    Contract contract = person.getContract();
                    return (personContractEndDateFromValue == null ||
                            contract.getEndDate().isAfter(personContractEndDateFromValue.minusDays(1)))
                            && (personContractEndDateToValue == null ||
                            contract.getEndDate().isBefore(personContractEndDateToValue.plusDays(1)));
                })
                .filter(person -> {
                    Contract contract = person.getContract();
                    return (personContractSalaryFromTextFieldValue.isBlank() ||
                            contract.getSalary().compareTo(new BigDecimal(personContractSalaryFromTextFieldValue)) >= 0)
                            && (personContractSalaryToTextFieldValue.isBlank() ||
                            contract.getSalary().compareTo(new BigDecimal(personContractSalaryToTextFieldValue)) <= 0);
                })
                .filter(person -> {
                    Bonus bonus = person.getBonus();
                    return (personContractBonusFromTextFieldValue.isBlank() ||
                            bonus.amount().compareTo(new BigDecimal(personContractBonusFromTextFieldValue)) >= 0)
                            && (personContractBonusToTextFieldValue.isBlank() ||
                            bonus.amount().compareTo(new BigDecimal(personContractBonusToTextFieldValue)) <= 0);
                })
                .toList();

        removeFilterLabel.setVisible(
                !personIdTextFieldValue.isBlank() ||
                        !personFirstNameTextFieldValue.isBlank() ||
                        !personLastNameTextFieldValue.isBlank() ||
                        !personContractTypeComboBoxValue.isBlank() ||
                        personContractStartDateFromValue != null ||
                        personContractStartDateToValue != null ||
                        personContractEndDateFromValue != null ||
                        personContractEndDateToValue != null ||
                        !personContractSalaryFromTextFieldValue.isBlank() ||
                        !personContractSalaryToTextFieldValue.isBlank() ||
                        !personContractBonusFromTextFieldValue.isBlank() ||
                        !personContractBonusToTextFieldValue.isBlank());

        personTableView.setItems(FXCollections.observableArrayList(people));
    }

    @Override
    public void removeFilter() {
        personIdTextField.clear();
        personFirstNameTextField.clear();
        personLastNameTextField.clear();
        personContractTypeComboBox.getSelectionModel().clearSelection();
        personContractStartDateFromDatePicker.setValue(null);
        personContractStartDateToDatePicker.setValue(null);
        personContractEndDateFromDatePicker.setValue(null);
        personContractEndDateToDatePicker.setValue(null);
        personContractSalaryFromTextField.clear();
        personContractSalaryToTextField.clear();
        personContractBonusFromTextField.clear();
        personContractBonusToTextField.clear();
        removeFilterLabel.setVisible(false);

        filter();
    }

}
