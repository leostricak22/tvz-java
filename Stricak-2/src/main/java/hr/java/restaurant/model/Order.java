package hr.java.restaurant.model;

import hr.java.service.Input;
import hr.java.service.Output;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Scanner;

public class Order extends Entity {
    private static Long counter = 0L;

    private Restaurant restaurant;
    private Meal[] meals;
    private Deliverer deliverer;
    private LocalDateTime deliveryDateAndTime;

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

    public Meal[] getMeals() {
        return meals;
    }

    public void setMeals(Meal[] meals) {
        this.meals = meals;
    }

    public Deliverer getDeliverer() {
        return deliverer;
    }

    public void setDeliverer(Deliverer deliverer) {
        this.deliverer = deliverer;
    }

    public LocalDateTime getDeliveryDateAndTime() {
        return deliveryDateAndTime;
    }

    public void setDeliveryDateAndTime(LocalDateTime deliveryDateAndTime) {
        this.deliveryDateAndTime = deliveryDateAndTime;
    }
    public static void inputOrder(Order[] orders, Restaurant[] restaurants, Meal[] meals, Scanner scanner) {
        for (int i = 0; i < orders.length; i++) {
            Restaurant orderRestaurant = Input.restaurantName(scanner, "Unesite naziv restorana " + (i + 1) + ". narudžbe", restaurants);

            Integer numberOfMeals = Input.integer(scanner, "Unesite broj jela koji želite dodati: ");
            Meal[] mealsEntered = new Meal[numberOfMeals];
            for(int j=0;j<numberOfMeals;j++) {
                mealsEntered[j] = Input.mealName(scanner,"Unesite naziv "+(j+1)+". jela koje želite dodati: ", orderRestaurant.getMeals());
            }

            Deliverer orderDeliverer = Input.delivererName(scanner,"Unesite ime i prezime (odvojeno razmakom) dostavljača kojeg želite dodati: ", orderRestaurant.getDeliverers());
            LocalDateTime orderDeliveryDateAndTime = Input.localDateTime(scanner, "Unesite datum dostave narudžbe. (primjer formata: yyyy-MM-ddTHH:mm:ss)");

            orders[i] = new Order(orderRestaurant, mealsEntered, orderDeliverer, orderDeliveryDateAndTime);
        }
    }

    public BigDecimal totalMealPrice() {
        BigDecimal total = BigDecimal.valueOf(0);

        for (int j = 0; j < this.meals.length; j++) {
            total = total.add(this.meals[j].getPrice());
        }

        return total;
    }

    public static Order[] findMostExpensiveOrders(Order[] orders) {
        Order[] mostExpensiveOrders = new Order[orders.length];
        Integer counterOfMostExpensiveOrders = 0;

        BigDecimal mostExpensive = BigDecimal.valueOf(0);
        BigDecimal totalPrice;
        for (int i = 0; i < orders.length; i++) {
            totalPrice = orders[i].totalMealPrice();

            if(totalPrice.compareTo(mostExpensive) == 0) { // -1 <, 0 =, 1 >
                mostExpensiveOrders[counterOfMostExpensiveOrders] = orders[i];
                counterOfMostExpensiveOrders++;
            } else if (totalPrice.compareTo(mostExpensive) > 0) {
                mostExpensiveOrders[0] = orders[i];
                counterOfMostExpensiveOrders =1;
                mostExpensive = totalPrice;
            }
        }

        Order[] mostExpensiveOrdersReturn = new Order[counterOfMostExpensiveOrders];
        for (int i = 0; i < counterOfMostExpensiveOrders; i++) {
            mostExpensiveOrdersReturn[i] = mostExpensiveOrders[i];
        }

        return mostExpensiveOrdersReturn;
    }

    public static Deliverer[] findDeliverersWithMostDeliveries(Order[] orders) {
        Deliverer[] deliverersWithMostDeliveries = new Deliverer[orders.length];
        Integer counterOfDeliverersWithMostDeliveries = 0;
        Integer mostDeliveries = 0;

        for(int i=0;i<orders.length;i++) {
            Deliverer orderDeliverer = orders[i].deliverer;

            boolean found = false;
            for(int j=0;j<counterOfDeliverersWithMostDeliveries;j++) {
                if(deliverersWithMostDeliveries[j].equals(orderDeliverer))
                    found = true;
            }

            if(!found) {
                Integer numberOfDeliveries = orderDeliverer.findNumberOfDeliveries(orders);

                if(Objects.equals(numberOfDeliveries, mostDeliveries)) {
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

    public void print() {
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
