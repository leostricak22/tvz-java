package hr.java.restaurant.model;

import hr.java.restaurant.exception.DuplicateEntryException;
import hr.java.restaurant.exception.InvalidValueException;
import hr.java.restaurant.util.EntityFinder;
import hr.java.restaurant.util.Input;
import hr.java.restaurant.util.Output;
import hr.java.restaurant.util.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Represents a meal that is vegan.
 */
public final class VeganMeal extends Meal implements Vegan, Serializable {
    private final String proteinSource;
    private final boolean organic;
    private final boolean glutenFree;
    private static final Logger logger = LoggerFactory.getLogger(VeganMeal.class);

    /**
     * Constructs a VeganMeal object.
     * @param name the name of the meal
     * @param category the category of the meal
     * @param ingredients the ingredients.txt of the meal
     * @param price the price of the meal
     * @param proteinSource the source of protein
     * @param organic if the meal is organic
     * @param glutenFree if the meal is gluten-free
     */
    public VeganMeal(Long id, String name, String mealType, Category category, Set<Ingredient> ingredients, BigDecimal price, String proteinSource, boolean organic, boolean glutenFree) {
        super(id, name, mealType, category, ingredients, price);
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

    public String getProteinSource() {
        return proteinSource;
    }
}
