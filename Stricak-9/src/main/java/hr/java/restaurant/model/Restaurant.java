package hr.java.restaurant.model;

import hr.java.restaurant.repository.RestaurantDatabaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a restaurant.
 */
public class Restaurant extends Entity implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Restaurant.class);

    private Address address;
    private Set<Meal> meals;
    private Set<Chef> chefs;
    private Set<Waiter> waiters;
    private Set<Deliverer> deliverers;

    private static final RestaurantDatabaseRepository restaurantRepository = new RestaurantDatabaseRepository();

    /**
     * Constructs a Restaurant object from provided data.
     * @param name the name of the restaurant
     * @param address the address of the restaurant
     * @param meals the meals in the restaurant
     * @param chefs the chefs in the restaurant
     * @param waiters the waiters in the restaurant
     * @param deliverers the deliverers in the restaurant
     */
    public Restaurant(Long id, String name, Address address, Set<Meal> meals, Set<Chef> chefs, Set<Waiter> waiters, Set<Deliverer> deliverers) {
        super(id, name);
        this.address = address;
        this.meals = meals;
        this.chefs = chefs;
        this.waiters = waiters;
        this.deliverers = deliverers;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Meal> getMeals() {
        return meals;
    }

    public void setMeals(Set<Meal> meals) {
        this.meals = meals;
    }

    public Set<Chef> getChefs() {
        return chefs;
    }

    public void setChefs(Set<Chef> chefs) {
        this.chefs = chefs;
    }

    public Set<Waiter> getWaiters() {
        return waiters;
    }

    public void setWaiters(Set<Waiter> waiters) {
        this.waiters = waiters;
    }

    public Set<Deliverer> getDeliverers() {
        return deliverers;
    }

    public void setDeliverers(Set<Deliverer> deliverers) {
        this.deliverers = deliverers;
    }
}