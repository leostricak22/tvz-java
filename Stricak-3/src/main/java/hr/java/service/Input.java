package hr.java.service;

import hr.java.restaurant.exception.InvalidValueException;
import hr.java.restaurant.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Input {
    private static final Logger logger = LoggerFactory.getLogger(Input.class);

    public static Integer integer(Scanner scanner, String message) {
        logger.info("Integer input.");

        while (true) {
            System.out.println(message);
            try {
                int input = scanner.nextInt();
                scanner.nextLine();

                if (input <= 0)
                    throw new InputMismatchException();

                return input;
            } catch (InputMismatchException e) {
                logger.error("Entered invalid Integer value.");
                System.out.println("Uneseni broj je neispravan. Pokušajte ponovno:");
                scanner.next();
            }
        }
    }

    public static BigDecimal bigDecimal(Scanner scanner, String message) {
        logger.info("BigDecimal input.");

        while (true) {
            System.out.println(message);
            try {
                BigDecimal input = scanner.nextBigDecimal();
                scanner.nextLine();

                return input;
            } catch (InputMismatchException e) {
                logger.error("Entered invalid BigDecimal value.");
                System.out.println("Uneseni broj je neispravan. Pokušajte ponovno:");
                scanner.nextLine();
            }
        }
    }

    public static String string(Scanner scanner, String message) {
        logger.info("String input.");

        System.out.println(message);
        String stringInputValue = scanner.nextLine();

        if (!stringInputValue.isEmpty()) {
            return stringInputValue;
        } else {
            logger.warn("Entered empty string.");
            System.out.println("Niste ništa unijeli.");
            return string(scanner, message);
        }
    }

    public static String postalCode(Scanner scanner, String message) {
        logger.info("Postal code input.");

        System.out.println(message);
        String postalCodeInputValue = scanner.nextLine();

        if (Validation.validPostalCode(postalCodeInputValue)) {
            return postalCodeInputValue;
        } else {
            logger.warn("Entered invalid postal code.");
            System.out.println("Unijeli ste pogrešan poštanski broj.");
            return postalCode(scanner, message);
        }
    }

    public static LocalDateTime localDateTime(Scanner scanner, String message) {
        logger.info("LocalDateTime input.");

        System.out.println(message);
        String localDateTimeInputValue = scanner.nextLine();

        if(Validation.validLocalDateTime(localDateTimeInputValue)) {
            return LocalDateTime.parse(localDateTimeInputValue);
        } else {
            logger.warn("Entered invalid LocalDateTime value.");
            System.out.println("Unijeli ste pogrešan datum.");
            return localDateTime(scanner, message);
        }
    }

    public static LocalDate localDate(Scanner scanner, String message) {
        logger.info("LocalDate input.");

        System.out.println(message);
        String localDateInputValue = scanner.nextLine();

        if(Validation.validLocalDate(localDateInputValue)) {
            return LocalDate.parse(localDateInputValue);
        } else {
            logger.warn("Entered invalid LocalDate value.");
            System.out.println("Unijeli ste pogrešan datum.");
            return localDate(scanner, message);
        }
    }

    public static Category categoryName(Scanner scanner, String message, Category[] categories) {
        logger.info("Category name input.");
        String categoryNameInput = Input.string(scanner, message + Output.objectNameOptions(Category.categoryNameArray(categories)));
        Integer selectedCategoryIndex = Category.existsByName(categories, categoryNameInput);

        if (selectedCategoryIndex != -1) {
            return categories[selectedCategoryIndex];
        } else {
            logger.warn("Entered invalid category name.");
            System.out.println("Unesena kategorija ne postoji.");
            return categoryName(scanner, message, categories);
        }
    }

    public static Ingredient ingredientName(Scanner scanner, String message, Ingredient[] ingredients) {
        logger.info("Ingredient name input.");

        String ingredientNameInput = Input.string(scanner, message  + Output.objectNameOptions(Ingredient.ingredientNameArray(ingredients)));
        Integer selectedIngredientIndex = Ingredient.existsByName(ingredients, ingredientNameInput);

        if (selectedIngredientIndex != -1) {
            return ingredients[selectedIngredientIndex];
        } else {
            logger.warn("Entered invalid ingredient name.");
            System.out.println("Uneseni sastojak ne postoji.");
            return ingredientName(scanner, message, ingredients);
        }
    }

    public static Meal mealName(Scanner scanner, String message, Meal[] meals) {
        logger.info("Find if meal exists by name.");

        String mealNameInput = Input.string(scanner, message + Output.objectNameOptions(Meal.mealNameArray(meals)));
        Integer selectedMealIndex = Meal.existsByName(meals, mealNameInput);

        if (selectedMealIndex != -1) {
            return meals[selectedMealIndex];
        } else {
            logger.warn("Meal doesn't exist.");
            System.out.println("Uneseno jelo ne postoji.");
            return mealName(scanner, message, meals);
        }
    }

    public static Chef chefName(Scanner scanner, String message, Chef[] chefs) {
        logger.info("Find if chef exists by name.");

        String chefFirstAndLastNameInput = Input.string(scanner, message + Output.objectNameOptions(Chef.chefNameArray(chefs)));
        String[] chefNameInput = chefFirstAndLastNameInput.split(" ");

        if (chefNameInput.length > 1) {
            Integer selectedChefIndex = Chef.existsByName(chefs, chefNameInput[0], chefNameInput[1]);

            if(selectedChefIndex != -1) {
                return chefs[selectedChefIndex];
            }
        }

        logger.warn("Chef doesn't exist.");
        System.out.println("Unesena osoba ne postoji.");
        return chefName(scanner, message, chefs);
    }

    public static Waiter waiterName(Scanner scanner, String message, Waiter[] waiters) {
        logger.info("Find if waiter exists by name.");

        String waiterFirstAndLastNameInput = Input.string(scanner, message  + Output.objectNameOptions(Waiter.waiterNameArray(waiters)));
        String[] waiterNameInput = waiterFirstAndLastNameInput.split(" ");

        if (waiterNameInput.length > 1) {
            Integer selectedWaiterIndex = Waiter.existsByName(waiters, waiterNameInput[0], waiterNameInput[1]);

            if(selectedWaiterIndex != -1) {
                return waiters[selectedWaiterIndex];
            }
        }

        logger.warn("Waiter doesn't exist.");
        System.out.println("Unesena osoba ne postoji.");
        return waiterName(scanner, message, waiters);
    }

    public static Deliverer delivererName(Scanner scanner, String message, Deliverer[] deliverers) {
        logger.info("Find if deliverer exists by name.");
        String delivererFirstAndLastNameInput = Input.string(scanner, message  + Output.objectNameOptions(Deliverer.delivererNameArray(deliverers)));
        String[] delivererNameInput = delivererFirstAndLastNameInput.split(" ");

        if (delivererNameInput.length > 1) {
            Integer selectedDelivererIndex = Deliverer.existsByName(deliverers, delivererNameInput[0], delivererNameInput[1]);

            if(selectedDelivererIndex != -1) {
                return deliverers[selectedDelivererIndex];
            }
        }

        logger.warn("Deliverer doesn't exist.");
        System.out.println("Unesena osoba ne postoji.");
        return delivererName(scanner, message, deliverers);
    }

    public static Restaurant restaurantName(Scanner scanner, String message, Restaurant[] restaurants) {
        logger.info("Find if restaurant exists by name.");
        String restaurantNameInput = Input.string(scanner, message  + Output.objectNameOptions(Restaurant.restaurantNameArray(restaurants)));
        Integer selectedRestaurantIndex = Restaurant.existsByName(restaurants, restaurantNameInput);

        if(selectedRestaurantIndex != -1) {
            return restaurants[selectedRestaurantIndex];
        }

        logger.warn("Restaurant doesn't exist.");
        System.out.println("Uneseni restoran ne postoji.");
        return restaurantName(scanner, message, restaurants);
    }

    public static Contract contract(Scanner scanner, String message) {
        logger.info("Contract input.");
        BigDecimal salary;

        while (true) {
            salary = Input.bigDecimal(scanner, "Unesite plaću.");

            try {
                Validation.checkSalary(salary);
                break;
            } catch (InvalidValueException e) {
                logger.error("Entered invalid salary value.");
                System.out.println("Unesena plaća je neispravna. Plaća mora biti veća od minimalne (" + Contract.getMinSalary() + ").");
            }
        }

        LocalDate startDate = Input.localDate(scanner, "Unesite početak ugovora.");
        LocalDate endDate = Input.localDate(scanner, "Unesite kraj ugovora.");
        String contractType = Input.string(scanner, "Unesite tip ugovora.");

        return new Contract(salary, startDate, endDate, contractType);
    }

    public static boolean booleanValue(Scanner scanner, String message) {
        logger.info("Boolean input.");
        System.out.println(message);
        String booleanInputValue = Input.string(scanner, "Unesite 'da' ili 'ne'.");

        if (booleanInputValue.equals("da")) {
            return true;
        } else if (booleanInputValue.equals("ne")) {
            return false;
        } else {
            logger.warn("Entered invalid boolean value.");
            return booleanValue(scanner, message);
        }
    }
}