package hr.java.restaurant.model;

import hr.java.restaurant.repository.RestaurantRepository;
import hr.java.restaurant.util.Output;
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
    private final Set<Meal> meals;
    private final Set<Chef> chefs;
    private final Set<Waiter> waiters;
    private final Set<Deliverer> deliverers;

    private static final RestaurantRepository<Restaurant> restaurantRepository = new RestaurantRepository<>();

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

    public static Set<Restaurant> restaurantByIdentifier(String restaurantIdentifier) {
        Set<Restaurant> restaurantList = new HashSet<>();
        String[] identifiers = restaurantIdentifier.split(",");
        for (String identifier : identifiers) {
            restaurantList.add(restaurantRepository.findById(Long.parseLong(identifier)));
        }

        return restaurantList;
    }

    public Person personWithLongestContract() {
        List<Person> restaurantWorkers = new ArrayList<>();

        restaurantWorkers.addAll(chefs);
        restaurantWorkers.addAll(waiters);
        restaurantWorkers.addAll(deliverers);

        restaurantWorkers.sort((p1, p2) -> p1.getContract().getStartDate().compareTo(p2.getContract().getStartDate()));

        if(restaurantWorkers.isEmpty())
            return null;

        return restaurantWorkers.getFirst();
    }

    public Set<Chef> getChefs() {
        return chefs;
    }

    public Set<Waiter> getWaiters() {
        return waiters;
    }

    public Address getAddress() {
        return address;
    }

    public static Map<Meal, List<Restaurant>> addMealsToMealRestaurantMap(List<Restaurant> restaurants) {
        Map<Meal, List<Restaurant>> mealRestaurants = new HashMap<>();

        for (Restaurant restaurant : restaurants) {
            Set<Meal> meals = restaurant.getMeals();

            for(Meal meal : meals) {
                if(mealRestaurants.get(meal) == null) {
                    mealRestaurants.put(meal, new ArrayList<>(Collections.singleton(restaurant)));
                } else {
                    mealRestaurants.get(meal).add(restaurant);
                }
            }
        }

        return mealRestaurants;
    }

    public Person personWithHighestSalary() {
        List<Person> restaurantWorkers = new ArrayList<>();

        restaurantWorkers.addAll(chefs);
        restaurantWorkers.addAll(waiters);
        restaurantWorkers.addAll(deliverers);

        restaurantWorkers.sort((p1, p2) -> p2.getSalary().compareTo(p1.getSalary()));

        if(restaurantWorkers.isEmpty())
            return null;

        return restaurantWorkers.getFirst();
    }

    public Set<Meal> getMeals() { return meals; }
    public Set<Deliverer> getDeliverers() { return deliverers; }

    /**
     * Checks if the restaurant with the given name already exists.
     * @param restaurants the restaurants to be checked
     * @param restaurantName the name of the restaurant to be checked
     * @return the index of the restaurant if it exists, -1 otherwise
     */
    public static Integer existsByName(List<Restaurant> restaurants, String restaurantName) {
        for (int j = 0; j< restaurants.size(); j++) {
            if (restaurantName.equals(restaurants.get(j).getName())) {
                return j;
            }
        }
        return -1;
    }

    /**
     * Returns the names of the restaurants in the array.
     * @param restaurants the restaurants to be listed by name
     * @return the names of the restaurants
     */
    public static List<String> restaurantNameArray(List<Restaurant> restaurants) {
        List<String> restaurantNames = new ArrayList<String>(restaurants.size());
        for (Restaurant restaurant : restaurants) {
            restaurantNames.add(restaurant.getName());
        }

        return restaurantNames;
    }

    public int getTotalEmployees() {
        return chefs.size() + waiters.size() + deliverers.size();
    }

    /**
     * Finds the number of meals in the restaurant.
     * @param tabulators the number of tabulators before the output
     */
    public void print(Integer tabulators) {
        logger.info("Printing restaurant.");

        Output.tabulatorPrint(tabulators);
        System.out.println("Id: " + this.getId());

        Output.tabulatorPrint(tabulators);
        System.out.println("Naziv restorana: " + getName());
        Output.tabulatorPrint(tabulators);
        System.out.println("Adresa restorana: ");
        address.print(tabulators+1);

        Output.tabulatorPrint(tabulators);
        System.out.println("Sva jela u restoranu:");
        int i=0;
        for(Meal meal : this.meals) {
            Output.tabulatorPrint(tabulators+1);
            System.out.println("Jelo "+(i+1)+":");
            meal.print(tabulators+2);
            i++;
        }

        Output.tabulatorPrint(tabulators);
        System.out.println("Svi kuhari u restoranu:");
        i=0;
        for(Chef chef : this.chefs) {
            Output.tabulatorPrint(tabulators+1);
            System.out.println("Kuhar "+(i+1)+":");
            chef.print(tabulators+2);
            i++;
        }

        Output.tabulatorPrint(tabulators);
        System.out.println("Svi konobari u restoranu:");
        i=0;
        for (Waiter waiter : this.waiters) {
            Output.tabulatorPrint(tabulators+1);
            System.out.println("Konobar "+(i+1)+":");
            waiter.print(tabulators+2);
            i++;
        }

        Output.tabulatorPrint(tabulators);
        System.out.println("Svi dostavljači u restoranu:");
        i=0;
        for(Deliverer deliverer : this.deliverers) {
            Output.tabulatorPrint(tabulators+1);
            System.out.println("Dostavljač "+(i+1)+":");
            deliverer.print(tabulators+2);
            i++;
        }
    }
}