package hr.java.restaurant.util;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.enumeration.MealTypeEnum;
import hr.java.restaurant.model.Category;
import hr.java.restaurant.model.Entity;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;

import java.util.Optional;

public class ComboBoxUtil {
    public static <T extends Entity > void comboBoxStringConverter(ComboBox<T> comboBox) {
        comboBox.setCellFactory(cellData -> new ListCell<T>() {
            public void updateItem(T object, boolean empty) {
                super.updateItem(object, empty);

                if (empty || object == null) {
                    setText(null);
                } else {
                    setText(object.getName());
                }
            }
        });

        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(T object) {
                return object != null ? object.getName() : "";
            }

            @Override
            public T fromString(String string) {
                return comboBox.getItems().stream()
                        .filter(object -> object.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    public static void comboBoxMealTypeConverter(ComboBox<MealTypeEnum> comboBox) {
        comboBox.setCellFactory(cellData -> new ListCell<MealTypeEnum>() {
            public void updateItem(MealTypeEnum object, boolean empty) {
                super.updateItem(object, empty);

                if (empty || object == null) {
                    setText(null);
                } else {
                    setText(object.getName());
                }
            }
        });

        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(MealTypeEnum object) {
                return object != null ? object.getName() : "";
            }

            @Override
            public MealTypeEnum fromString(String string) {
                return comboBox.getItems().stream()
                        .filter(object -> object.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    public static void comboBoxContractTypeConverter(ComboBox<ContractType> comboBox) {
        comboBox.setCellFactory(cellData -> new ListCell<ContractType>() {
            public void updateItem(ContractType object, boolean empty) {
                super.updateItem(object, empty);

                if (empty || object == null) {
                    setText(null);
                } else {
                    setText(object.getName());
                }
            }
        });

        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(ContractType object) {
                return object != null ? object.getName() : "";
            }

            @Override
            public ContractType fromString(String string) {
                return comboBox.getItems().stream()
                        .filter(object -> object.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    public static <T> Optional<T> getComboBoxValue(ComboBox<T> comboBox) {
        if(comboBox.getValue() == null) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(comboBox.getValue());
        }
    }
}
