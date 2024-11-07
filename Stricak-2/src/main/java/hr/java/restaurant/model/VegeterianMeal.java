package hr.java.restaurant.model;

import hr.java.service.Input;
import hr.java.service.Output;

import java.math.BigDecimal;
import java.util.Scanner;

public final class VegeterianMeal extends Meal implements Vegeterian {
    private boolean containsDairy;
    private boolean containsEggs;
    private String proteinSource;

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

    public void setContainsDairy(boolean containsDairy) {
        this.containsDairy = containsDairy;
    }
    
    public void setContainsEggs(boolean containsEggs) {
        this.containsEggs = containsEggs;
    }

    public static VegeterianMeal inputVegeterianMeal(Category[] categories, Ingredient[] ingredients , Scanner scanner) {
        String mealName = Input.string(scanner, "Unesite naziv vegetarijanskog jela: ");
        Category mealCategory = Input.categoryName(scanner, "Unesite kategoriju vegetarijanskog jela: ", categories);

        Integer numberOfIngredients = Input.integer(scanner, "Unesite broj sastojaka koji želite dodati: ");
        Ingredient[] ingredientsEntered = new Ingredient[numberOfIngredients];
        for(int j=0;j<numberOfIngredients;j++) {
            ingredientsEntered[j] = Input.ingredientName(scanner,"Unesite naziv sastojka kojeg želite dodati u jelo: ", ingredients);
        }

        BigDecimal mealPrice = Input.bigDecimal(scanner, "Unesite cijenu vegetarijanskog jela: ");
        String mealProteinSource = Input.string(scanner, "Unesite izvor proteina vegetarijanskog jela: ");
        boolean mealContainsDiary = Input.booleanValue(scanner, "Sadrži li jelo mliječne proizvode? ");
        boolean mealContainsEggs = Input.booleanValue(scanner, "Sadrži li jelo jaja? ");

        return new VegeterianMeal(mealName, mealCategory, ingredientsEntered, mealPrice, mealProteinSource, mealContainsDiary, mealContainsEggs);
    }

    @Override
    public void print(Integer tabulators) {
        super.print(tabulators);
        Output.tabulatorPrint(tabulators);
        System.out.println("Izvor proteina: " + this.proteinSource);
        Output.tabulatorPrint(tabulators);
        System.out.println("Sadrži jaja: " + (this.containsEggs ? "Da" : "Ne"));
        Output.tabulatorPrint(tabulators);
        System.out.println("Sadrži mliječne proizvode:  " + (this.containsDairy ? "Da" : "Ne"));
    }
}
