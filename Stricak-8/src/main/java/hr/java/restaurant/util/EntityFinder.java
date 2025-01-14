package hr.java.restaurant.util;

import hr.java.restaurant.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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
    public static Category categoryName(Scanner scanner, String message, List<Category> categories) {
        logger.info("Category name input.");
        String categoryNameInput = Input.string(scanner, message + Output.objectNameOptions(Category.categoryNameArray(categories)));
        Integer selectedCategoryIndex = Category.existsByName(categories, categoryNameInput);

        if (selectedCategoryIndex != -1) {
            int index = 0;
            for (Category category : categories) {
                if (index == selectedCategoryIndex)
                    return category;
                index++;
            }
            return null;
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
     * @param ingredients the ingredients.txt
     * @return the ingredient
     */
    public static Ingredient ingredientName(Scanner scanner, String message, Set<Ingredient> ingredients) {
        logger.info("Ingredient name input.");

        String ingredientNameInput = Input.string(scanner, message  + Output.objectNameOptions(Ingredient.ingredientNameArray(ingredients)));
        Integer selectedIngredientIndex = Ingredient.existsByName(ingredients, ingredientNameInput);

        if (selectedIngredientIndex != -1) {
            int index = 0;
            for (Ingredient ingredient : ingredients) {
                if (index == selectedIngredientIndex)
                    return ingredient;
                index++;
            }
            return null;
        } else {
            logger.warn("Entered invalid ingredient name.");
            System.out.println("Uneseni sastojak ne postoji.");
            return ingredientName(scanner, message, ingredients);
        }
    }

    public static Meal mealName(Scanner scanner, String message, Set<Meal> meals) {
        logger.info("Find if meal exists by name.");

        String mealNameInput = Input.string(scanner, message + Output.objectNameOptions(Meal.mealNameArray(meals)));
        Integer selectedMealIndex = Meal.existsByName(meals, mealNameInput);

        if (selectedMealIndex != -1) {
            int index = 0;
            for (Meal meal : meals) {
                if (index == selectedMealIndex)
                    return meal;
                index++;
            }
            return null;
        } else {
            logger.warn("Meal doesn't exist.");
            System.out.println("Uneseno jelo ne postoji.");
            return mealName(scanner, message, meals);
        }
    }

    public static Chef chefName(Scanner scanner, String message, Set<Chef> chefs) {
        logger.info("Find if chef exists by name.");

        String chefFirstAndLastNameInput = Input.string(scanner, message + Output.objectNameOptions(Chef.chefNameArray(chefs)));
        List<String> chefNameInput = Arrays.asList(chefFirstAndLastNameInput.split(" "));

        if (chefNameInput.size() > 1) {
            Integer selectedChefIndex = Chef.existsByName(chefs, chefNameInput.get(0), chefNameInput.get(1));

            if(selectedChefIndex != -1) {
                int index = 0;
                for (Chef chef : chefs) {
                    if (index == selectedChefIndex)
                        return chef;
                    index++;
                }
                return null;
            }
        }

        logger.warn("Chef doesn't exist.");
        System.out.println("Unesena osoba ne postoji.");
        return chefName(scanner, message, chefs);
    }

    public static Waiter waiterName(Scanner scanner, String message, Set<Waiter> waiters) {
        logger.info("Find if waiter exists by name.");

        String waiterFirstAndLastNameInput = Input.string(scanner, message  + Output.objectNameOptions(Waiter.waiterNameArray(waiters)));
        List<String> waiterNameInput = Arrays.asList(waiterFirstAndLastNameInput.split(" "));

        if (waiterNameInput.size() > 1) {
            Integer selectedWaiterIndex = Waiter.existsByName(waiters, waiterNameInput.get(0), waiterNameInput.get(1));

            if(selectedWaiterIndex != -1) {
                int index = 0;
                for (Waiter waiter : waiters) {
                    if (index == selectedWaiterIndex)
                        return waiter;
                    index++;
                }
                return null;
            }
        }

        logger.warn("Waiter doesn't exist.");
        System.out.println("Unesena osoba ne postoji.");
        return waiterName(scanner, message, waiters);
    }

    public static Deliverer delivererName(Scanner scanner, String message, Set<Deliverer> deliverers) {
        logger.info("Find if deliverer exists by name.");
        String delivererFirstAndLastNameInput = Input.string(scanner, message  + Output.objectNameOptions(Deliverer.delivererNameArray(deliverers)));
        List<String> delivererNameInput = Arrays.asList(delivererFirstAndLastNameInput.split(" "));


        if (delivererNameInput.size() > 1) {
            Integer selectedDelivererIndex = Deliverer.existsByName(deliverers, delivererNameInput.get(0), delivererNameInput.get(1));

            if(selectedDelivererIndex != -1) {
                int index = 0;
                for (Deliverer deliverer : deliverers) {
                    if (index == selectedDelivererIndex)
                        return deliverer;
                    index++;
                }
                return null;
            }
        }

        logger.warn("Deliverer doesn't exist.");
        System.out.println("Unesena osoba ne postoji.");
        return delivererName(scanner, message, deliverers);
    }

    public static Restaurant restaurantName(Scanner scanner, String message, List<Restaurant> restaurants) {
        logger.info("Find if restaurant exists by name.");
        String restaurantNameInput = Input.string(scanner, message  + Output.objectNameOptions(Restaurant.restaurantNameArray(restaurants)));
        Integer selectedRestaurantIndex = Restaurant.existsByName(restaurants, restaurantNameInput);

        if(selectedRestaurantIndex != -1) {
            int index = 0;
            for (Restaurant restaurant : restaurants) {
                if (index == selectedRestaurantIndex)
                    return restaurant;
                index++;
            }
            return null;
        }

        logger.warn("Restaurant doesn't exist.");
        System.out.println("Uneseni restoran ne postoji.");
        return restaurantName(scanner, message, restaurants);
    }

    public static Category categoryById(Long categoryId, Set<Category> categories) {
        for (Category category : categories)
            if (category.getId().equals(categoryId))
                return category;

        return null;
    }
}