package hr.java.service;

import hr.java.restaurant.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Provides methods for finding entities.
 */
public class EntityFinder {
    private static final Logger logger = LoggerFactory.getLogger(EntityFinder.class);

    /**
     * Finds a category by name.
     * @param scanner the scanner object used for input
     * @param message the message to be displayed to the user
     * @param categories the categories
     * @return the category
     */
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

    /**
     * Finds an ingredient by name.
     * @param scanner the scanner object used for input
     * @param message the message to be displayed to the user
     * @param ingredients the ingredients
     * @return the ingredient
     */
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

    /**
     * Finds a meal by name.
     * @param scanner the scanner object used for input
     * @param message the message to be displayed to the user
     * @param meals the meals
     * @return the meal
     */
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

    /**
     * Finds a chef by name.
     * @param scanner the scanner object used for input
     * @param message the message to be displayed to the user
     * @param chefs the chefs
     * @return the chef
     */
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

    /**
     * Finds a waiter by name.
     * @param scanner the scanner object used for input
     * @param message the message to be displayed to the user
     * @param waiters the waiters
     * @return the waiter
     */
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

    /**
     * Finds a deliverer by name.
     * @param scanner the scanner object used for input
     * @param message the message to be displayed to the user
     * @param deliverers the deliverers
     * @return the deliverer
     */
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

    /**
     * Finds a restaurant by name.
     * @param scanner the scanner object used for input
     * @param message the message to be displayed to the user
     * @param restaurants the restaurants
     * @return the restaurant
     */
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
}
