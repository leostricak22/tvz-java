package hr.java.restaurant.model;

/**
 * Represents a vegan meal.
 */
public sealed interface Vegan permits VeganMeal {
    boolean isOrganic();
    boolean isGlutenFree();
}
