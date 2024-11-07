package hr.java.restaurant.model;

public sealed interface Vegan permits VeganMeal {
    boolean isOrganic();
    boolean isGlutenFree();
}
