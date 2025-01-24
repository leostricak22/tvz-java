package hr.java.restaurant.model;

import hr.java.restaurant.enumeration.MealTypeEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Represents a meal in a restaurant.
 */
public class Meal extends Entity implements Serializable {

    private Category category;
    private Set<Ingredient> ingredients;
    private BigDecimal price;

    // TODO: remove this constructor
    public Meal(Long id, String name, Category category, Set<Ingredient> ingredients, BigDecimal price) {
        super(id, name);
        this.category = category;
        this.ingredients = ingredients;
        this.price = price;
    }

    public Meal(Builder builder) {
        super(builder.id, builder.name);
        this.category = builder.category;
        this.ingredients = builder.ingredients;
        this.price = builder.price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public static class Builder {
        private final Long id;
        private final String name;
        private Category category;
        private Set<Ingredient> ingredients;
        private BigDecimal price;

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

        public Meal build() {
            return new Meal(this);
        }
    }
}