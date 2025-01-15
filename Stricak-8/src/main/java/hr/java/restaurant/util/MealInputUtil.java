package hr.java.restaurant.util;

import hr.java.restaurant.enumeration.MealTypeEnum;
import hr.java.restaurant.model.*;
import hr.java.restaurant.repository.MealRepository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MealInputUtil {
    private static final MealRepository<Meal> mealRepository = new MealRepository<>();

    public static Meal createMeal(MealTypeEnum mealType, String name,
                                  Category category, BigDecimal price,
                                  Set<Ingredient> selectedIngredients,
                                  boolean additionalAttribute1,
                                  boolean additionalAttribute2,
                                  String additionalAttribute3) {

        return switch (mealType) {
            case MEAT -> new MeatMeal.Builder(mealRepository.getNextId(), name)
                    .setMealType(mealType)
                    .setCategory(category)
                    .setPrice(price)
                    .setIngredients(new HashSet<>(selectedIngredients))
                    .setOrganic(additionalAttribute1)
                    .setEcoFriendly(additionalAttribute2)
                    .setMeatType(additionalAttribute3)
                    .build();
            case VEGAN -> new VeganMeal.Builder(mealRepository.getNextId(), name)
                    .setMealType(mealType)
                    .setCategory(category)
                    .setPrice(price)
                    .setIngredients(new HashSet<>(selectedIngredients))
                    .setGlutenFree(additionalAttribute1)
                    .setOrganic(additionalAttribute2)
                    .setProteinSource(additionalAttribute3)
                    .build();
            case VEGETARIAN -> new VegetarianMeal.Builder(mealRepository.getNextId(), name)
                    .setMealType(mealType)
                    .setCategory(category)
                    .setPrice(price)
                    .setIngredients(new HashSet<>(selectedIngredients))
                    .setContainsDairy(additionalAttribute1)
                    .setContainsEggs(additionalAttribute2)
                    .setProteinSource(additionalAttribute3)
                    .build();
            default -> throw new IllegalArgumentException("Unsupported meal type: " + mealType);
        };
    }
}
