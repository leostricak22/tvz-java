package hr.java.restaurant.model;

import hr.java.service.Input;

import java.math.BigDecimal;
import java.util.Scanner;

public class Ingredient  {
    private String name;
    private Category category;
    private BigDecimal kcal;
    private String preparationMethod;

    public Ingredient(String name, Category category, BigDecimal kcal, String preparationMethod) {
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getKcal() {
        return kcal;
    }

    public void setKcal(BigDecimal kcal) {
        this.kcal = kcal;
    }

    public String getPreparationMethod() {
        return preparationMethod;
    }

    public void setPreparationMethod(String preparationMethod) {
        this.preparationMethod = preparationMethod;
    }

    public static Integer existsByName(Ingredient[] ingredients, String ingredientName) {
        for (int j=0;j<ingredients.length;j++) {
            if (ingredientName.equals(ingredients[j].getName())) {
                return j;
            }
        }
        return -1;
    }

    public static void inputIngredient(Ingredient[] ingredients, Category[] categories, Scanner scanner) {
        for(int i = 0; i < ingredients.length; i++) {
            String ingredientName = Input.string(scanner, "Unesite naziv  "+ (i+1) +". sastojka: ");
            Category ingredientCategory = Input.categoryName(scanner, "Unesite naziv kategorije "+ (i+1) +". sastojka", categories);
            BigDecimal ingredientKcal = Input.bigDecimal(scanner, "Unesite kalorije " + (i+1) + ". sastojka.");
            String ingredientPreparationMethod = Input.string(scanner, "Unesite metodu pripremanja " + (i+1) + ". sastojka: ");

            ingredients[i] = new Ingredient(ingredientName, ingredientCategory, ingredientKcal, ingredientPreparationMethod);
        }
    }

    public static String[] ingredientNameArray(Ingredient[] ingredients) {
        String[] ingredientsNames = new String[ingredients.length];

        for (int i = 0; i < ingredients.length; i++) {
            ingredientsNames[i] = ingredients[i].getName();
        }

        return ingredientsNames;
    }
}
