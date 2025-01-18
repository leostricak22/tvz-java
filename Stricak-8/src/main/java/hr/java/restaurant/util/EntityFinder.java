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

    public static String getMealIdentifiers(Set<Meal> meals) {
        StringBuilder mealIdentifiers = new StringBuilder();
        for (Meal meal : meals) {
            mealIdentifiers.append(meal.getId()).append(",");
        }

        mealIdentifiers.deleteCharAt(mealIdentifiers.length() - 1);

        return mealIdentifiers.toString();
    }


    public static <T extends Person> String getPersonIdentifiers(Set<T> people) {
        StringBuilder personIdentifiers = new StringBuilder();
        for (Person person : people) {
            personIdentifiers.append(person.getId()).append(",");
        }

        personIdentifiers.deleteCharAt(personIdentifiers.length() - 1);

        return personIdentifiers.toString();
    }

    public static List<String> getFilesFromPaths(String filesPaths) {
        List<String> files = new ArrayList<>();
        String[] paths = filesPaths.split(",");
        for (String path : paths) {
            files.add(path);
        }

        return files;
    }

    public static String getFilesPaths(List<String> files) {
        StringBuilder filesPaths = new StringBuilder();
        for (String file : files) {
            filesPaths.append(file).append(",");
        }

        System.out.println(filesPaths);
        if (!filesPaths.isEmpty())
            filesPaths.deleteCharAt(filesPaths.length() - 1);

        return filesPaths.toString();
    }
}