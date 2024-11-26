package hr.java.restaurant.model;

import hr.java.service.Input;
import hr.java.service.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Represents a meal in a restaurant.
 */
public class Meal extends Entity {
    private static final BigDecimal unrealPrice = new BigDecimal(500);
    private static final Logger logger = LoggerFactory.getLogger(Meal.class);

    private static Long counter = 0L;

    private String name;
    private final Category category;
    private final Set<Ingredient> ingredients;
    private final BigDecimal price;

    /**
     * Constructs a Meal object using the provided builder.
     *
     * @param name the name of the meal
     * @param category the category of the meal
     * @param ingredients the ingredients of the meal
     * @param price the price of the meal
     */
    public Meal(String name, Category category, Set<Ingredient> ingredients, BigDecimal price) {
        super(++counter);
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
        this.price = price;
    }

    public static BigDecimal getUnrealPrice() {
        return unrealPrice;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public static Integer existsByName(Meal[] meals, String mealName) {
        for (int j=0;j<meals.length;j++) {
            if (mealName.equals(meals[j].getName())) {
                return j;
            }
        }
        return -1;
    }

    /**
     * Add meal names to a String array.
     * @param meals the meals
     * @return the meal names
     */
    public static String[] mealNameArray(Meal[] meals) {
        String[] mealNames = new String[meals.length];

        for (int i = 0; i < meals.length; i++) {
            mealNames[i] = meals[i].getName();
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

    /**
     * Finds the most caloric meal
     * @param meals the meals
     * @return the most caloric meal
     */
    public static Meal findMostCaloricMeal(Meal[] meals) {
        Meal mostCaloricMeal = meals[0];
        BigDecimal mostMealCalories = BigDecimal.valueOf(-1);
        BigDecimal mealCalories;

        for (Meal meal : meals) {
            mealCalories = meal.getTotalCalories();

            if (mealCalories.compareTo(mostMealCalories) > 0) {
                mostCaloricMeal = meal;
                mostMealCalories = mealCalories;
            }
        }

        return mostCaloricMeal;
    }

    /**
     * Finds the least caloric meal.
     * @param meals the meals
     * @return the least caloric meal
     */
    public static Meal findLeastCaloricMeal(Meal[] meals) {
        Meal mostCaloricMeal = meals[0];
        BigDecimal mostMealCalories = BigDecimal.valueOf(Double.MAX_VALUE);
        BigDecimal mealCalories;

        for (Meal meal : meals) {
            mealCalories = meal.getTotalCalories();

            if (mealCalories.compareTo(mostMealCalories) < 0) {
                mostCaloricMeal = meal;
                mostMealCalories = mealCalories;
            }
        }

        return mostCaloricMeal;
    }

    /**
     * print meal
     * @param tabulators number of tabulators
     */
    public void print(Integer tabulators) {
        logger.info("Printing meal.");

        Output.tabulatorPrint(tabulators);
        System.out.println("Id: " + this.getId() + ", Naziv: " + this.name + ", Cijena: " + this.price);

        Output.tabulatorPrint(tabulators);
        System.out.println("Kategorija:");
        category.print(tabulators + 1);

        Output.tabulatorPrint(tabulators);
        System.out.println("Sastojci: ");
        int index = 1;

        for (Ingredient ingredient : ingredients) {
            Output.tabulatorPrint(tabulators + 1);
            System.out.println("Sastojak " + index + ":");
            ingredient.print(tabulators + 2);
            index++;
        }
    }
}