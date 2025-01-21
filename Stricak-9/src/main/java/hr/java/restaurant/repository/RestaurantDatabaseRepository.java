package hr.java.restaurant.repository;

import hr.java.restaurant.exception.EmptyRepositoryResultException;
import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.*;
import hr.java.restaurant.util.DatabaseUtil;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class RestaurantDatabaseRepository extends AbstractRepository<Restaurant> {

    private final AddressRepository addressRepository = new AddressRepository();
    private final MealDatabaseRepository mealRepository = new MealDatabaseRepository();
    private final ChefDatabaseRepository chefRepository = new ChefDatabaseRepository();
    private final WaiterDatabaseRepository waiterRepository = new WaiterDatabaseRepository();
    private final DelivererDatabaseRepository delivererRepository = new DelivererDatabaseRepository();

    @Override
    public Restaurant findById(Long id) throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RESTAURANT WHERE ID = ?;");
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToRestaurant(resultSet);
            } else {
                throw new EmptyRepositoryResultException("Restaurant with id " + id + " not found");
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    public Restaurant findByName(String name) throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RESTAURANT WHERE NAME = ?;");
            stmt.setString(1, name);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToRestaurant(resultSet);
            } else {
                return null;
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public Set<Restaurant> findAll() throws RepositoryAccessException {
        Set<Restaurant> restaurants = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM RESTAURANT;");

            while (resultSet.next()) {
                Restaurant restaurant = mapResultSetToRestaurant(resultSet);
                restaurants.add(restaurant);
            }
            return restaurants;
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public void save(Set<Restaurant> entities) throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("""
                INSERT INTO RESTAURANT (NAME, ADDRESS_ID) VALUES (?, ?);
                """, Statement.RETURN_GENERATED_KEYS);

            for (Restaurant restaurant : entities) {
                stmt.setString(1, restaurant.getName());
                stmt.setLong(2, restaurant.getAddress().getId());
                stmt.executeUpdate();

                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    restaurant.setId(generatedKeys.getLong(1));
                }

                saveMealsForRestaurant(restaurant);
                saveChefsForRestaurant(restaurant);
                saveWaitersForRestaurant(restaurant);
                saveDeliverersForRestaurant(restaurant);
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public Long findNextId() throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT MAX(ID) FROM RESTAURANT;");

            if (resultSet.next()) {
                return resultSet.getLong(1) + 1;
            } else {
                return 1L;
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    private void saveMealsForRestaurant(Restaurant restaurant) {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            for (Meal meal : restaurant.getMeals()) {
                PreparedStatement stmt = connection.prepareStatement("""
                    INSERT INTO RESTAURANT_MEAL (RESTAURANT_ID, MEAL_ID) VALUES (?, ?);
                    """);

                stmt.setLong(1, restaurant.getId());
                stmt.setLong(2, meal.getId());
                stmt.executeUpdate();
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    private void saveChefsForRestaurant(Restaurant restaurant) {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            for (Chef chef : restaurant.getChefs()) {
                PreparedStatement stmt = connection.prepareStatement("""
                    INSERT INTO RESTAURANT_CHEF (RESTAURANT_ID, CHEF_ID) VALUES (?, ?);
                    """);

                stmt.setLong(1, restaurant.getId());
                stmt.setLong(2, chef.getId());
                stmt.executeUpdate();
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    private void saveWaitersForRestaurant(Restaurant restaurant) {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            for (Waiter waiter : restaurant.getWaiters()) {
                PreparedStatement stmt = connection.prepareStatement("""
                    INSERT INTO RESTAURANT_WAITER (RESTAURANT_ID, WAITER_ID) VALUES (?, ?);
                    """);

                stmt.setLong(1, restaurant.getId());
                stmt.setLong(2, waiter.getId());
                stmt.executeUpdate();
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    private void saveDeliverersForRestaurant(Restaurant restaurant) {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            for (Deliverer deliverer : restaurant.getDeliverers()) {
                PreparedStatement stmt = connection.prepareStatement("""
                    INSERT INTO RESTAURANT_DELIVERER (RESTAURANT_ID, DELIVERER_ID) VALUES (?, ?);
                    """);

                stmt.setLong(1, restaurant.getId());
                stmt.setLong(2, deliverer.getId());
                stmt.executeUpdate();
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    private Set<Meal> getMealsForRestaurant(Long restaurantId) {
        Set<Meal> meals = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("""
                SELECT m.*
                FROM RESTAURANT_MEAL rm
                JOIN MEAL m ON rm.MEAL_ID = m.ID
                WHERE rm.RESTAURANT_ID = ?;
                """);

            stmt.setLong(1, restaurantId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Meal meal = mealRepository.mapResultSetToMeal(resultSet);
                meals.add(meal);
            }

            return meals;
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    private Set<Chef> getChefsForRestaurant(Long restaurantId) {
        Set<Chef> chefs = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("""
                SELECT c.*
                FROM RESTAURANT_CHEF rc
                JOIN CHEF c ON rc.CHEF_ID = c.ID
                WHERE rc.RESTAURANT_ID = ?;
                """);

            stmt.setLong(1, restaurantId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Chef chef = chefRepository.mapResultSetToChef(resultSet);
                chefs.add(chef);
            }

            return chefs;
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    private Set<Waiter> getWaitersForRestaurant(Long restaurantId) {
        Set<Waiter> waiters = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("""
                SELECT w.*
                FROM RESTAURANT_WAITER rw
                JOIN WAITER w ON rw.WAITER_ID = w.ID
                WHERE rw.RESTAURANT_ID = ?;
                """);

            stmt.setLong(1, restaurantId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Waiter waiter = waiterRepository.mapResultSetToWaiter(resultSet);
                waiters.add(waiter);
            }

            return waiters;
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    private Set<Deliverer> getDeliverersForRestaurant(Long restaurantId) {
        Set<Deliverer> deliverers = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("""
                SELECT d.*
                FROM RESTAURANT_DELIVERER rd
                JOIN DELIVERER d ON rd.DELIVERER_ID = d.ID
                WHERE rd.RESTAURANT_ID = ?;
                """);

            stmt.setLong(1, restaurantId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Deliverer deliverer = delivererRepository.mapResultSetToDeliverer(resultSet);
                deliverers.add(deliverer);
            }

            return deliverers;
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    private Restaurant mapResultSetToRestaurant(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("ID");
        String name = resultSet.getString("NAME");
        Address address = addressRepository.findById(resultSet.getLong("ADDRESS_ID"));

        Set<Meal> meals = getMealsForRestaurant(id);
        Set<Chef> chefs = getChefsForRestaurant(id);
        Set<Waiter> waiters = getWaitersForRestaurant(id);
        Set<Deliverer> deliverers = getDeliverersForRestaurant(id);

        return new Restaurant(id, name, address, meals, chefs, waiters, deliverers);
    }
}
