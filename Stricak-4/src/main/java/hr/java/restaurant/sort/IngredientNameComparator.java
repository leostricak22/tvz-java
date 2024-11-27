package hr.java.restaurant.sort;

import hr.java.restaurant.model.Ingredient;

import java.util.Comparator;

public class IngredientNameComparator implements Comparator<Ingredient> {
    @Override
    public int compare(Ingredient ingredient1, Ingredient ingredient2) {
        return ingredient1.getName().compareTo(ingredient2.getName());
    }
}