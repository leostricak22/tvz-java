package hr.java.restaurant.model;

import hr.java.service.Constants;
import hr.java.service.EntityFinder;
import hr.java.service.Input;
import hr.java.service.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents an order.
 */
public class Order extends Entity implements Serializable {
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
    public Order(Long id, Restaurant restaurant, List<Meal> meals, Deliverer deliverer, LocalDateTime deliveryDateAndTime) {
        super(id, "Oder " + id);
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

    public LocalDateTime getDeliveryDateAndTime() {
        return deliveryDateAndTime;
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

    public static void serializeToFile(Set<Order> orders) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dat/orders.dat"));
            out.writeObject(orders);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Set<Order> deserializeFromFile() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("dat/orders.dat"));

            System.out.println(1);

            Set<Order> orders  = (Set<Order>) in.readObject();
            System.out.println("Podaci o pročitanom objektu:");
            for (Order order : orders) {
                order.print();
            }

            in.close();

            return orders;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
