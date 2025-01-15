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
import java.util.*;

/**
 * Represents a meal that is vegetarian.
 */
public final class VegetarianMeal extends Meal implements Vegetarian, Serializable {
    private final boolean containsDairy;
    private final boolean containsEggs;
    private final String proteinSource;

    private static final Logger logger = LoggerFactory.getLogger(VegetarianMeal.class);

    /**
     * Constructs a VegetarianMeal object.
     * @param name the name of the meal
     * @param category the category of the meal
     * @param ingredients the ingredients.txt of the meal
     * @param price the price of the meal
     * @param proteinSource the source of protein
     * @param containsDairy if the meal contains dairy
     * @param containsEggs if the meal contains eggs
     */
    public VegetarianMeal(Long id, String name, String mealType, Category category, Set<Ingredient> ingredients, BigDecimal price, String proteinSource, boolean containsDairy, boolean containsEggs) {
        super(id, name, mealType, category, ingredients, price);
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

    public boolean isContainsDairy() {
        return containsDairy;
    }

    public boolean isContainsEggs() {
        return containsEggs;
    }

    public String getProteinSource() {
        return proteinSource;
    }
}
