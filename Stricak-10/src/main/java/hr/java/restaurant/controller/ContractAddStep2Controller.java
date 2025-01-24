package hr.java.restaurant.controller;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.util.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class ContractAddStep2Controller {
    private String name;
    private String salary;
    private LocalDate startDate;
    private LocalDate endDate;
    private ContractType contractType;
    private List<String> files;

    @FXML private ListView<String> filesListView;

    public void initialize(String name, String salary, LocalDate startDate, LocalDate endDate, ContractType contractType, List<String> files) {
        this.name = name;
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contractType = contractType;
        this.files = files;

        if (!isNull(files))
           filesListView.getItems().addAll(files);
    }

    public void selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Path destinationDir = Path.of("files");

            if (!Files.exists(destinationDir)) {
                try {
                    Files.createDirectory(destinationDir);
                } catch (IOException e) {
                    return;
                }
            }

            try {
                String uniqueName = Instant.now().toEpochMilli() + "_" + selectedFile.getName();
                Path destinationPath = destinationDir.resolve(uniqueName);
                filesListView.getItems().add(destinationPath.toString());
                files.add(destinationPath.toString());
                Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleNextScene() {
        SceneLoader.loadSceneContract("contractAddStep3", "Add contract step 3", name, salary, startDate, endDate, contractType, files);
    }
    public void handlePreviousScene() {
        SceneLoader.loadSceneContract("contractAddStep1", "Add contract step 1", name, salary, startDate, endDate, contractType, files);
    }
}
