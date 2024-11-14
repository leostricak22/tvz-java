package hr.java.restaurant.model;

/**
 * Represents a vegeterian meal.
 */
public sealed interface Vegeterian permits VegeterianMeal {
    boolean containsDairy();
    boolean containsEggs();
}
