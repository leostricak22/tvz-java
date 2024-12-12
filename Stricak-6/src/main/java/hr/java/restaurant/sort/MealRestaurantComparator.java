package hr.java.restaurant.sort;

import hr.java.restaurant.model.Meal;
import hr.java.restaurant.model.Restaurant;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MealRestaurantComparator implements Comparator<Map.Entry<Meal, List<Restaurant>>> {
    public int compare(Map.Entry<Meal, List<Restaurant>> mr1, Map.Entry<Meal, List<Restaurant>> mr2) {
        return mr2.getValue().size() - mr1.getValue().size();
    }
}