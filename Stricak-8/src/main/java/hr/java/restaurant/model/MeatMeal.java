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
 * Represents a meal that contains meat.
 */
public final class MeatMeal extends Meal implements Meat, Serializable {
    private final String meatType;
    private final String meatOrigin;
    private final String meatCookingType;
    private static final Logger logger = LoggerFactory.getLogger(MeatMeal.class);

    /**
     * Constructs a MeatMeal object.
     *
     * @param name the name of the meal
     * @param category the category of the meal
     * @param ingredients the ingredients.txt of the meal
     * @param price the price of the meal
     * @param meatType the type of meat
     * @param meatOrigin the origin of meat
     * @param meatCookingType the cooking type of meat
     */
    public MeatMeal(Long id, String name, String mealType, Category category, Set<Ingredient> ingredients, BigDecimal price, String meatType, String meatOrigin, String meatCookingType) {
        super(id, name, mealType, category, ingredients, price);
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

    public String getMeatCookingType() {
        return meatCookingType;
    }
}
