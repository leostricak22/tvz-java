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
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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
     * @param ingredients the ingredients of the meal
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
