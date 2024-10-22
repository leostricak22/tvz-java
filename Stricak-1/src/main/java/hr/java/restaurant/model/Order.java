package hr.java.restaurant.model;

import hr.java.service.Input;

import java.time.LocalDateTime;
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
}
