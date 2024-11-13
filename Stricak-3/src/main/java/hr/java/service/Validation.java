package hr.java.service;

import hr.java.restaurant.exception.DuplicateEntryException;
import hr.java.restaurant.model.*;

public class Validation {
    public static boolean isBigDecimal(String value) {
        return value.matches("\\d+(\\.\\d+)?");
    }

    public static boolean isInteger(String value) {
        return value.matches("[1-9]\\d*");
    }

    public static boolean validPostalCode(String value) {
        return value.matches("^[1-5]\\d{4}$");
    }

    public static boolean validLocalDateTime(String value) {
        return value.matches("\\b([0-9]{4})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])T([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])\\b");
    }

    public static boolean validLocalDate(String value) {
        return value.matches("\\b([0-9]{4})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])\\b");
    }

    public static void checkDuplicatePerson(Person[] people, String name) throws DuplicateEntryException {
        for (Person person : people) {
            if( person != null) {
                System.out.println("KonobarLmao: " + person.getFirstName() + " " + person.getFirstName());
                System.out.println("KonobarLmao: " + (person.getFirstName() + " " + person.getFirstName()).equals(name));
                if (person != null && (person.getFirstName() + " " + person.getLastName()).equals(name))
                    throw new DuplicateEntryException();
            }
        }
    }

    public static void checkDuplicateRestaurant(Restaurant[] restaurants, String name) throws DuplicateEntryException {
        for (Restaurant restaurant : restaurants) {
            if (restaurant != null && restaurant.getName().equals(name))
                throw new DuplicateEntryException();
        }
    }

    public static void checkDuplicateCategory(Category[] categories, String name) throws DuplicateEntryException {
        for (Category category : categories) {
            if (category != null && category.getName().equals(name))
                throw new DuplicateEntryException();
        }
    }

    public static void checkDuplicateIngredient(Ingredient[] ingredients, String name) throws DuplicateEntryException {
        for (Ingredient ingredient : ingredients) {
            if (ingredient != null && ingredient.getName().equals(name))
                throw new DuplicateEntryException();
        }
    }

    public static void checkDuplicateMeal(Meal[] meals, String name) throws DuplicateEntryException {
        for (Meal meal : meals) {
            if (meal != null && meal.getName().equals(name))
                throw new DuplicateEntryException();
        }
    }
}
