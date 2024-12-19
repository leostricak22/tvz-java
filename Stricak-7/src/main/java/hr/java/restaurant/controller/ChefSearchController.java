package hr.java.restaurant.controller;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.model.Bonus;
import hr.java.restaurant.model.Chef;
import hr.java.restaurant.model.Contract;
import hr.java.restaurant.repository.ChefRepository;
import hr.java.restaurant.util.ComboBoxUtil;
import hr.java.restaurant.util.Localization;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ChefSearchController implements SearchController {

    @FXML
    private TextField chefIdTextField;
    @FXML
    private TextField chefFirstNameTextField;
    @FXML
    private TextField chefLastNameTextField;
    @FXML
    private ComboBox<ContractType> chefContractTypeComboBox;
    @FXML
    private DatePicker chefContractStartDateFromDatePicker;
    @FXML
    private DatePicker chefContractStartDateToDatePicker;
    @FXML
    private DatePicker chefContractEndDateFromDatePicker;
    @FXML
    private DatePicker chefContractEndDateToDatePicker;
    @FXML
    private TextField chefContractSalaryFromTextField;
    @FXML
    private TextField chefContractSalaryToTextField;
    @FXML
    private TextField chefContractBonusFromTextField;
    @FXML
    private TextField chefContractBonusToTextField;
    @FXML
    private TableView<Chef> chefTableView;
    @FXML
    private TableColumn<Chef, Long> chefIdColumn;
    @FXML
    private TableColumn<Chef, String> chefFirstNameColumn;
    @FXML
    private TableColumn<Chef, String> chefLastNameColumn;
    @FXML
    private TableColumn<Chef, String> chefContractTypeColumn;
    @FXML
    private TableColumn<Chef, String> chefContractStartDateColumn;
    @FXML
    private TableColumn<Chef, String> chefContractEndDateColumn;
    @FXML
    private TableColumn<Chef, String> chefContractSalaryColumn;
    @FXML
    private TableColumn<Chef, String> chefContractBonusColumn;

    @FXML
    private Label removeFilterLabel;

    private final static ChefRepository<Chef> chefRepository = new ChefRepository<>();

    @Override
    public void initialize() {
        ComboBoxUtil.comboBoxContractTypeConverter(chefContractTypeComboBox);

        chefIdColumn.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getId()).asObject());

        chefFirstNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirstName()));

        chefLastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLastName()));

        chefContractTypeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContract().getContractType().getName()));

        chefContractStartDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContract().getStartDate()
                        .format(Localization.DateFormatter())));

        chefContractEndDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContract().getEndDate()
                        .format(Localization.DateFormatter())));

        chefContractSalaryColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContract().getSalary().toString()));

        chefContractBonusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBonus().amount().toString()));

        chefContractTypeComboBox.setItems(FXCollections.observableArrayList(ContractType.values()));
        filter();
    }

    @Override
    public void filter() {
        List<Chef> chefs = new ArrayList<>(chefRepository.findAll());
        chefs.sort((c1, c2) -> c1.getId().compareTo(c2.getId()));

        String chefIdTextFieldValue = chefIdTextField.getText();
        String chefFirstNameTextFieldValue = chefFirstNameTextField.getText();
        String chefLastNameTextFieldValue = chefLastNameTextField.getText();
        String chefContractTypeComboBoxValue = ComboBoxUtil.getComboBoxValue(chefContractTypeComboBox)
                .map(ContractType::getName).orElse("");
        LocalDate chefContractStartDateFromValue = chefContractStartDateFromDatePicker.getValue();
        LocalDate chefContractStartDateToValue = chefContractStartDateToDatePicker.getValue();
        LocalDate chefContractEndDateFromValue = chefContractEndDateFromDatePicker.getValue();
        LocalDate chefContractEndDateToValue = chefContractEndDateToDatePicker.getValue();
        String chefContractSalaryFromTextFieldValue = chefContractSalaryFromTextField.getText();
        String chefContractSalaryToTextFieldValue = chefContractSalaryToTextField.getText();
        String chefContractBonusFromTextFieldValue = chefContractBonusFromTextField.getText();
        String chefContractBonusToTextFieldValue = chefContractBonusToTextField.getText();

        chefs = chefs.stream()
                .filter(chef -> chef.getId().toString().contains(chefIdTextFieldValue))
                .filter(chef -> chef.getFirstName().contains(chefFirstNameTextFieldValue))
                .filter(chef -> chef.getLastName().contains(chefLastNameTextFieldValue))
                .filter(chef -> chef.getContract().getContractType().getName().contains(chefContractTypeComboBoxValue))
                .filter(chef -> {
                    Contract contract = chef.getContract();
                    return (chefContractStartDateFromValue == null ||
                            contract.getStartDate().isAfter(chefContractStartDateFromValue.minusDays(1)))
                            && (chefContractStartDateToValue == null ||
                            contract.getStartDate().isBefore(chefContractStartDateToValue.plusDays(1)));
                })
                .filter(chef -> {
                    Contract contract = chef.getContract();
                    return (chefContractEndDateFromValue == null ||
                            contract.getEndDate().isAfter(chefContractEndDateFromValue.minusDays(1)))
                            && (chefContractEndDateToValue == null ||
                            contract.getEndDate().isBefore(chefContractEndDateToValue.plusDays(1)));
                })
                .filter(chef -> {
                    Contract contract = chef.getContract();
                    return (chefContractSalaryFromTextFieldValue.isBlank() ||
                            contract.getSalary().compareTo(new BigDecimal(chefContractSalaryFromTextFieldValue)) >= 0)
                            && (chefContractSalaryToTextFieldValue.isBlank() ||
                            contract.getSalary().compareTo(new BigDecimal(chefContractSalaryToTextFieldValue)) <= 0);
                })
                .filter(chef -> {
                    Bonus bonus = chef.getBonus();
                    return (chefContractBonusFromTextFieldValue.isBlank() ||
                            bonus.amount().compareTo(new BigDecimal(chefContractBonusFromTextFieldValue)) >= 0)
                            && (chefContractBonusToTextFieldValue.isBlank() ||
                            bonus.amount().compareTo(new BigDecimal(chefContractBonusToTextFieldValue)) <= 0);
                })
                .toList();

        removeFilterLabel.setVisible(
                !chefIdTextFieldValue.isBlank() ||
                !chefFirstNameTextFieldValue.isBlank() ||
                !chefLastNameTextFieldValue.isBlank() ||
                !chefContractTypeComboBoxValue.isBlank() ||
                chefContractStartDateFromValue != null ||
                chefContractStartDateToValue != null ||
                chefContractEndDateFromValue != null ||
                chefContractEndDateToValue != null ||
                !chefContractSalaryFromTextFieldValue.isBlank() ||
                !chefContractSalaryToTextFieldValue.isBlank() ||
                !chefContractBonusFromTextFieldValue.isBlank() ||
                !chefContractBonusToTextFieldValue.isBlank());

        chefTableView.setItems(FXCollections.observableArrayList(chefs));
    }

    @Override
    public void removeFilter() {
        chefIdTextField.clear();
        chefFirstNameTextField.clear();
        chefLastNameTextField.clear();
        chefContractTypeComboBox.getSelectionModel().clearSelection();
        chefContractStartDateFromDatePicker.setValue(null);
        chefContractStartDateToDatePicker.setValue(null);
        chefContractEndDateFromDatePicker.setValue(null);
        chefContractEndDateToDatePicker.setValue(null);
        chefContractSalaryFromTextField.clear();
        chefContractSalaryToTextField.clear();
        chefContractBonusFromTextField.clear();
        chefContractBonusToTextField.clear();
        removeFilterLabel.setVisible(false);

        filter();
    }
}
