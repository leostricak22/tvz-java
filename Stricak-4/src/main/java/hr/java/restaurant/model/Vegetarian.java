package hr.java.restaurant.model;

/**
 * Represents a vegetarian meal.
 */
public sealed interface Vegetarian permits VegetarianMeal {
    boolean containsDairy();
    boolean containsEggs();
}
