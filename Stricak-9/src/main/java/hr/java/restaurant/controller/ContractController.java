package hr.java.restaurant.controller;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.model.Contract;
import hr.java.restaurant.repository.ContractRepository;
import hr.java.restaurant.util.ComboBoxUtil;
import hr.java.restaurant.util.Localization;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class ContractController implements SearchController {

    @FXML private TextField contractSalaryFromTextField;
    @FXML private TextField contractSalaryToTextField;
    @FXML private DatePicker contractEndDateFromDatePicker;
    @FXML private DatePicker contractEndDateToDatePicker;
    @FXML private ComboBox<ContractType> contractTypeComboBox;
    @FXML private TextField addContractSalaryTextField;
    @FXML private ComboBox<ContractType> addContractTypeComboBox;
    @FXML private DatePicker addContractStartDateDatePicker;
    @FXML private DatePicker addContractEndDateDatePicker;
    @FXML private Label errorLabel;
    @FXML private TableView<Contract> contractTableView;
    @FXML private TableColumn<Contract, String> idColumn;
    @FXML private TableColumn<Contract, String> nameColumn;
    @FXML private TableColumn<Contract, String> salaryColumn;
    @FXML private TableColumn<Contract, String> startDateColumn;
    @FXML private TableColumn<Contract, String> endDateColumn;
    @FXML private TableColumn<Contract, String> contractTypeColumn;
    @FXML private TableColumn<Contract, String> activeColumn;

    private final ContractRepository contractRepository = new ContractRepository();

    @Override
    public void initialize() {
        ComboBoxUtil.comboBoxContractTypeConverter(contractTypeComboBox);
        ComboBoxUtil.comboBoxContractTypeConverter(addContractTypeComboBox);

        idColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getId().toString()));

        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        salaryColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSalary().toString()));

        startDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStartDate().format(Localization.DateFormatter())));

        endDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(isNull(cellData.getValue().getEndDate()) ? "-" : cellData.getValue().getEndDate().format(Localization.DateFormatter())));

        contractTypeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContractType().getName()));

        activeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getActiveAsString()));

        contractTypeComboBox.setItems(FXCollections.observableArrayList(ContractType.values()));
        addContractTypeComboBox.setItems(FXCollections.observableArrayList(ContractType.values()));
        filter();
    }

    @Override
    public void filter() {
        List<Contract> contracts = new ArrayList<>(contractRepository.findAll());

        String contractSalaryFromTextFieldValue = contractSalaryFromTextField.getText();
        String contractSalaryToTextFieldValue = contractSalaryToTextField.getText();
        LocalDate contractEndDateFromDatePickerValue = contractEndDateFromDatePicker.getValue();
        LocalDate contractEndDateToDatePickerValue = contractEndDateToDatePicker.getValue();
        String contractTypeComboBoxValue = ComboBoxUtil.getComboBoxValue(contractTypeComboBox).map(ContractType::getName).orElse("");

        contracts = contracts.stream()
                .filter(contract -> contractSalaryFromTextFieldValue.isEmpty() ||
                        contract.getSalary().compareTo(new BigDecimal(contractSalaryFromTextFieldValue)) >= 0)
                .filter(contract -> contractSalaryToTextFieldValue.isEmpty() ||
                        contract.getSalary().compareTo(new BigDecimal(contractSalaryToTextFieldValue)) <= 0)
                .filter(contract -> isNull(contractEndDateFromDatePickerValue) ||
                        contract.getEndDate().isAfter(contractEndDateFromDatePickerValue))
                .filter(contract -> contractEndDateToDatePickerValue == null ||
                        contract.getEndDate().isBefore(contractEndDateToDatePickerValue))
                .filter(contract -> contractTypeComboBoxValue.isEmpty() ||
                        contract.getContractType().getName().equals(contractTypeComboBoxValue))
                .toList();

        ObservableList<Contract> contractObservableList =  FXCollections.observableList(contracts);
        contractTableView.setItems(contractObservableList);
    }

    @Override
    public void removeFilter() {
        contractSalaryFromTextField.clear();
        contractSalaryToTextField.clear();
        contractEndDateFromDatePicker.setValue(null);
        contractEndDateToDatePicker.setValue(null);
        contractTypeComboBox.setValue(null);
        filter();
    }

    public void addContract() {
        String addContractSalary = addContractSalaryTextField.getText();
        var  addContractType = ComboBoxUtil.getComboBoxValue(addContractTypeComboBox).map(ContractType::getName);
        LocalDate addContractStartDate = addContractStartDateDatePicker.getValue();
        LocalDate addContractEndDate = addContractEndDateDatePicker.getValue();

        String errorMessage = "";
        if(addContractSalary.isBlank())
            errorMessage = "Salary is required";
        else if (addContractType.isEmpty())
            errorMessage = "Contract type is required";
        else if (addContractStartDate == null)
            errorMessage = "Contract start date is required";
        else if (addContractEndDate == null)
            errorMessage = "Contract end date is required";
        else if (!addContractStartDate.isBefore(addContractEndDate))
            errorMessage = "Start date must be before the end date!";

        if(!errorMessage.isBlank()) {
            errorLabel.setText(errorMessage);
            return;
        }

        errorLabel.setText("");

        Long nextId = contractRepository.findNextId();
        Contract contract = new Contract(
                contractRepository.findNextId(),
                "Contract" + nextId,
                new BigDecimal(addContractSalary),
                addContractStartDate,
                addContractEndDate,
                ContractType.valueOfByName(addContractType.orElse(null))
        );
        contractRepository.save(contract);

        removeFilter();
    }
}
