package hr.java.restaurant.repository;

import hr.java.restaurant.model.*;
import hr.java.restaurant.util.EntityFinder;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class OrderRepository<T extends Order> extends AbstractRepository<T> {
    public final static String FILE_PATH = "dat/orders.txt";

    private final DelivererRepository<Deliverer> delivererRepository = new DelivererRepository<>();
    private final RestaurantRepository<Restaurant> restaurantRepository = new RestaurantRepository<>();

    @Override
    public T findById(Long id) {
        return findAll().stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nepostojeći identifikator"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<T> findAll() {
        Set<T> orders = new HashSet<>();

        try (Stream<String> stream = Files.lines(Path.of(FILE_PATH))) {
            List<String> fileRows = stream.toList();

            while (!fileRows.isEmpty()) {
                Long id = Long.parseLong(fileRows.get(0));

                String restaurantIdentifier = fileRows.get(1);
                String mealIdentifier = fileRows.get(2);

                String delivererIdentifier = fileRows.get(3);
                LocalDateTime deliveryDateAndTime = LocalDateTime.parse(fileRows.get(4));

                Restaurant restaurant = restaurantRepository.findById(Long.parseLong(restaurantIdentifier));
                Set<Meal> meals = EntityFinder.getMealByIdentifiers(mealIdentifier);
                Deliverer deliverer = new ArrayList<>(Person.getPersonByIdentifiers(delivererIdentifier, delivererRepository.findAll())).getFirst();


                orders.add((T) new Order(id, restaurant, new ArrayList<>(meals), deliverer, deliveryDateAndTime));

                fileRows = fileRows.subList(5, fileRows.size());
            }
        } catch (IOException e) {
            System.err.println("Greška pri čitanju datoteke: " + e.getMessage());
        }

        return orders;
    }

    @Override
    public void save(Set<T> entities) {
        try (PrintWriter printWriter = new PrintWriter(FILE_PATH)) {
            for (T order : entities) {
                printWriter.println(order.getId());
                printWriter.println(order.getRestaurant().getId());
                printWriter.println(EntityFinder.getMealIdentifiers(new HashSet<>(order.getMeals())));
                printWriter.println(order.getDeliverer().getId());
                printWriter.println(order.getDeliveryDateAndTime());
            }

            printWriter.flush();
        } catch (IOException e) {
            System.err.println("Greška pri zapisivanju u datoteku: " + e.getMessage());
        }
    }

    @Override
    public Long findNextId() {
        return findAll().stream()
                .map(Order::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }

    @Override
    public void save(T order) {
        Set<T> orders = findAll();
        orders.add(order);
        save(orders);
    }
}
