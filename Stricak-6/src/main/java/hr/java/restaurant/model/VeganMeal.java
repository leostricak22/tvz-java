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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Represents a meal that is vegan.
 */
public final class VeganMeal extends Meal implements Vegan {
    private final String proteinSource;
    private final boolean organic;
    private final boolean glutenFree;
    private static final Logger logger = LoggerFactory.getLogger(VeganMeal.class);

    /**
     * Constructs a VeganMeal object.
     * @param name the name of the meal
     * @param category the category of the meal
     * @param ingredients the ingredients.txt of the meal
     * @param price the price of the meal
     * @param proteinSource the source of protein
     * @param organic if the meal is organic
     * @param glutenFree if the meal is gluten-free
     */
    public VeganMeal(String name, Category category, Set<Ingredient> ingredients, BigDecimal price, String proteinSource, boolean organic, boolean glutenFree) {
        super(name, category, ingredients, price);
        this.proteinSource = proteinSource;
        this.organic = organic;
        this.glutenFree = glutenFree;
    }

    public static List<Meal> readVeganMealFromFile() {
        List<Meal> veganMeals = new ArrayList<>();
        int linesRead = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.FILENAME_MEALS))) {
            String line;
            while ((line = reader.readLine()) != null) {
                linesRead++;
                if (linesRead > 24) {
                    break;
                }

                int id = Integer.parseInt(line.trim());

                String name = reader.readLine().trim();
                long categoryId = Long.parseLong(reader.readLine().trim());
                String ingredientsIdentifiers = reader.readLine().trim();
                BigDecimal price = new BigDecimal(reader.readLine().trim());
                String proteinSource = reader.readLine().trim();
                boolean organic = Boolean.parseBoolean(reader.readLine().trim());
                boolean glutenFree = Boolean.parseBoolean(reader.readLine().trim());

                Category category = EntityFinder.categoryById(categoryId, Category.readCategoryFromFile());
                Set<Ingredient> ingredients = Ingredient.getIngredientsByIdentifiers(ingredientsIdentifiers, Ingredient.readIngredientFromFile());

                veganMeals.add(new VeganMeal(name, category, ingredients, price, proteinSource, organic, glutenFree));
            }
        } catch (IOException e) {
            System.err.println("Greška pri čitanju datoteke: " + e.getMessage());
        }

        return veganMeals;
    }

    @Override
    public boolean isOrganic() {
        return this.organic;
    }

    @Override
    public boolean isGlutenFree() {
        return this.glutenFree;
    }

    /**
     * Inputs a vegan meal.
     * @param categories the categories
     * @param ingredients the ingredients.txt
     * @param meals the meals
     * @param scanner the scanner object used for input
     * @return the vegan meal
     */
    public static VeganMeal inputVeganMeal(List<Category> categories, Set<Ingredient> ingredients, Set<Meal> meals, Scanner scanner) {
        String mealName;

        logger.info("Vegan meal input");
        while (true) {
            mealName = Input.string(scanner, "Unesite naziv veganskog jela: ");
            try {
                Validation.checkDuplicateMeal(meals, mealName);
                break;
            } catch (DuplicateEntryException e) {
                System.out.println("Jelo s tim nazivom već postoji!");
            }
        }

        Category mealCategory = EntityFinder.categoryName(scanner, "Unesite kategoriju veganskog jela: ", categories);

        BigDecimal mealPrice;
        Set<Ingredient> ingredientsEntered = Ingredient.enterArrayOfIngredients(ingredients, scanner);
        while (true) {
            mealPrice = Input.bigDecimal(scanner, "Unesite cijenu veganskog jela: ");

            try {
                Validation.checkMealPrice(mealPrice);
                break;
            } catch (InvalidValueException e) {
                System.out.println("Cijena mora biti veća od 0 i treba biti realna. Pokušajte ponovno:");
            }
        }
        String mealProteinSource = Input.string(scanner, "Unesite izvor proteina veganskog jela: ");
        boolean mealOrganic = Input.booleanValue(scanner, "Unesite je li vegansko jelo organsko: ");
        boolean mealGlutenFree = Input.booleanValue(scanner, "Unesite je li vegansko jelo bez glutena: ");

        return new VeganMeal(mealName, mealCategory, ingredientsEntered, mealPrice, mealProteinSource, mealOrganic, mealGlutenFree);
    }

    /**
     * Prints the vegan meal.
     * @param tabulators the number of tabulators
     */
    @Override
    public void print(Integer tabulators) {
        logger.info("Printing vegan meal.");

        super.print(tabulators);
        Output.tabulatorPrint(tabulators);
        System.out.println("Izvor proteina: " + this.proteinSource);
        Output.tabulatorPrint(tabulators);
        System.out.println("Bez glutena: " + (this.glutenFree ? "Da" : "Ne"));
        Output.tabulatorPrint(tabulators);
        System.out.println("Organsko: " + (this.organic ? "Da" : "Ne"));
    }
}
