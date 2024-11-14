package hr.java.restaurant.model;

import hr.java.restaurant.exception.DuplicateEntryException;
import hr.java.restaurant.exception.InvalidValueException;
import hr.java.service.EntityFinder;
import hr.java.service.Input;
import hr.java.service.Output;
import hr.java.service.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Represents a meal that is vegetarian.
 */
public final class VegeterianMeal extends Meal implements Vegeterian {
    private final boolean containsDairy;
    private final boolean containsEggs;
    private final String proteinSource;

    private static final Logger logger = LoggerFactory.getLogger(VegeterianMeal.class);

    /**
     * Constructs a VegeterianMeal object.
     * @param name the name of the meal
     * @param category the category of the meal
     * @param ingredients the ingredients of the meal
     * @param price the price of the meal
     * @param proteinSource the source of protein
     * @param containsDairy if the meal contains dairy
     * @param containsEggs if the meal contains eggs
     */
    public VegeterianMeal(String name, Category category, Ingredient[] ingredients, BigDecimal price, String proteinSource, boolean containsDairy, boolean containsEggs) {
        super(name, category, ingredients, price);
        this.containsDairy = containsDairy;
        this.containsEggs = containsEggs;
        this.proteinSource = proteinSource;
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
     * @param ingredients the ingredients
     * @param meals the meals
     * @param scanner the scanner object used for input
     * @return the vegetarian meal
     */
    public static VegeterianMeal inputVegeterianMeal(Category[] categories, Ingredient[] ingredients, Meal[] meals, Scanner scanner) {
        String mealName;

        logger.info("Vegeterian meal input");
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

        int numberOfIngredients = Input.integer(scanner, "Unesite broj sastojaka koji želite dodati: ");
        Ingredient[] ingredientsEntered = new Ingredient[numberOfIngredients];
        for(int j=0;j<numberOfIngredients;j++) {
            ingredientsEntered[j] = EntityFinder.ingredientName(scanner,"Unesite naziv sastojka kojeg želite dodati u jelo: ", ingredients);
        }

        BigDecimal mealPrice;
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

        return new VegeterianMeal(mealName, mealCategory, ingredientsEntered, mealPrice, mealProteinSource, mealContainsDiary, mealContainsEggs);
    }

    /**
     * Prints the vegetarian meal.
     * @param tabulators the number of tabulators
     */
    @Override
    public void print(Integer tabulators) {
        logger.info("Printing vegeterian meal.");

        super.print(tabulators);
        Output.tabulatorPrint(tabulators);
        System.out.println("Izvor proteina: " + this.proteinSource);
        Output.tabulatorPrint(tabulators);
        System.out.println("Sadrži jaja: " + (this.containsEggs ? "Da" : "Ne"));
        Output.tabulatorPrint(tabulators);
        System.out.println("Sadrži mliječne proizvode:  " + (this.containsDairy ? "Da" : "Ne"));
    }
}
