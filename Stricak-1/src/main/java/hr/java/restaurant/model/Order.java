package hr.java.restaurant.model;

import hr.java.service.Input;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Order {

    private final static Duration DEFAULT_ALLOWED_CANCEL_PERIOD = Duration.ofSeconds(15);

    private Restaurant restaurant;
    private Meal[] meals;
    private Deliverer deliverer;
    private LocalDateTime deliveryDateAndTime;

    private LocalDateTime createdDateAndTime = LocalDateTime.now();
    private LocalDateTime canceledDateAndTime;
    private String canceledMessage;

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

    public LocalDateTime getCreatedDateAndTime() {
        return createdDateAndTime;
    }

    public void setCreatedDateAndTime(LocalDateTime createdDateAndTime) {
        this.createdDateAndTime = createdDateAndTime;
    }

    public LocalDateTime getCanceledDateAndTime() {
        return canceledDateAndTime;
    }

    public void setCanceledDateAndTime(LocalDateTime canceledDateAndTime) {
        this.canceledDateAndTime = canceledDateAndTime;
    }

    public String getCanceledMessage() {
        return canceledMessage;
    }

    public void setCanceledMessage(String canceledMessage) {
        this.canceledMessage = canceledMessage;
    }

    public Boolean wasAllowedCancel() {
        return Duration.between(this.createdDateAndTime, LocalDateTime.now()).compareTo(DEFAULT_ALLOWED_CANCEL_PERIOD) < 0;
    }

    public void cancelOrder(String canceledMessage) {
        if(!wasAllowedCancel()) {
            System.out.println("Prošlo je dopušteno vrijeme za otkazivanje narudžbe.");
        } else {
            this.canceledDateAndTime = LocalDateTime.now();
            this.canceledMessage = canceledMessage;
        }
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

                if(numberOfDeliveries == mostDeliveries) {
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

        Input.tabulatorPrint(1);
        System.out.print("Datum kreiranja narudžbe: ");
        System.out.println(this.createdDateAndTime);

        if(this.canceledDateAndTime != null) {
            Input.tabulatorPrint(1);
            System.out.print("Datum otkazivanja narudžbe: ");
            System.out.println(this.canceledDateAndTime);

            Input.tabulatorPrint(1);
            System.out.print("Razlog otkazivanja narudžbe: ");
            System.out.println(this.canceledMessage);
        }
    }
}
