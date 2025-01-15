package hr.java.restaurant.util;

import hr.java.restaurant.model.*;
import hr.java.restaurant.repository.IngredientRepository;
import hr.java.restaurant.repository.MealRepository;
import hr.java.restaurant.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Provides methods for finding entities.
 */
public class EntityFinder {
    private static final Logger logger = LoggerFactory.getLogger(EntityFinder.class);

    private static final IngredientRepository<Ingredient> ingredientRepository = new IngredientRepository<>();
    private static final RestaurantRepository<Restaurant> restaurantRepository = new RestaurantRepository<>();
    private static final MealRepository<Meal> mealRepository = new MealRepository<>();

    public static Optional<Category> categoryById(Long categoryId, Set<Category> categories) {
        for (Category category : categories)
            if (category.getId().equals(categoryId))
                return Optional.of(category);

        return Optional.empty();
    }

    public static Set<Ingredient> ingredientsByIdentifiers(String ingredientsIdentifiers) {
        Set<Ingredient> ingredientsList = new HashSet<>();
        String[] identifiers = ingredientsIdentifiers.split(",");
        for (String identifier : identifiers) {
            ingredientsList.add(ingredientRepository.findById(Long.parseLong(identifier)));
        }

        return ingredientsList;
    }

    public static Set<Restaurant> restaurantsByIdentifiers(String restaurantIdentifier) {
        Set<Restaurant> restaurantList = new HashSet<>();
        String[] identifiers = restaurantIdentifier.split(",");
        for (String identifier : identifiers) {
            restaurantList.add(restaurantRepository.findById(Long.parseLong(identifier)));
        }

        return restaurantList;
    }

    public static Set<Meal> getMealByIdentifiers(String mealsInRestaurantIdentifiers) {
        Set<Meal> mealsList = new HashSet<>();
        String[] identifiers = mealsInRestaurantIdentifiers.split(",");
        for (String identifier : identifiers) {
            mealsList.add(mealRepository.findById(Long.parseLong(identifier)));
        }

        return mealsList;
    }

    public static String getIngredientsIdentifiers(List<Ingredient> ingredients) {
        StringBuilder ingredientIdentifiers = new StringBuilder();
        for (Ingredient ingredient : ingredients) {
            ingredientIdentifiers.append(ingredient.getId()).append(",");
        }

        ingredientIdentifiers.deleteCharAt(ingredientIdentifiers.length() - 1);

        return ingredientIdentifiers.toString();
    }

}