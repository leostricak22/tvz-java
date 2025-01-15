package hr.java.restaurant.model;

/**
 * Represents a meat meal.
 */
public sealed interface Meat permits MeatMeal {
    boolean isEcoFriendly();
    boolean isOrganic();
}
