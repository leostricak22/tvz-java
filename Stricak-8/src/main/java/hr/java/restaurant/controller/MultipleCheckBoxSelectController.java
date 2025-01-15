package hr.java.restaurant.controller;

import hr.java.restaurant.model.Entity;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MultipleCheckBoxSelectController<T extends Entity> {
    @FXML
    private ListView<CheckBox> listView;

    private Consumer<List<T>> onConfirmCallback;
    private final Map<CheckBox, T> checkBoxToEntityMap = new HashMap<>();

    public void initialize(List<T> entities, List<T> previouslySelectedItems) {
        for (T entity : entities) {
            CheckBox checkBox = new CheckBox(entity.getName());

            if (previouslySelectedItems.contains(entity)) {
                checkBox.setSelected(true);
            }

            checkBoxToEntityMap.put(checkBox, entity);
            listView.getItems().add(checkBox);
        }
    }

    public void setOnConfirmCallback(Consumer<List<T>> callback) {
        this.onConfirmCallback = callback;
    }

    @FXML
    private void onConfirm() {
        List<T> selectedEntities = listView.getItems().stream()
                .filter(CheckBox::isSelected)
                .map(checkBoxToEntityMap::get)
                .toList();

        if (onConfirmCallback != null) {
            onConfirmCallback.accept(selectedEntities);
        }

        listView.getScene().getWindow().hide();
    }
}
