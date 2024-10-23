package hr.java.restaurant.model;

import hr.java.service.Input;

import java.math.BigDecimal;
import java.util.Scanner;

public class Meal {
    private String name;
    private Category category;
    private Ingredient[] ingredients;
    private BigDecimal price;

    public Meal(String name, Category category, Ingredient[] ingredient, BigDecimal price) {
        this.name = name;
        this.category = category;
        this.ingredients = ingredient;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public static Integer existsByName(Meal[] meals, String mealName) {
        for (int j=0;j<meals.length;j++) {
            if (mealName.equals(meals[j].getName())) {
                return j;
            }
        }
        return -1;
    }


    public static void inputMeal(Meal[] meals, Category[] categories, Ingredient[] ingredients, Scanner scanner) {
        for(int i = 0; i < meals.length; i++) {
            String mealName = Input.string(scanner, "Unesite naziv  "+ (i+1) +". jela: ");
            Category mealCategory = Input.categoryName(scanner, "Unesite naziv kategorije "+ (i+1) +". jela", categories);

            Integer numberOfIngredients = Input.integer(scanner, "Unesite broj sastojaka koji želite dodati: ");
            Ingredient[] ingredientsEntered = new Ingredient[numberOfIngredients];
            for(int j=0;j<numberOfIngredients;j++) {
                ingredientsEntered[j] = Input.ingredientName(scanner,"Unesite naziv "+(j+1)+". sastojka kojeg želite dodati u jelo: ", ingredients);
            }

            BigDecimal mealPrice = Input.bigDecimal(scanner, "Unesite cijenu "+(i+1)+". jela.");

            meals[i] = new Meal(mealName, mealCategory, ingredientsEntered, mealPrice);
        }
    }

    public static String[] mealNameArray(Meal[] meals) {
        String[] mealNames = new String[meals.length];

        for (int i = 0; i < meals.length; i++) {
            mealNames[i] = meals[i].getName();
        }

        return mealNames;
    }

    public void print(Integer tabulators) {
        Input.tabulatorPrint(tabulators);
        System.out.println("Naziv: " + this.name);

        Input.tabulatorPrint(tabulators);
        System.out.println("Kategorija:" );
        category.print(tabulators+1);

        Input.tabulatorPrint(tabulators);
        System.out.println("Sastojci: ");
        for(int i=0;i<ingredients.length;i++) {
            Input.tabulatorPrint(tabulators+1);
            System.out.println("Sastojak "+(i+1)+":");
            ingredients[i].print(tabulators+2);
        }

        Input.tabulatorPrint(tabulators);
        System.out.println("Cijena: " + this.price);
    }
}