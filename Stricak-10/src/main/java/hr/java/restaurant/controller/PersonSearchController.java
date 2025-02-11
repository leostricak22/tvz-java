package hr.java.restaurant.controller;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.model.Bonus;
import hr.java.restaurant.model.Contract;
import hr.java.restaurant.model.Deliverer;
import hr.java.restaurant.model.Person;
import hr.java.restaurant.util.ComboBoxUtil;
import hr.java.restaurant.util.FXMLLoaderHelper;
import hr.java.restaurant.util.Localization;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static java.util.Objects.isNull;

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



    @FXML
    private TableColumn<Deliverer, String> personOrderWaitingColumn;
    @FXML
    private TableColumn<Deliverer, String> personOrderDeliveredColumn;
    @FXML
    private TableColumn<Deliverer, String> personOrderDeliverInProcessColumn;

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
                new SimpleStringProperty(isNull(cellData.getValue().getContract().getEndDate()) ? "-" : cellData.getValue().getContract().getEndDate().format(Localization.DateFormatter())));

        personContractSalaryColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContract().getSalary().toString()));

        personContractBonusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBonus().amount().toString()));

        personOrderDeliveredColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf((cellData.getValue()).getDeliveredOrders().size())));

        personOrderWaitingColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf((cellData.getValue()).getWaitingOrders().size())));

        personOrderDeliverInProcessColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf((cellData.getValue()).getDeliverInProcessOrders().size())));

        personContractTypeComboBox.setItems(FXCollections.observableArrayList(ContractType.values()));

        personTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                openDelivererOrderDetails(newSelection);
            } else {
                System.out.println("No selection");
            }
        });

        filter();
    }

    private void openDelivererOrderDetails(Person person) {
        if (person instanceof Deliverer deliverer) {

            System.out.println(deliverer.getName());

            try {
                FXMLLoader loader = FXMLLoaderHelper.fxmlFilePath("delivererOrderDetails.fxml");
                Parent root = loader.load();

                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.setTitle("Deliverer details");
                popupStage.setScene(new Scene(root, 500, 500));

                DelivererOrderDetailsController controller = loader.getController();
                controller.initialize(deliverer);

                controller.setOnComplete(this::reset);

                popupStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void reset() {
        personTableView.getSelectionModel().clearSelection();

        List<T> people = fetchAllPeople();
        personTableView.setItems(FXCollections.observableArrayList(people));

    }

    @Override
    public void filter() {
        System.out.println("Filtering");
        reset();
        /*List<T> people = fetchAllPeople();
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

         */
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
