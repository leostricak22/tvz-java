package hr.java.restaurant.controller;

import hr.java.restaurant.model.Address;
import hr.java.restaurant.repository.AddressDatabaseRepository;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class AddressSearchController implements SearchController {

    @FXML private Label removeFilterLabel;

    @FXML private TextField streetTextField;
    @FXML private TextField houseNumberTextField;
    @FXML private TextField cityTextField;
    @FXML private TextField postalCodeTextField;

    @FXML private TableView<Address> addressTableView;
    @FXML private TableColumn<Address, Long> idTableColumn;
    @FXML private TableColumn<Address, String> streetTableColumn;
    @FXML private TableColumn<Address, String> houseNumberTableColumn;
    @FXML private TableColumn<Address, String> cityTableColumn;
    @FXML private TableColumn<Address, String> postalCodeTableColumn;

    private final AddressDatabaseRepository addressRepository = new AddressDatabaseRepository();

    @Override
    public void initialize() {
        idTableColumn.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getId()).asObject());

        streetTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStreet()));

        houseNumberTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getHouseNumber()));

        cityTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCity()));

        postalCodeTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPostalCode()));

        filter();
    }

    @Override
    public void filter() {
        List<Address> filteredAddresses = new ArrayList<>(addressRepository.findAll());
        filteredAddresses.sort((a1, a2) -> (int) (a1.getId() - a2.getId()));

        String street = streetTextField.getText();
        String houseNumber = houseNumberTextField.getText();
        String city = cityTextField.getText();
        String postalCode = postalCodeTextField.getText();

        filteredAddresses = filteredAddresses.stream()
                .filter(address -> address.getStreet().contains(street))
                .filter(address -> address.getHouseNumber().contains(houseNumber))
                .filter(address -> address.getCity().contains(city))
                .filter(address -> address.getPostalCode().contains(postalCode))
                .toList();

        removeFilterLabel.setVisible(
                !street.isEmpty() ||
                !houseNumber.isEmpty() ||
                !city.isEmpty() ||
                !postalCode.isEmpty());

        addressTableView.setItems(FXCollections.observableArrayList(filteredAddresses));
    }

    @Override
    public void removeFilter() {
        streetTextField.clear();
        houseNumberTextField.clear();
        cityTextField.clear();
        postalCodeTextField.clear();
        filter();
    }
}
