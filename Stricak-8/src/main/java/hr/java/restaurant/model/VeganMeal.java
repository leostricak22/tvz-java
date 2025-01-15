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
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Represents a meal that is vegan.
 */
public final class VeganMeal extends Meal implements Vegan, Serializable {
    private String proteinSource;
    private boolean organic;
    private boolean glutenFree;

   public VeganMeal(Builder builder) {
        super(builder.id, builder.name, builder.mealType, builder.category, builder.ingredients, builder.price);
        this.proteinSource = builder.proteinSource;
        this.organic = builder.organic;
        this.glutenFree = builder.glutenFree;
    }

    public String getProteinSource() {
        return proteinSource;
    }

    public void setProteinSource(String proteinSource) {
        this.proteinSource = proteinSource;
    }

    @Override
    public boolean isOrganic() {
        return organic;
    }

    public void setOrganic(boolean organic) {
        this.organic = organic;
    }

    @Override
    public boolean isGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public static class Builder {
        private final Long id;
        private final String name;
        private MealTypeEnum mealType;
        private Category category;
        private Set<Ingredient> ingredients;
        private BigDecimal price;
        private boolean organic;
        private boolean glutenFree;
        private String proteinSource;

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

        public Builder setOrganic(boolean organic) {
            this.organic = organic;
            return this;
        }

        public Builder setGlutenFree(boolean glutenFree) {
            this.glutenFree = glutenFree;
            return this;
        }

        public Builder setProteinSource(String proteinSource) {
            this.proteinSource = proteinSource;
            return this;
        }

        public VeganMeal build() {
            return new VeganMeal(this);
        }
    }
}
