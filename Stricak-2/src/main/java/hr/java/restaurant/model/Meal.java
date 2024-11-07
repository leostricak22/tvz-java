package hr.java.restaurant.model;

import hr.java.service.Input;
import hr.java.service.Output;

import java.math.BigDecimal;
import java.util.Scanner;

public class Meal extends Entity {
    private static Long counter = 0L;

    private String name;
    private Category category;
    private Ingredient[] ingredients;
    private BigDecimal price;

    public Meal(String name, Category category, Ingredient[] ingredients, BigDecimal price) {
        super(++counter);
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
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


    public static Meal inputMeal(Category[] categories, Ingredient[] ingredients, Scanner scanner) {
        String mealName = Input.string(scanner, "Unesite naziv jela: ");
        Category mealCategory = Input.categoryName(scanner, "Unesite naziv kategorije jela", categories);

        Integer numberOfIngredients = Input.integer(scanner, "Unesite broj sastojaka koji želite dodati: ");
        Ingredient[] ingredientsEntered = new Ingredient[numberOfIngredients];
        for(int j=0;j<numberOfIngredients;j++) {
            ingredientsEntered[j] = Input.ingredientName(scanner,"Unesite naziv sastojka kojeg želite dodati u jelo: ", ingredients);
        }

        BigDecimal mealPrice = Input.bigDecimal(scanner, "Unesite cijenu jela.");

        return new Meal(mealName, mealCategory, ingredientsEntered, mealPrice);
    }

    public static String[] mealNameArray(Meal[] meals) {
        String[] mealNames = new String[meals.length];

        for (int i = 0; i < meals.length; i++) {
            mealNames[i] = meals[i].getName();
        }

        return mealNames;
    }

    public void print(Integer tabulators) {
        Output.tabulatorPrint(tabulators);
        System.out.println("Id: " + this.getId() + ", Naziv: " + this.name + ", Cijena: " + this.price);

        Output.tabulatorPrint(tabulators);
        System.out.println("Kategorija:");
        category.print(tabulators + 1);

        Output.tabulatorPrint(tabulators);
        System.out.println("Sastojci: ");
        for (int i = 0; i < ingredients.length; i++) {
            Output.tabulatorPrint(tabulators + 1);
            System.out.println("Sastojak " + (i + 1) + ":");
            ingredients[i].print(tabulators + 2);
        }
    }

    public BigDecimal getTotalCalories() {
        BigDecimal mealCalories = BigDecimal.valueOf(0);

        for (int j = 0; j < this.getIngredients().length; j++)
            mealCalories = mealCalories.add(this.getIngredients()[j].getKcal());

        return  mealCalories;
    }

    public static Meal findMostCaloricMeal(Meal[] meals) {
        Meal mostCaloricMeal = meals[0];
        BigDecimal mostMealCalories = BigDecimal.valueOf(-1);
        BigDecimal mealCalories;

        for (Meal meal : meals) {
            mealCalories = meal.getTotalCalories();

            if (mealCalories.compareTo(mostMealCalories) > 0) {
                mostCaloricMeal = meal;
                mostMealCalories = mealCalories;
            }
        }

        return mostCaloricMeal;
    }

    public static Meal findLeastCaloricMeal(Meal[] meals) {
        Meal mostCaloricMeal = meals[0];
        BigDecimal mostMealCalories = BigDecimal.valueOf(Double.MAX_VALUE);
        BigDecimal mealCalories;

        for (Meal meal : meals) {
            mealCalories = meal.getTotalCalories();

            if (mealCalories.compareTo(mostMealCalories) < 0) {
                mostCaloricMeal = meal;
                mostMealCalories = mealCalories;
            }
        }

        return mostCaloricMeal;
    }

}