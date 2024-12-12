package hr.java.restaurant.model;

import hr.java.restaurant.exception.DuplicateEntryException;
import hr.java.restaurant.exception.InvalidValueException;
import hr.java.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Represents a meal that contains meat.
 */
public final class MeatMeal extends Meal implements Meat {
    private final String meatType;
    private final String meatOrigin;
    private final String meatCookingType;
    private static final Logger logger = LoggerFactory.getLogger(MeatMeal.class);

    /**
     * Constructs a MeatMeal object.
     *
     * @param name the name of the meal
     * @param category the category of the meal
     * @param ingredients the ingredients.txt of the meal
     * @param price the price of the meal
     * @param meatType the type of meat
     * @param meatOrigin the origin of meat
     * @param meatCookingType the cooking type of meat
     */
    public MeatMeal(String name, Category category, Set<Ingredient> ingredients, BigDecimal price, String meatType, String meatOrigin, String meatCookingType) {
        super(name, category, ingredients, price);
        this.meatType = meatType;
        this.meatOrigin = meatOrigin;
        this.meatCookingType = meatCookingType;
    }

    public static List<Meal> readMeatMealFromFile() {
        List<Meal> meatMeals = new ArrayList<>();
        int linesRead = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.FILENAME_MEALS))) {
            String line;
            while ((line = reader.readLine()) != null) {
                linesRead++;
                if (linesRead < 49) {
                    break;
                }

                int id = Integer.parseInt(line.trim());
                String name = reader.readLine().trim();
                long categoryId = Long.parseLong(reader.readLine().trim());
                String ingredientsIdentifiers = reader.readLine().trim();
                BigDecimal price = new BigDecimal(reader.readLine().trim());
                String meatType = reader.readLine().trim();
                String meatOrigin = reader.readLine().trim();
                String meatCookingType = reader.readLine().trim();

                Category category = EntityFinder.categoryById(categoryId, Category.readCategoryFromFile());
                Set<Ingredient> ingredients = Ingredient.getIngredientsByIdentifiers(ingredientsIdentifiers, Ingredient.readIngredientFromFile());

                meatMeals.add(new MeatMeal(name, category, ingredients, price, meatType, meatOrigin, meatCookingType));
            }
        } catch (IOException e) {
            logger.error("Error reading from file.");
            System.out.println("Pogreška prilikom čitanja iz datoteke.");
        }
        return meatMeals;
    }

    @Override
    public String getMeatType() {
        return this.meatType;
    }

    @Override
    public String getMeatOrigin() {
        return this.meatOrigin;
    }

    /**
     * Inputs a meat meal.
     * @param categories the categories
     * @param ingredients the ingredients.txt
     * @param meals the meals
     * @param scanner the scanner object used for input
     * @return the meat meal
     */
    public static MeatMeal inputMeatMeal(List<Category> categories, Set<Ingredient> ingredients, Set<Meal> meals, Scanner scanner) {
        logger.info("Meat meal input");

        String mealName;

        while (true) {
            mealName = Input.string(scanner, "Unesite naziv mesnog jela: ");
            try {
                Validation.checkDuplicateMeal(meals, mealName);
                break;
            } catch (DuplicateEntryException e) {
                logger.error("Duplicate meal entry");
                System.out.println("Jelo s tim nazivom već postoji!");
            }
        }

        Category mealCategory = EntityFinder.categoryName(scanner, "Unesite kategoriju mesnog jela: ", categories);
        Set<Ingredient> ingredientsEntered = Ingredient.enterArrayOfIngredients(ingredients, scanner);

        BigDecimal mealPrice;
        while (true) {
            mealPrice = Input.bigDecimal(scanner, "Unesite cijenu mesnog jela: ");

            try {
                Validation.checkMealPrice(mealPrice);
                break;
            } catch (InvalidValueException e) {
                logger.error("Invalid meal price");
                System.out.println("Cijena mora biti veća od 0 i treba biti realna. Pokušajte ponovno:");
            }
        }

        String meatType = Input.string(scanner, "Unesite tip mesa: ");
        String meatOrigin = Input.string(scanner, "Unesite podrijetlo mesa: ");
        String meatCookingType = Input.string(scanner, "Unesite način pripreme mesa: ");

        return new MeatMeal(mealName, mealCategory, ingredientsEntered, mealPrice, meatType, meatOrigin, meatCookingType);
    }

    /**
     * Prints a meat meal.
     * @param tabulators the number of tabulators
     */
    @Override
    public void print(Integer tabulators) {
        logger.info("Printing meat meal.");
        super.print(tabulators);
        Output.tabulatorPrint(tabulators);
        System.out.println("Tip mesa: " + this.meatType);
        Output.tabulatorPrint(tabulators);
        System.out.println("Podrijetlo mesa: " + this.meatOrigin);
        Output.tabulatorPrint(tabulators);
        System.out.println("Način kuhanja: " + this.meatCookingType);
    }
}
