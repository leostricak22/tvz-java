package hr.java.restaurant.repository;

import hr.java.restaurant.model.*;

import java.io.IOException;
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

                String street = fileRows.get(2);
                String houseNumber = fileRows.get(3);
                String city = fileRows.get(4);
                String postalCode = fileRows.get(5);

                String mealsInRestaurantIdentifiers = fileRows.get(6);
                String chefsInRestaurantIdentifiers = fileRows.get(7);
                String waitersInRestaurantIdentifiers = fileRows.get(8);
                String deliverersInRestaurantIdentifiers = fileRows.get(9);

                Set<Meal> mealsInRestaurant = Meal.getMealByIdentifiers(mealsInRestaurantIdentifiers);
                Set<Chef> chefsInRestaurant = Person.getPersonByIdentifiers(chefsInRestaurantIdentifiers, chefRepository.findAll());
                Set<Waiter> waitersInRestaurant = Person.getPersonByIdentifiers(waitersInRestaurantIdentifiers, waiterRepository.findAll());
                Set<Deliverer> deliverersInRestaurant = Person.getPersonByIdentifiers(deliverersInRestaurantIdentifiers, delivererRepository.findAll());

                restaurants.add((T) new Restaurant(id, name, new Address.Builder().id(id).street(street).houseNumber(houseNumber).city(city).postalCode(postalCode).build(), mealsInRestaurant, chefsInRestaurant, waitersInRestaurant, deliverersInRestaurant));

                fileRows = fileRows.subList(10, fileRows.size());
            }
        } catch (IOException e) {
            System.err.println("Greška pri čitanju datoteke: " + e.getMessage());
        }

        return restaurants;
    }

    @Override
    public void save(Set<T> entity) {

    }
}
