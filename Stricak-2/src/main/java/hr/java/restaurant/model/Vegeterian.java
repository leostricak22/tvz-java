package hr.java.restaurant.model;

public sealed interface Vegeterian permits VegeterianMeal {
    boolean containsDairy();
    boolean containsEggs();
}
