package hr.java.restaurant.model;

import hr.java.restaurant.enumeration.MealTypeEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Represents a meal in a restaurant.
 */
public class Meal extends Entity implements Serializable {

    private MealTypeEnum mealType;
    private Category category;
    private Set<Ingredient> ingredients;
    private BigDecimal price;

    public Meal(Long id, String name, MealTypeEnum mealType, Category category, Set<Ingredient> ingredients, BigDecimal price) {
        super(id, name);
        this.mealType = mealType;
        this.category = category;
        this.ingredients = ingredients;
        this.price = price;
    }

    public MealTypeEnum getMealType() {
        return mealType;
    }

    public void setMealType(MealTypeEnum mealType) {
        this.mealType = mealType;
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
}