package hr.java.restaurant.enumeration;

import hr.java.restaurant.model.Entity;

public enum MealTypeEnum {
    VEGAN("vegan"),
    VEGETARIAN("vegetarian"),
    MEAT("meat");

    private final String name;

    MealTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
