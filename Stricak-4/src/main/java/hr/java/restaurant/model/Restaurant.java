package hr.java.restaurant.model;

import hr.java.service.EntityFinder;
import hr.java.service.Input;
import hr.java.service.Output;
import hr.java.service.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Represents a restaurant.
 */
public class Restaurant extends Entity {
    private static Long counter = 0L;
    private static final Logger logger = LoggerFactory.getLogger(Restaurant.class);

    private String name;
    private final Address address;
    private final Set<Meal> meals;
    private final Set<Chef> chefs;
    private final Set<Waiter> waiters;
    private final Set<Deliverer> deliverers;

    /**
     * Constructs a Restaurant object from provided data.
     * @param name the name of the restaurant
     * @param address the address of the restaurant
     * @param meals the meals in the restaurant
     * @param chefs the chefs in the restaurant
     * @param waiters the waiters in the restaurant
     * @param deliverers the deliverers in the restaurant
     */
    public Restaurant(String name, Address address, Set<Meal> meals, Set<Chef> chefs, Set<Waiter> waiters, Set<Deliverer> deliverers) {
        super(++counter);
        this.name = name;
        this.address = address;
        this.meals = meals;
        this.chefs = chefs;
        this.waiters = waiters;
        this.deliverers = deliverers;
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

    public static void findMealInRestaurants(Map<Meal, List<Restaurant>> mealRestaurants, Set<Meal> meals, Scanner scanner) {
        Meal meal = EntityFinder.mealName(scanner, "Unesite naziv jela da bi vidjeli u kojim se restoranima nalazi: ", meals);

        if(meal == null)
            return;

        System.out.println("Meal: " + meal.getName());
        if (mealRestaurants.get(meal) != null) {
            List<Restaurant> restaurantList = mealRestaurants.get(meal);
            for (Restaurant restaurant : restaurantList) {
                System.out.println("- " + restaurant.getName());
            }
        } else {
            System.out.println("* ne poslužuje se u ni jednom restoranu");
        }


    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
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
     * Inputs restaurant data from the console.
     *
     * @param numOfRestaurants the restaurants to be input
     * @param meals            the choice of meals to be input
     * @param chefs            the choice of chefs to be input
     * @param waiters          the choice of waiters to be input
     * @param deliverers       the choice of deliverers to be input
     * @param scanner          the scanner object used for input
     * @return
     */
    public static List<Restaurant> inputRestaurantList(int numOfRestaurants, Set<Meal> meals, Set<Chef> chefs, Set<Waiter> waiters, Set<Deliverer> deliverers, Scanner scanner) {
        logger.info("Restaurant input");
        List<Restaurant> restaurants = new ArrayList<>();

        for(int i = 0; i < numOfRestaurants; i++) {
            String restaurantName;

            while (true) {
                restaurantName = Input.string(scanner, "Unesite naziv  "+ (i+1) +". restorana: ");

                try {
                    Validation.checkDuplicateRestaurant(restaurants, restaurantName);
                    break;
                } catch (Exception e) {
                    System.out.println("Restoran s tim imenom već postoji. Molimo unesite drugo ime.");
                }
            }

            Address restaurantAddress = Address.inputAddress(scanner);

            int numberOfMeals = Input.integer(scanner, "Unesite broj jela koji želite dodati: ");
            Set<Meal> mealsEntered = new HashSet<>(Set.of());
            for(int j=0;j<numberOfMeals;j++) {
                mealsEntered.add(EntityFinder.mealName(scanner,"Unesite naziv "+(j+1)+". jela koje želite dodati: ", meals));
            }

            int numberOfChefs = Input.integer(scanner, "Unesite broj kuhara koji želite dodati: ");
            Set<Chef> chefsEntered = new HashSet<>(Set.of());
            for(int j=0;j<numberOfChefs;j++) {
                chefsEntered.add(EntityFinder.chefName(scanner,"Unesite ime i prezime (odvojeno razmakom) "+(j+1)+". kuhara kojeg želite dodati: ", chefs));
            }

            int numberOfWaiters = Input.integer(scanner, "Unesite broj konobara koji želite dodati: ");
            Set<Waiter> waitersEntered = new HashSet<>(Set.of());
            for(int j = 0; j< numberOfWaiters; j++) {
                waitersEntered.add(EntityFinder.waiterName(scanner,"Unesite ime i prezime (odvojeno razmakom) "+(j+1)+". konobara kojeg želite dodati: ", waiters));
            }

            int numberOfDeliverers = Input.integer(scanner, "Unesite broj dostavljača koji želite dodati: ");
            Set<Deliverer> deliverersEntered = new HashSet<>(Set.of());
            for(int j = 0; j< numberOfDeliverers; j++) {
                deliverersEntered.add(EntityFinder.delivererName(scanner,"Unesite ime i prezime (odvojeno razmakom) "+(j+1)+". dostavljača kojeg želite dodati: ", deliverers));
            }

            restaurants.add(new Restaurant(restaurantName, restaurantAddress, mealsEntered, chefsEntered, waitersEntered, deliverersEntered));
        }

        return restaurants;
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

    /**
     * Finds the number of orders in the restaurant.
     * @param orders the orders to be counted
     * @return the number of orders
     */
    public int findNumberOfOrders(List<Order> orders) {
        int counter=0;
        for (Order order : orders) {
            if (order.getRestaurant() == this) {
                counter++;
            }
        }
        return counter;
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
        System.out.println("Naziv restorana: " + this.name);
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