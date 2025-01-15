package hr.java.restaurant.model;

import hr.java.restaurant.repository.MealRepository;
import hr.java.restaurant.util.Constants;
import hr.java.restaurant.util.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Represents a meal in a restaurant.
 */
public class Meal extends Entity implements Serializable {
    private static final BigDecimal unrealPrice = new BigDecimal(500);
    private static final Logger logger = LoggerFactory.getLogger(Meal.class);

    private static Long counter = 0L;

    private String mealType;
    private final Category category;
    private Set<Ingredient> ingredients;
    private final BigDecimal price;

    private final static MealRepository<Meal> mealRepository = new MealRepository<>();

    /**
     * Constructs a Meal object using the provided builder.
     *
     * @param name the name of the meal
     * @param category the category of the meal
     * @param ingredients the ingredients.txt of the meal
     * @param price the price of the meal
     */
    public Meal(Long id, String name, String mealType, Category category, Set<Ingredient> ingredients, BigDecimal price) {
        super(id, name);
        this.mealType = mealType;
        this.category = category;
        this.ingredients = ingredients;
        this.price = price;
    }

    public static BigDecimal getUnrealPrice() {
        return unrealPrice;
    }

    public static Set<Meal> getMealByIdentifiers(String mealsInRestaurantIdentifiers) {
        Set<Meal> mealsList = new HashSet<>();
        String[] identifiers = mealsInRestaurantIdentifiers.split(",");
        for (String identifier : identifiers) {
            mealsList.add(mealRepository.findById(Long.parseLong(identifier)));
        }

        return mealsList;
    }

    public String getIngredientsIdentifiers() {
        StringBuilder ingredientIdentifiers = new StringBuilder();
        for (Ingredient ingredient : ingredients) {
            ingredientIdentifiers.append(ingredient.getId()).append(",");
        }

        ingredientIdentifiers.deleteCharAt(ingredientIdentifiers.length() - 1);

        return ingredientIdentifiers.toString();
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Checks if the meal with the provided name already exists in the array of meals.
     * @param meals the meals
     * @param mealName the name of the meal
     * @return the index of the meal if it exists, -1 otherwise
     */
    public static Integer existsByName(Set<Meal> meals, String mealName) {
        int i=0;
        for (Meal meal : meals) {
            if (mealName.equals(meal.getName())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * Add meal names to a String array.
     * @param meals the meals
     * @return the meal names
     */
    public static List<String> mealNameArray(Set<Meal> meals) {
        List<String> mealNames = new ArrayList<>();

        for (Meal meal : meals) {
            mealNames.add(meal.getName());
        }

        return mealNames;
    }

    /**
     * Calculates the total calories of the meal.
     * @return the total calories of the meal
     */
    public BigDecimal getTotalCalories() {
        BigDecimal mealCalories = BigDecimal.valueOf(0);

        for (Ingredient ingredient : this.getIngredients()) {
            mealCalories = mealCalories.add(ingredient.getKcal());
        }

        return  mealCalories;
    }

    public String getMealType() {
        return mealType;
    }

    public Category getCategory() {
        return category;
    }

}