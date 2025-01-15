package hr.java.restaurant.util;

import hr.java.restaurant.exception.DuplicateEntryException;
import hr.java.restaurant.exception.InvalidValueException;
import hr.java.restaurant.model.*;

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

    public static boolean validString(String value) {
        return !value.isEmpty();
    }

    public static boolean validBigDecimal(String value) {
        return value.matches("\\d+(\\.\\d+)?");
    }

    public static boolean validInteger(String value) {
        return value.matches("\\d+");
    }

    public static <T extends Entity> boolean isDuplicateByName(Iterable<T> list, String name) {
        for (T item : list) {
            if (item.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given name is a duplicate.
     * @param people the people
     * @param name the name
     * @throws DuplicateEntryException if the name is a duplicate
     */
    public static void checkDuplicatePerson(Set<Person> people, String name) throws DuplicateEntryException {
        for (Person person : people) {
            if (person != null && (person.getFirstName() + " " + person.getLastName()).equals(name))
                throw new DuplicateEntryException();
        }
    }

    public static void checkDuplicateChef(Set<Chef> chefs, String name) throws DuplicateEntryException {
        for (Chef person : chefs) {
            if (person != null && (person.getFirstName() + " " + person.getLastName()).equals(name))
                throw new DuplicateEntryException();
        }
    }

    public static void checkDuplicateWaiter(Set<Waiter> waiters, String name) throws DuplicateEntryException {
        for (Waiter person : waiters) {
            if (person != null && (person.getFirstName() + " " + person.getLastName()).equals(name))
                throw new DuplicateEntryException();
        }
    }

    public static void checkDuplicateDeliverer(Set<Deliverer> deliverers, String name) throws DuplicateEntryException {
        for (Deliverer person : deliverers) {
            if (person != null && (person.getFirstName() + " " + person.getLastName()).equals(name))
                throw new DuplicateEntryException();
        }
    }



    /**
     * Checks if the given name is a duplicate.
     * @param restaurants the restaurants
     * @param name the name
     * @throws DuplicateEntryException if the name is a duplicate
     */
    public static void checkDuplicateRestaurant(List<Restaurant> restaurants, String name) throws DuplicateEntryException {
        for (Restaurant restaurant : restaurants) {
            if (restaurant != null && restaurant.getName().equals(name))
                throw new DuplicateEntryException();
        }
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

    /**
     * Checks if the given name is a duplicate.
     * @param ingredients the ingredients.txt
     * @param name the name
     * @throws DuplicateEntryException if the name is a duplicate
     */
    public static void checkDuplicateIngredient(Set<Ingredient> ingredients, String name) throws DuplicateEntryException {
        for (Ingredient ingredient : ingredients) {
            if (ingredient != null && ingredient.getName().equals(name))
                throw new DuplicateEntryException();
        }
    }

    /**
     * Checks if the given name is a duplicate.
     * @param meals the meals
     * @param name the name
     * @throws DuplicateEntryException if the name is a duplicate
     */
    public static void checkDuplicateMeal(Set<Meal> meals, String name) throws DuplicateEntryException {
        for (Meal meal : meals) {
            if (meal != null && meal.getName().equals(name))
                throw new DuplicateEntryException();
        }
    }

    /**
     * Checks if the given salary is valid.
     * @param salary the salary
     * @throws InvalidValueException if the salary is invalid
     */
    public static void checkSalary(BigDecimal salary) throws InvalidValueException{
        if (salary.compareTo(Contract.getMinSalary()) <= 0)
            throw new InvalidValueException();
    }

    /**
     * Checks if the given price is valid.
     * @param price the price
     * @throws InvalidValueException if the price is invalid
     */
    public static void checkMealPrice(BigDecimal price) throws InvalidValueException {
        if(price.compareTo(Meal.getUnrealPrice()) >= 0 || price.compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidValueException();
    }
}
