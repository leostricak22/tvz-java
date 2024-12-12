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
 * Represents a meal that is vegetarian.
 */
public final class VegetarianMeal extends Meal implements Vegetarian {
    private final boolean containsDairy;
    private final boolean containsEggs;
    private final String proteinSource;

    private static final Logger logger = LoggerFactory.getLogger(VegetarianMeal.class);

    /**
     * Constructs a VegetarianMeal object.
     * @param name the name of the meal
     * @param category the category of the meal
     * @param ingredients the ingredients.txt of the meal
     * @param price the price of the meal
     * @param proteinSource the source of protein
     * @param containsDairy if the meal contains dairy
     * @param containsEggs if the meal contains eggs
     */
    public VegetarianMeal(String name, Category category, Set<Ingredient> ingredients, BigDecimal price, String proteinSource, boolean containsDairy, boolean containsEggs) {
        super(name, category, ingredients, price);
        this.containsDairy = containsDairy;
        this.containsEggs = containsEggs;
        this.proteinSource = proteinSource;
    }

    public static List<Meal> readVegetarianMealFromFile() {
        List<Meal> vegetarianMeals = new ArrayList<>();

        int linesRead = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.FILENAME_MEALS))) {
            String line;
            while ((line = reader.readLine()) != null) {
                linesRead++;
                if (!(linesRead > 24 && linesRead < 49)) {
                    break;
                }

                int id = Integer.parseInt(line.trim());

                String name = reader.readLine().trim();
                long categoryId = Long.parseLong(reader.readLine().trim());
                String ingredientsIdentifiers = reader.readLine().trim();
                BigDecimal price = new BigDecimal(reader.readLine().trim());
                String proteinSource = reader.readLine().trim();
                boolean containsDairy = Boolean.parseBoolean(reader.readLine().trim());
                boolean containsEggs = Boolean.parseBoolean(reader.readLine().trim());

                Category category = EntityFinder.categoryById(categoryId, Category.readCategoryFromFile());
                Set<Ingredient> ingredients = Ingredient.getIngredientsByIdentifiers(ingredientsIdentifiers, Ingredient.readIngredientFromFile());

                vegetarianMeals.add(new VegetarianMeal(name, category, ingredients, price, proteinSource, containsDairy, containsEggs));
            }
        } catch (IOException e) {
            System.err.println("Greška pri čitanju datoteke: " + e.getMessage());
        }

        return vegetarianMeals;
    }

    public boolean containsDairy() {
        return containsDairy;
    }

    public boolean containsEggs() {
        return containsEggs;
    }

    /**
     * Inputs a vegetarian meal.
     * @param categories the categories
     * @param ingredients the ingredients.txt
     * @param meals the meals
     * @param scanner the scanner object used for input
     * @return the vegetarian meal
     */
    public static VegetarianMeal inputVegetarianMeal(List<Category> categories, Set<Ingredient> ingredients, Set<Meal> meals, Scanner scanner) {
        String mealName;

        logger.info("Vegetarian meal input");
        while (true) {
            mealName = Input.string(scanner, "Unesite naziv vegetarijanskog jela: ");
            try {
                Validation.checkDuplicateMeal(meals, mealName);
                break;
            } catch (DuplicateEntryException e) {
                System.out.println("Jelo s tim nazivom već postoji!");
            }
        }

        Category mealCategory = EntityFinder.categoryName(scanner, "Unesite kategoriju vegetarijanskog jela: ", categories);


        BigDecimal mealPrice;
        Set<Ingredient> ingredientsEntered = Ingredient.enterArrayOfIngredients(ingredients, scanner);;
        while (true) {
            mealPrice = Input.bigDecimal(scanner, "Unesite cijenu vegetarijanskog jela: ");

            try {
                Validation.checkMealPrice(mealPrice);
                break;
            } catch (InvalidValueException e) {
                System.out.println("Cijena mora biti veća od 0 i treba biti realna. Pokušajte ponovno:");
            }
        }

        String mealProteinSource = Input.string(scanner, "Unesite izvor proteina vegetarijanskog jela: ");
        boolean mealContainsDiary = Input.booleanValue(scanner, "Sadrži li jelo mliječne proizvode? ");
        boolean mealContainsEggs = Input.booleanValue(scanner, "Sadrži li jelo jaja? ");

        return new VegetarianMeal(mealName, mealCategory, ingredientsEntered, mealPrice, mealProteinSource, mealContainsDiary, mealContainsEggs);
    }

    /**
     * Prints the vegetarian meal.
     * @param tabulators the number of tabulators
     */
    @Override
    public void print(Integer tabulators) {
        logger.info("Printing vegetarian meal.");

        super.print(tabulators);
        Output.tabulatorPrint(tabulators);
        System.out.println("Izvor proteina: " + this.proteinSource);
        Output.tabulatorPrint(tabulators);
        System.out.println("Sadrži jaja: " + (this.containsEggs ? "Da" : "Ne"));
        Output.tabulatorPrint(tabulators);
        System.out.println("Sadrži mliječne proizvode:  " + (this.containsDairy ? "Da" : "Ne"));
    }
}
