package hr.java.restaurant.model;

import hr.java.service.EntityFinder;
import hr.java.service.Input;
import hr.java.service.Output;
import hr.java.service.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Represents a restaurant.
 */
public class Restaurant extends Entity {
    private static Long counter = 0L;
    private static final Logger logger = LoggerFactory.getLogger(Restaurant.class);

    private String name;
    private final Address address;
    private final Meal[] meals;
    private final Chef[] chefs;
    private final Waiter[] waiters;
    private final Deliverer[] deliverers;

    /**
     * Constructs a Restaurant object from provided data.
     * @param name the name of the restaurant
     * @param address the address of the restaurant
     * @param meals the meals in the restaurant
     * @param chefs the chefs in the restaurant
     * @param waiters the waiters in the restaurant
     * @param deliverers the deliverers in the restaurant
     */
    public Restaurant(String name, Address address, Meal[] meals, Chef[] chefs, Waiter[] waiters, Deliverer[] deliverers) {
        super(++counter);
        this.name = name;
        this.address = address;
        this.meals = meals;
        this.chefs = chefs;
        this.waiters = waiters;
        this.deliverers = deliverers;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Meal[] getMeals() { return meals; }
    public Deliverer[] getDeliverers() { return deliverers; }

    /**
     * Checks if the restaurant with the given name already exists.
     * @param restaurants the restaurants to be checked
     * @param restaurantName the name of the restaurant to be checked
     * @return the index of the restaurant if it exists, -1 otherwise
     */
    public static Integer existsByName(Restaurant[] restaurants, String restaurantName) {
        for (int j = 0; j< restaurants.length; j++) {
            if (restaurantName.equals(restaurants[j].getName())) {
                return j;
            }
        }
        return -1;
    }

    /**
     * Inputs restaurant data from the console.
     * @param restaurants the restaurants to be input
     * @param meals the choice of meals to be input
     * @param chefs the choice of chefs to be input
     * @param waiters the choice of waiters to be input
     * @param deliverers the choice of deliverers to be input
     * @param scanner the scanner object used for input
     */
    public static void inputRestaurant(Restaurant[] restaurants, Meal[] meals, Chef[] chefs, Waiter[] waiters, Deliverer[] deliverers, Scanner scanner) {
        logger.info("Restaurant input");
        for(int i = 0; i < restaurants.length; i++) {
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
            Meal[] mealsEntered = new Meal[numberOfMeals];
            for(int j=0;j<numberOfMeals;j++) {
                mealsEntered[j] = EntityFinder.mealName(scanner,"Unesite naziv "+(j+1)+". jela koje želite dodati: ", meals);
            }

            int numberOfChefs = Input.integer(scanner, "Unesite broj kuhara koji želite dodati: ");
            Chef[] chefsEntered = new Chef[numberOfChefs];
            for(int j=0;j<numberOfChefs;j++) {
                chefsEntered[j] = EntityFinder.chefName(scanner,"Unesite ime i prezime (odvojeno razmakom) "+(j+1)+". kuhara kojeg želite dodati: ", chefs);
            }

            int numberOfWaiters = Input.integer(scanner, "Unesite broj konobara koji želite dodati: ");
            Waiter[] waitersEntered = new Waiter[numberOfWaiters];
            for(int j = 0; j< numberOfWaiters; j++) {
                waitersEntered[j] = EntityFinder.waiterName(scanner,"Unesite ime i prezime (odvojeno razmakom) "+(j+1)+". konobara kojeg želite dodati: ", waiters);
            }

            int numberOfDeliverers = Input.integer(scanner, "Unesite broj dostavljača koji želite dodati: ");
            Deliverer[] deliverersEntered = new Deliverer[numberOfDeliverers];
            for(int j = 0; j< numberOfDeliverers; j++) {
                deliverersEntered[j] = EntityFinder.delivererName(scanner,"Unesite ime i prezime (odvojeno razmakom) "+(j+1)+". dostavljača kojeg želite dodati: ", deliverers);
            }

            restaurants[i] = new Restaurant(restaurantName, restaurantAddress, mealsEntered, chefsEntered, waitersEntered, deliverersEntered);
        }
    }

    /**
     * Returns the names of the restaurants in the array.
     * @param restaurants the restaurants to be listed by name
     * @return the names of the restaurants
     */
    public static String[] restaurantNameArray(Restaurant[] restaurants) {
        String[] restaurantNames = new String[restaurants.length];
        for (int i = 0; i < restaurants.length; i++) {
            restaurantNames[i] = restaurants[i].getName();
        }

        return restaurantNames;
    }

    /**
     * Finds the number of orders in the restaurant.
     * @param orders the orders to be counted
     * @return the number of orders
     */
    public int findNumberOfOrders(Order[] orders) {
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
        for(int i=0;i<this.meals.length;i++) {
            Output.tabulatorPrint(tabulators+1);
            System.out.println("Jelo "+(i+1)+":");
            this.meals[i].print(tabulators+2);
        }

        Output.tabulatorPrint(tabulators);
        System.out.println("Svi kuhari u restoranu:");
        for(int i=0;i<this.chefs.length;i++) {
            Output.tabulatorPrint(tabulators+1);
            System.out.println("Kuhar "+(i+1)+":");
            this.chefs[i].print(tabulators+2);
        }

        Output.tabulatorPrint(tabulators);
        System.out.println("Svi konobari u restoranu:");
        for(int i=0;i<this.waiters.length;i++) {
            Output.tabulatorPrint(tabulators+1);
            System.out.println("Konobar "+(i+1)+":");
            this.waiters[i].print(tabulators+2);
        }

        Output.tabulatorPrint(tabulators);
        System.out.println("Svi dostavljači u restoranu:");
        for(int i=0;i<this.deliverers.length;i++) {
            Output.tabulatorPrint(tabulators+1);
            System.out.println("Dostavljač "+(i+1)+":");
            this.deliverers[i].print(tabulators+2);
        }
    }
}