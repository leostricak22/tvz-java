package hr.java.restaurant.model;

import hr.java.restaurant.enumeration.MealTypeEnum;
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
    private String meatType;
    private boolean ecoFriendly;
    private boolean organic;

    public MeatMeal(Builder builder) {
        super(builder.id, builder.name, builder.mealType, builder.category, builder.ingredients, builder.price);
        this.meatType = builder.meatType;
        this.ecoFriendly = builder.ecoFriendly;
        this.organic = builder.organic;
    }

    public String getMeatType() {
        return meatType;
    }

    public void setMeatType(String meatType) {
        this.meatType = meatType;
    }

    public boolean isEcoFriendly() {
        return ecoFriendly;
    }

    public void setEcoFriendly(boolean ecoFriendly) {
        this.ecoFriendly = ecoFriendly;
    }

    public boolean isOrganic() {
        return organic;
    }

    public void setOrganic(boolean organic) {
        this.organic = organic;
    }

    public static class Builder {
        private final Long id;
        private final String name;
        private MealTypeEnum mealType;
        private Category category;
        private Set<Ingredient> ingredients;
        private BigDecimal price;
        private String meatType;
        private boolean ecoFriendly;
        private boolean organic;

        public Builder(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Builder setMealType(MealTypeEnum mealType) {
            this.mealType = mealType;
            return this;
        }

        public Builder setCategory(Category category) {
            this.category = category;
            return this;
        }

        public Builder setIngredients(Set<Ingredient> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public Builder setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder setMeatType(String meatType) {
            this.meatType = meatType;
            return this;
        }

        public Builder setEcoFriendly(boolean ecoFriendly) {
            this.ecoFriendly = ecoFriendly;
            return this;
        }

        public Builder setOrganic(boolean organic) {
            this.organic = organic;
            return this;
        }

        public MeatMeal build() {
            return new MeatMeal(this);
        }
    }
}
