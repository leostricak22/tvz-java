package hr.java.restaurant.model;

import hr.java.restaurant.exception.DuplicateEntryException;
import hr.java.restaurant.exception.InvalidValueException;
import hr.java.service.Input;
import hr.java.service.Output;
import hr.java.service.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Scanner;

public final class VeganMeal extends Meal implements Vegan {
    private String proteinSource;
    private boolean organic;
    private boolean glutenFree;
    private static final Logger logger = LoggerFactory.getLogger(VeganMeal.class);

    public VeganMeal(String name, Category category, Ingredient[] ingredients, BigDecimal price, String proteinSource, boolean organic, boolean glutenFree) {
        super(name, category, ingredients, price);
        this.proteinSource = proteinSource;
        this.organic = organic;
        this.glutenFree = glutenFree;
    }

    @Override
    public boolean isOrganic() {
        return this.organic;
    }

    @Override
    public boolean isGlutenFree() {
        return this.glutenFree;
    }

    public void setProteinSource(String proteinSource) {
        this.proteinSource = proteinSource;
    }

    public String getProteinSource() {
        return proteinSource;
    }

    public void setOrganic(boolean organic) {
        this.organic = organic;
    }

    public void setGlutenFree(boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public static VeganMeal inputVeganMeal(Category[] categories, Ingredient[] ingredients, Meal[] meals, Scanner scanner) {
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

        Category mealCategory = Input.categoryName(scanner, "Unesite kategoriju veganskog jela: ", categories);

        Integer numberOfIngredients = Input.integer(scanner, "Unesite broj sastojaka koji želite dodati: ");
        Ingredient[] ingredientsEntered = new Ingredient[numberOfIngredients];
        for(int j=0;j<numberOfIngredients;j++) {
            ingredientsEntered[j] = Input.ingredientName(scanner,"Unesite naziv sastojka kojeg želite dodati u jelo: ", ingredients);
        }

        BigDecimal mealPrice;
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
