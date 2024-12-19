package hr.java.service;

import hr.java.restaurant.model.Category;
import hr.java.restaurant.model.Ingredient;
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
                if (index == selectedCategoryIndex) {
                    return category;
                }
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
     * Finds a category by id.
     * @param categoryId the category id
     * @param categories the categories
     * @return the category
     */
    public static Category categoryById(Long categoryId, Set<Category> categories) {
        for (Category category : categories) {
            if (category.getId().equals(categoryId)) {
                return category;
            }
        }

        return null;
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
        System.out.println("ingredientNameInput " + ingredientNameInput);
        Integer selectedIngredientIndex = Ingredient.existsByName(ingredients, ingredientNameInput);

        if (selectedIngredientIndex != -1) {
            int index = 0;
            for (Ingredient ingredient : ingredients) {
                if (index == selectedIngredientIndex) {
                    return ingredient;
                }
                index++;
            }
            return null;
        } else {
            logger.warn("Entered invalid ingredient name.");
            System.out.println("Uneseni sastojak ne postoji.");
            return ingredientName(scanner, message, ingredients);
        }
    }
}
