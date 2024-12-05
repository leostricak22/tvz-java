package hr.java.restaurant.generics;

import hr.java.restaurant.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantLabourExchangeOffice <T extends Restaurant> {
    private final List<T> restaurants;

    public RestaurantLabourExchangeOffice(List<T> restaurants) {
        this.restaurants = restaurants;
    }

    public List<T> getRestaurants() {
        return restaurants;
    }
}
