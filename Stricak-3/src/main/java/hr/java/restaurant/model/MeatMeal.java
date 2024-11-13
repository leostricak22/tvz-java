package hr.java.restaurant.model;

import hr.java.restaurant.exception.DuplicateEntryException;
import hr.java.restaurant.exception.InvalidValueException;
import hr.java.service.Input;
import hr.java.service.Output;
import hr.java.service.Validation;

import java.math.BigDecimal;
import java.util.Scanner;

public final class MeatMeal extends Meal implements Meat {
    private String meatType;
    private String meatOrigin;
    private String meatCookingType;

    public MeatMeal(String name, Category category, Ingredient[] ingredients, BigDecimal price, String meatType, String meatOrigin, String meatCookingType) {
        super(name, category, ingredients, price);
        this.meatType = meatType;
        this.meatOrigin = meatOrigin;
        this.meatCookingType = meatCookingType;
    }

    @Override
    public String getMeatType() {
        return this.meatType;
    }

    @Override
    public String getMeatOrigin() {
        return this.meatOrigin;
    }

    public void setMeatType(String meatType) {
        this.meatType = meatType;
    }

    public void setMeatOrigin(String meatOrigin) {
        this.meatOrigin = meatOrigin;
    }

    public static MeatMeal inputMeatMeal(Category[] categories, Ingredient[] ingredients, Meal[] meals, Scanner scanner) {
        String mealName;

        while (true) {
            mealName = Input.string(scanner, "Unesite naziv mesnog jela: ");
            try {
                Validation.checkDuplicateMeal(meals, mealName);
                break;
            } catch (DuplicateEntryException e) {
                System.out.println("Jelo s tim nazivom već postoji!");
            }
        }

        Category mealCategory = Input.categoryName(scanner, "Unesite kategoriju mesnog jela: ", categories);

        Integer numberOfIngredients = Input.integer(scanner, "Unesite broj sastojaka koji želite dodati: ");
        Ingredient[] ingredientsEntered = new Ingredient[numberOfIngredients];
        for(int j=0;j<numberOfIngredients;j++) {
            ingredientsEntered[j] = Input.ingredientName(scanner,"Unesite naziv sastojka kojeg želite dodati u mesno jelo: ", ingredients);
        }

        BigDecimal mealPrice;
        while (true) {
            mealPrice = Input.bigDecimal(scanner, "Unesite cijenu mesnog jela: ");

            try {
                Validation.checkMealPrice(mealPrice);
                break;
            } catch (InvalidValueException e) {
                System.out.println("Cijena mora biti veća od 0 i treba biti realna. Pokušajte ponovno:");
            }
        }

        String meatType = Input.string(scanner, "Unesite tip mesa: ");
        String meatOrigin = Input.string(scanner, "Unesite podrijetlo mesa: ");
        String meatCookingType = Input.string(scanner, "Unesite način pripreme mesa: ");

        return new MeatMeal(mealName, mealCategory, ingredientsEntered, mealPrice, meatType, meatOrigin, meatCookingType);
    }

    @Override
    public void print(Integer tabulators) {
        super.print(tabulators);
        Output.tabulatorPrint(tabulators);
        System.out.println("Tip mesa: " + this.meatType);
        Output.tabulatorPrint(tabulators);
        System.out.println("Podrijetlo mesa: " + this.meatOrigin);
        Output.tabulatorPrint(tabulators);
        System.out.println("Način kuhanja: " + this.meatCookingType);
    }
}
