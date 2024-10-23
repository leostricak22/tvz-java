package hr.java.restaurant.model;

import hr.java.service.Input;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Scanner;

public class Order {
    private Restaurant restaurant;
    private Meal[] meals;
    private Deliverer deliverer;
    private LocalDateTime deliveryDateAndTime;

    public Order(Restaurant restaurant, Meal[] meals, Deliverer deliverer, LocalDateTime deliveryDateAndTime) {
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

    public static Order[] findMostExpensiveOrders(Order[] orders) {
        Order[] mostExpensiveOrders = new Order[orders.length];
        Integer numberOfMostExpensiveOrders = 0;

        BigDecimal mostExpensive = BigDecimal.valueOf(0);
        BigDecimal totalMealPrice;
        for (int i = 0; i < orders.length; i++) {
            totalMealPrice = BigDecimal.valueOf(0);

            for (int j = 0; j < orders[i].getMeals().length; j++) {
                totalMealPrice.add(orders[i].getMeals()[j].getPrice());
            }

            if(totalMealPrice.compareTo(mostExpensive) == 0) { // -1 <, 0 =, 1 >
                mostExpensiveOrders[numberOfMostExpensiveOrders] = orders[i];
                numberOfMostExpensiveOrders++;
            } else if (totalMealPrice.compareTo(mostExpensive) > 0) {
                numberOfMostExpensiveOrders=0;
                mostExpensiveOrders[numberOfMostExpensiveOrders] = orders[i];
                mostExpensive = totalMealPrice;
            }
        }

        Order[] mostExpensiveOrdersReturn = new Order[numberOfMostExpensiveOrders];
        for (int i = 0; i < numberOfMostExpensiveOrders; i++) {
            mostExpensiveOrdersReturn[i] = mostExpensiveOrders[i];
        }

        return mostExpensiveOrdersReturn;
    }

    public void print() {
        Input.tabulatorPrint(1);
        System.out.println("Restoran:");
        this.restaurant.print(2);

        Input.tabulatorPrint(1);
        System.out.println("Naručena jela:");
        for(int i=0;i<this.meals.length;i++) {
            Input.tabulatorPrint(2);
            System.out.println("Jelo "+(i+1)+":");
            this.meals[i].print(3);
        }

        Input.tabulatorPrint(1);
        System.out.print("Dostavljač narudžbe: ");
        this.deliverer.print(0);
        Input.tabulatorPrint(1);
        System.out.print("Datum dostave: ");
        System.out.println(this.deliveryDateAndTime);
    }
}
