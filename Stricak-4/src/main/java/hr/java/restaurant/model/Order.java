package hr.java.restaurant.model;

import hr.java.service.EntityFinder;
import hr.java.service.Input;
import hr.java.service.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Scanner;

/**
 * Represents an order.
 */
public class Order extends Entity {
    private static Long counter = 0L;
    private static final Logger logger = LoggerFactory.getLogger(Order.class);

    private Restaurant restaurant;
    private final Meal[] meals;
    private final Deliverer deliverer;
    private final LocalDateTime deliveryDateAndTime;

    /**
     * Constructs an Order object.
     * @param restaurant the restaurant
     * @param meals the meals
     * @param deliverer the deliverer
     * @param deliveryDateAndTime the delivery date and time
     */
    public Order(Restaurant restaurant, Meal[] meals, Deliverer deliverer, LocalDateTime deliveryDateAndTime) {
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

    public Deliverer getDeliverer() {
        return deliverer;
    }

    /**
     * Inputs the order information.
     * @param orders the orders
     * @param restaurants the restaurants
     * @param scanner the scanner object used for input
     */
    public static void inputOrder(Order[] orders, Restaurant[] restaurants, Scanner scanner) {
        for (int i = 0; i < orders.length; i++) {
            logger.info("Order input");
            Restaurant orderRestaurant = EntityFinder.restaurantName(scanner, "Unesite naziv restorana " + (i + 1) + ". narudžbe", restaurants);

            int numberOfMeals = Input.integer(scanner, "Unesite broj jela koji želite dodati: ");
            Meal[] mealsEntered = new Meal[numberOfMeals];
            for(int j=0;j<numberOfMeals;j++) {
                mealsEntered[j] = EntityFinder.mealName(scanner,"Unesite naziv "+(j+1)+". jela koje želite dodati: ", orderRestaurant.getMeals());
            }

            Deliverer orderDeliverer = EntityFinder.delivererName(scanner,"Unesite ime i prezime (odvojeno razmakom) dostavljača kojeg želite dodati: ", orderRestaurant.getDeliverers());
            LocalDateTime orderDeliveryDateAndTime = Input.localDateTime(scanner, "Unesite datum dostave narudžbe. (primjer formata: yyyy-MM-ddTHH:mm:ss)");

            orders[i] = new Order(orderRestaurant, mealsEntered, orderDeliverer, orderDeliveryDateAndTime);
        }
    }

    /**
     * Calculates the total price of the meals in the order.
     * @return the total price of the meals
     */
    public BigDecimal totalMealPrice() {
        BigDecimal total = BigDecimal.valueOf(0);

        for (int j = 0; j < this.meals.length; j++) {
            total = total.add(this.meals[j].getPrice());
        }

        return total;
    }

    /**
     * Finds the most expensive orders.
     * @param orders the orders
     * @return the most expensive orders
     */
    public static Order[] findMostExpensiveOrders(Order[] orders) {
        Order[] mostExpensiveOrders = new Order[orders.length];
        int counterOfMostExpensiveOrders = 0;

        BigDecimal mostExpensive = BigDecimal.valueOf(0);
        BigDecimal totalPrice;
        for (Order order : orders) {
            totalPrice = order.totalMealPrice();

            if (totalPrice.compareTo(mostExpensive) == 0) { // -1 <, 0 =, 1 >
                mostExpensiveOrders[counterOfMostExpensiveOrders] = order;
                counterOfMostExpensiveOrders++;
            } else if (totalPrice.compareTo(mostExpensive) > 0) {
                mostExpensiveOrders[0] = order;
                counterOfMostExpensiveOrders = 1;
                mostExpensive = totalPrice;
            }
        }

        Order[] mostExpensiveOrdersReturn = new Order[counterOfMostExpensiveOrders];
        for (int i = 0; i < counterOfMostExpensiveOrders; i++)
            mostExpensiveOrdersReturn[i] = mostExpensiveOrders[i];

        return mostExpensiveOrdersReturn;
    }

    /**
     * Finds the deliverers with the most deliveries.
     * @param orders the orders
     * @return the deliverers with the most deliveries
     */
    public static Deliverer[] findDeliverersWithMostDeliveries(Order[] orders) {
        Deliverer[] deliverersWithMostDeliveries = new Deliverer[orders.length];
        int counterOfDeliverersWithMostDeliveries = 0;
        Integer mostDeliveries = 0;

        for (Order order : orders) {
            Deliverer orderDeliverer = order.deliverer;

            boolean found = false;
            for (int j = 0; j < counterOfDeliverersWithMostDeliveries; j++) {
                if (deliverersWithMostDeliveries[j].equals(orderDeliverer)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                Integer numberOfDeliveries = orderDeliverer.findNumberOfDeliveries(orders);

                if (Objects.equals(numberOfDeliveries, mostDeliveries)) {
                    deliverersWithMostDeliveries[counterOfDeliverersWithMostDeliveries] = orderDeliverer;
                    counterOfDeliverersWithMostDeliveries++;
                } else if (numberOfDeliveries > mostDeliveries) {
                    deliverersWithMostDeliveries[counterOfDeliverersWithMostDeliveries] = orderDeliverer;
                    counterOfDeliverersWithMostDeliveries = 1;
                    mostDeliveries = numberOfDeliveries;
                }
            }
        }

        Deliverer[] deliverersWithMostDeliveriesReturn = new Deliverer[counterOfDeliverersWithMostDeliveries];
        for (int i = 0; i < counterOfDeliverersWithMostDeliveries; i++) {
            deliverersWithMostDeliveriesReturn[i] = deliverersWithMostDeliveries[i];
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
        for(int i=0;i<this.meals.length;i++) {
            Output.tabulatorPrint(2);
            System.out.println("Jelo "+(i+1)+":");
            this.meals[i].print(3);
        }

        Output.tabulatorPrint(1);
        System.out.print("Dostavljač narudžbe: ");
        this.deliverer.print(0);
        Output.tabulatorPrint(1);
        System.out.print("Datum dostave: ");
        System.out.println(this.deliveryDateAndTime);
    }
}
