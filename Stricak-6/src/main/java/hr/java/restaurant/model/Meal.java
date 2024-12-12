package hr.java.restaurant.model;

import hr.java.restaurant.repository.MealRepository;
import hr.java.service.Constants;
import hr.java.service.Output;
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

    private String name;
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
        super(id);
        this.name = name;
        this.mealType = mealType;
        this.category = category;
        this.ingredients = ingredients;
        this.price = price;
    }

    public static BigDecimal getUnrealPrice() {
        return unrealPrice;
    }

    public static Set<Meal> inputMealSet(List<Category> categories, Set<Ingredient> ingredients, Scanner scanner) {
        Set<Meal> meals = new HashSet<>();

        System.out.println("Unos veganskih jela: ");
        for (int i = 0; i < Constants.NUM_OF_VEGAN_MEALS; i++) {
            meals.add(VeganMeal.inputVeganMeal(categories, ingredients, meals, scanner));
        }

        System.out.println("Unos vegetarijanskih jela: ");
        for (int i = 0; i < Constants.NUM_OF_VEGETARIAN_MEALS; i++) {
            meals.add(VegetarianMeal.inputVegetarianMeal(categories, ingredients, meals, scanner));
        }

        System.out.println("Unos mesnih jela: ");
        for (int i = 0; i < Constants.NUM_OF_MEAT_MEALS; i++) {
            meals.add(MeatMeal.inputMeatMeal(categories, ingredients, meals, scanner));
        }

        return meals;
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

    /**
     * Finds the most caloric meal
     * @param meals the meals
     * @return the most caloric meal
     */
    public static Meal findMostCaloricMeal(List<Meal> meals) {
        Meal mostCaloricMeal = meals.getFirst();
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
    public static Meal findLeastCaloricMeal(List<Meal> meals) {
        Meal mostCaloricMeal = meals.getFirst();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return Objects.equals(name, meal.name) && Objects.equals(category, meal.category) && Objects.equals(ingredients, meal.ingredients) && Objects.equals(price, meal.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, ingredients, price);
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


        List<Ingredient> sortedIngredients = new ArrayList<>(this.ingredients);
        sortedIngredients.sort((i1, i2) -> i1.getName().compareTo(i2.getName()));
        for (Ingredient ingredient : sortedIngredients) {
            Output.tabulatorPrint(tabulators + 1);
            System.out.println("Sastojak " + index + ":");
            ingredient.print(tabulators + 2);
            index++;
        }
    }
}