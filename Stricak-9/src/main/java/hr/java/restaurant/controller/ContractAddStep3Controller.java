package hr.java.restaurant.controller;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.model.Contract;
import hr.java.restaurant.repository.ContractDatabaseRepository;
import hr.java.restaurant.util.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ContractAddStep3Controller{

    private String name;
    private String salary;
    private LocalDate startDate;
    private LocalDate endDate;
    private ContractType contractType;
    private List<String> files;

    @FXML private TextField nameTextField;
    @FXML private TextField salaryTextField;
    @FXML private TextField startDateTextField;
    @FXML private TextField endDateTextField;
    @FXML private TextField contractTypeTextField;
    @FXML private ListView<String> filesListView;

    private final ContractDatabaseRepository contractRepository = new ContractDatabaseRepository();

    public void initialize(String name, String salary, LocalDate startDate, LocalDate endDate, ContractType contractType, List<String> files) {
        this.name = name;
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contractType = contractType;
        this.files = files;

        nameTextField.setText(name);
        salaryTextField.setText(salary);
        startDateTextField.setText(startDate.toString());
        endDateTextField.setText(endDate.toString());
        contractTypeTextField.setText(contractType.toString());
        filesListView.getItems().addAll(files);
    }

    public void add() {
        Contract contract = new Contract(contractRepository.findNextId(), name, new BigDecimal(salary), startDate, endDate, contractType);
        contract.setFiles(files);

        contractRepository.save(contract);

        SceneLoader.loadScene("contract", "Contracts");
    }

    public void handlePreviousScene() {
        SceneLoader.loadSceneContract("contractAddStep2", "Add contract step 2", name, salary, startDate, endDate, contractType, files);
    }
}
