package hr.java.restaurant.repository;

import hr.java.restaurant.model.*;
import hr.java.restaurant.util.EntityFinder;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class RestaurantRepository<T extends Restaurant> extends AbstractRepository<T> {
    public final static String FILE_PATH = "dat/restaurants.txt";

    private final ChefRepository<Chef> chefRepository = new ChefRepository<>();
    private final WaiterRepository<Waiter> waiterRepository = new WaiterRepository<>();
    private final DelivererRepository<Deliverer> delivererRepository = new DelivererRepository<>();
    private final AddressRepository addressRepository = new AddressRepository();

    @Override
    public T findById(Long id) {
        return findAll().stream()
                .filter(restaurant -> restaurant.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nepostojeći identifikator"));
    }

    public T findByName(String name) {
        return findAll().stream()
                .filter(restaurant -> restaurant.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nepostojeći restoran"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<T> findAll() {
        Set<T> restaurants = new HashSet<>();

        try (Stream<String> stream = Files.lines(Path.of(FILE_PATH))) {
            List<String> fileRows = stream.toList();

            while (!fileRows.isEmpty()) {
                Long id = Long.parseLong(fileRows.get(0));
                String name = fileRows.get(1);

                Long addressId = Long.parseLong(fileRows.get(2));

                String mealsInRestaurantIdentifiers = fileRows.get(3);
                String chefsInRestaurantIdentifiers = fileRows.get(4);
                String waitersInRestaurantIdentifiers = fileRows.get(5);
                String deliverersInRestaurantIdentifiers = fileRows.get(6);

                Set<Meal> mealsInRestaurant = EntityFinder.getMealByIdentifiers(mealsInRestaurantIdentifiers);
                Set<Chef> chefsInRestaurant = Person.getPersonByIdentifiers(chefsInRestaurantIdentifiers, chefRepository.findAll());
                Set<Waiter> waitersInRestaurant = Person.getPersonByIdentifiers(waitersInRestaurantIdentifiers, waiterRepository.findAll());
                Set<Deliverer> deliverersInRestaurant = Person.getPersonByIdentifiers(deliverersInRestaurantIdentifiers, delivererRepository.findAll());

                restaurants.add((T) new Restaurant(id, name, addressRepository.findById(addressId)
                        ,mealsInRestaurant, chefsInRestaurant, waitersInRestaurant, deliverersInRestaurant));

                fileRows = fileRows.subList(7, fileRows.size());
            }
        } catch (IOException e) {
            System.err.println("Greška pri čitanju datoteke: " + e.getMessage());
        }

        return restaurants;
    }

    @Override
    public void save(Set<T> entities) {
        try (PrintWriter printWriter = new PrintWriter(FILE_PATH)) {
            for (T restaurant : entities) {
                printWriter.println(restaurant.getId());
                printWriter.println(restaurant.getName());
                printWriter.println(restaurant.getAddress().getId());
                printWriter.println(EntityFinder.getMealIdentifiers(restaurant.getMeals()));
                printWriter.println(EntityFinder.getPersonIdentifiers(restaurant.getChefs()));
                printWriter.println(EntityFinder.getPersonIdentifiers(restaurant.getWaiters()));
                printWriter.println(EntityFinder.getPersonIdentifiers(restaurant.getDeliverers()));
            }

            printWriter.flush();
        } catch (IOException e) {
            System.err.println("Greška pri zapisivanju u datoteku: " + e.getMessage());
        }
    }

    @Override
    public Long findNextId() {
        return findAll().stream()
                .map(Restaurant::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }

    @Override
    public void save(T entity) {
        Set<T> restaurants = findAll();
        restaurants.add(entity);
        save(restaurants);
    }
}
