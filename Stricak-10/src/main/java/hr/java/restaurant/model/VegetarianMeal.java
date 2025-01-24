package hr.java.restaurant.model;

import hr.java.restaurant.enumeration.MealTypeEnum;

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

    public VegetarianMeal(Builder builder) {
        super(builder.id, builder.name, builder.category, builder.ingredients, builder.price);
        this.containsDairy = builder.containsDairy;
        this.containsEggs = builder.containsEggs;
        this.proteinSource = builder.proteinSource;
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

    public static class Builder {
        private final Long id;
        private final String name;
        private Category category;
        private Set<Ingredient> ingredients;
        private BigDecimal price;
        private boolean containsDairy;
        private boolean containsEggs;
        private String proteinSource;

        public Builder(Long id, String name) {
            this.id = id;
            this.name = name;
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

        public Builder setContainsDairy(boolean containsDairy) {
            this.containsDairy = containsDairy;
            return this;
        }

        public Builder setContainsEggs(boolean containsEggs) {
            this.containsEggs = containsEggs;
            return this;
        }

        public Builder setProteinSource(String proteinSource) {
            this.proteinSource = proteinSource;
            return this;
        }

        public VegetarianMeal build() {
            return new VegetarianMeal(this);
        }
    }
}
