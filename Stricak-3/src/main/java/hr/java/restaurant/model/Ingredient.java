package hr.java.restaurant.model;

import hr.java.restaurant.exception.DuplicateEntryException;
import hr.java.service.EntityFinder;
import hr.java.service.Input;
import hr.java.service.Output;
import hr.java.service.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Represents an ingredient of a meal.
 */
public class Ingredient  extends Entity {
    private static Long counter = 0L;
    private static final Logger logger = LoggerFactory.getLogger(Ingredient.class);

    private String name;
    private final Category category;
    private final BigDecimal kcal;
    private final String preparationMethod;

    /**
     * Constructs an Ingredient object
     * @param name the name of the ingredient
     * @param category the category of the ingredient
     * @param kcal the calories of the ingredient
     * @param preparationMethod the preparation method of the ingredient
     */
    public Ingredient(String name, Category category, BigDecimal kcal, String preparationMethod) {
        super(++counter);
        this.name = name;
        this.category = category;
        this.kcal = kcal;
        this.preparationMethod = preparationMethod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getKcal() {
        return kcal;
    }

    /**
     * Checks if the ingredient with the provided name already exists in the array of ingredients.
     * @param ingredients the ingredients
     * @param ingredientName the name of the ingredient
     * @return the index of the ingredient if it exists, -1 otherwise
     */
    public static Integer existsByName(Ingredient[] ingredients, String ingredientName) {
        for (int j=0;j<ingredients.length;j++) {
            if (ingredientName.equals(ingredients[j].getName())) {
                return j;
            }
        }
        return -1;
    }

    /**
     * Inputs the ingredient information.
     * @param ingredients the ingredients
     * @param categories the categories
     * @param scanner the scanner object used for input
     */
    public static void inputIngredient(Ingredient[] ingredients, Category[] categories, Scanner scanner) {
        for(int i = 0; i < ingredients.length; i++) {
            logger.info("Ingredient input");

            String ingredientName;

            while(true) {
                ingredientName = Input.string(scanner, "Unesite naziv "+ (i+1) +". sastojka: ");

                try {
                    Validation.checkDuplicateIngredient(ingredients, ingredientName);
                    break;
                } catch (DuplicateEntryException e) {
                    System.out.println("Sastojak s tim nazivom već postoji. Molimo unesite drugi naziv.");
                }
            }

            Category ingredientCategory = EntityFinder.categoryName(scanner, "Unesite naziv kategorije "+ (i+1) +". sastojka", categories);
            BigDecimal ingredientKcal = Input.bigDecimal(scanner, "Unesite kalorije " + (i+1) + ". sastojka.");
            String ingredientPreparationMethod = Input.string(scanner, "Unesite metodu pripremanja " + (i+1) + ". sastojka: ");

            ingredients[i] = new Ingredient(ingredientName, ingredientCategory, ingredientKcal, ingredientPreparationMethod);
        }
    }

    /**
     * Returns an array of ingredient names.
     * @param ingredients the ingredients
     * @return the array of ingredient names
     */
    public static String[] ingredientNameArray(Ingredient[] ingredients) {
        String[] ingredientsNames = new String[ingredients.length];

        for (int i = 0; i < ingredients.length; i++) {
            ingredientsNames[i] = ingredients[i].getName();
        }

        return ingredientsNames;
    }

    /**
     * Prints the ingredient information.
     * @param tabulators the number of tabulators to be printed before the ingredient information
     */
    public void print(Integer tabulators) {
        logger.info("Ingredient print");
        Output.tabulatorPrint(tabulators);
        System.out.println("Id: " + this.getId() + ", Naziv: " + this.name + ", Kalorije: " + this.kcal + ", Metoda pripreme: " + this.preparationMethod);

        Output.tabulatorPrint(tabulators);
        System.out.println("Kategorija:");
        category.print(tabulators+1);
    }
}
