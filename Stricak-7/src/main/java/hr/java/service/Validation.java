package hr.java.service;

import hr.java.restaurant.exception.DuplicateEntryException;
import hr.java.restaurant.exception.InvalidValueException;
import hr.java.restaurant.model.Category;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Represents a validation class.
 */
public class Validation {
    /**
     * Checks if the given value is a valid postal code.
     *
     * @param value the value to check
     * @return true if the value is a valid postal code, false otherwise
     */
    public static boolean validPostalCode(String value) {
        return value.matches("^[1-5]\\d{4}$");
    }

    /**
     * Checks if the given value is a valid LocalDateTime.
     * @param value the value to check
     * @return true if the value is a valid LocalDateTime, false otherwise
     */
    public static boolean validLocalDateTime(String value) {
        return value.matches("\\b([0-9]{4})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])T([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])\\b");
    }

    /**
     * Checks if the given value is a valid LocalDate.
     * @param value the value to check
     * @return true if the value is a valid LocalDate, false otherwise
     */
    public static boolean validLocalDate(String value) {
        return value.matches("\\b([0-9]{4})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])\\b");
    }

    /**
     * Checks if the given name is a duplicate.
     * @param categories the categories
     * @param name the name
     * @throws DuplicateEntryException if the name is a duplicate
     */
    public static void checkDuplicateCategory(List<Category> categories, String name) throws DuplicateEntryException {
        for (Category category : categories) {
            if (category != null && category.getName().equals(name))
                throw new DuplicateEntryException();
        }
    }


}
