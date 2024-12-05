package hr.java.restaurant.model;

import hr.java.service.EntityFinder;
import hr.java.service.Input;
import hr.java.service.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents an order.
 */
public class Order extends Entity {
    private static Long counter = 0L;
    private static final Logger logger = LoggerFactory.getLogger(Order.class);

    private Restaurant restaurant;
    private List<Meal> meals;
    private final Deliverer deliverer;
    private final LocalDateTime deliveryDateAndTime;

    /**
     * Constructs an Order object.
     * @param restaurant the restaurant
     * @param meals the meals
     * @param deliverer the deliverer
     * @param deliveryDateAndTime the delivery date and time
     */
    public Order(Restaurant restaurant, List<Meal> meals, Deliverer deliverer, LocalDateTime deliveryDateAndTime) {
        super(++counter);
        this.restaurant = restaurant;
        this.meals = meals;
        this.deliverer = deliverer;
        this.deliveryDateAndTime = deliveryDateAndTime;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<Meal> getMeals() {
        return this.meals;
    }

    public Deliverer getDeliverer() {
        return deliverer;
    }

    /**
     * Inputs the order information.
     * @param numOfElements the orders
     * @param restaurants the restaurants
     * @param scanner the scanner object used for input
     */
    public static List<Order> inputOrder(int numOfElements, List<Restaurant> restaurants, Scanner scanner) {
        List<Order> orders = new ArrayList<>();

        for (int i = 0; i < numOfElements; i++) {
            logger.info("Order input");
            Restaurant orderRestaurant = EntityFinder.restaurantName(scanner, "Unesite naziv restorana " + (i + 1) + ". narudžbe", restaurants);

            int numberOfMeals = Input.integer(scanner, "Unesite broj jela koji želite dodati: ");
            List<Meal> mealsEntered = new ArrayList<>();
            for(int j=0;j<numberOfMeals;j++) {
                mealsEntered.add(EntityFinder.mealName(scanner,"Unesite naziv "+(j+1)+". jela koje želite dodati: ", orderRestaurant.getMeals()));
            }

            Deliverer orderDeliverer = EntityFinder.delivererName(scanner,"Unesite ime i prezime (odvojeno razmakom) dostavljača kojeg želite dodati: ", orderRestaurant.getDeliverers());
            LocalDateTime orderDeliveryDateAndTime = Input.localDateTime(scanner, "Unesite datum dostave narudžbe. (primjer formata: yyyy-MM-ddTHH:mm:ss)");

            orders.add(new Order(orderRestaurant, mealsEntered, orderDeliverer, orderDeliveryDateAndTime));
        }

        return orders;
    }

    /**
     * Calculates the total price of the meals in the order.
     * @return the total price of the meals
     */
    public BigDecimal totalMealPrice() {
        BigDecimal total = BigDecimal.valueOf(0);

        for (Meal meal : this.meals) {
            total = total.add(meal.getPrice());
        }

        return total;
    }

    /**
     * Finds the most expensive orders.
     * @param orders the orders
     * @return the most expensive orders
     */
    public static List<Order> findMostExpensiveOrders(List<Order> orders) {
        List<Order> mostExpensiveOrders = new ArrayList<>();
        int counterOfMostExpensiveOrders = 0;

        BigDecimal mostExpensive = BigDecimal.valueOf(0);
        BigDecimal totalPrice;
        for (Order order : orders) {
            totalPrice = order.totalMealPrice();

            if (totalPrice.compareTo(mostExpensive) == 0) { // -1 <, 0 =, 1 >
                mostExpensiveOrders.set(counterOfMostExpensiveOrders, order);
                counterOfMostExpensiveOrders++;
            } else if (totalPrice.compareTo(mostExpensive) > 0) {
                mostExpensiveOrders.set(0, order);
                counterOfMostExpensiveOrders = 1;
                mostExpensive = totalPrice;
            }
        }

        List<Order> mostExpensiveOrdersReturn = new ArrayList<>();
        for (int i = 0; i < counterOfMostExpensiveOrders; i++)
            mostExpensiveOrdersReturn.add(mostExpensiveOrders.get(i));

        return mostExpensiveOrdersReturn;
    }

    /**
     * Finds the deliverers with the most deliveries.
     * @param orders the orders
     * @return the deliverers with the most deliveries
     */
    public static List<Deliverer> findDeliverersWithMostDeliveries(List<Order> orders) {
        List<Deliverer> deliverersWithMostDeliveries = new ArrayList<>();
        int counterOfDeliverersWithMostDeliveries = 0;
        Integer mostDeliveries = 0;

        for (Order order : orders) {
            Deliverer orderDeliverer = order.deliverer;

            boolean found = false;
            for (int j = 0; j < counterOfDeliverersWithMostDeliveries; j++) {
                if (deliverersWithMostDeliveries.get(j).equals(orderDeliverer)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                Integer numberOfDeliveries = orderDeliverer.findNumberOfDeliveries(orders);

                if (Objects.equals(numberOfDeliveries, mostDeliveries)) {
                    deliverersWithMostDeliveries.set(counterOfDeliverersWithMostDeliveries, orderDeliverer);
                    counterOfDeliverersWithMostDeliveries++;
                } else if (numberOfDeliveries > mostDeliveries) {
                    deliverersWithMostDeliveries.set(counterOfDeliverersWithMostDeliveries, orderDeliverer);
                    counterOfDeliverersWithMostDeliveries = 1;
                    mostDeliveries = numberOfDeliveries;
                }
            }
        }

        List<Deliverer> deliverersWithMostDeliveriesReturn = new ArrayList<>();
        for (int i = 0; i < counterOfDeliverersWithMostDeliveries; i++) {
            deliverersWithMostDeliveriesReturn.add(deliverersWithMostDeliveries.get(i));
        }

        return deliverersWithMostDeliveriesReturn;
    }

    /**
     * Prints order
     */
    public void print() {
        logger.info("Printing order.");
        Output.tabulatorPrint(1);
        System.out.println("Id: " + this.getId());

        Output.tabulatorPrint(1);
        System.out.println("Restoran:");
        this.restaurant.print(2);

        Output.tabulatorPrint(1);
        System.out.println("Naručena jela:");
        for(int i=0;i<this.meals.size();i++) {
            Output.tabulatorPrint(2);
            System.out.println("Jelo "+(i+1)+":");
            this.meals.get(i).print(3);
        }

        Output.tabulatorPrint(1);
        System.out.print("Dostavljač narudžbe: ");
        this.deliverer.print(0);
        Output.tabulatorPrint(1);
        System.out.print("Datum dostave: ");
        System.out.println(this.deliveryDateAndTime);
    }
}
