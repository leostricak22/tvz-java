package hr.java.restaurant.repository;

import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Deliverer;
import hr.java.restaurant.model.Meal;
import hr.java.restaurant.model.Order;
import hr.java.restaurant.model.Restaurant;
import hr.java.restaurant.util.DatabaseUtil;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class OrderDatabaseRepository extends AbstractRepository<Order> {

    private final RestaurantDatabaseRepository restaurantRepository = new RestaurantDatabaseRepository();
    private final MealDatabaseRepository mealRepository = new MealDatabaseRepository();
    private final DelivererDatabaseRepository delivererRepository = new DelivererDatabaseRepository();

    @Override
    public Order findById(Long id) throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RESTAURANT_ORDER WHERE ID = ?;");
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToOrder(resultSet);
            } else {
                throw new RepositoryAccessException("Order with id " + id + " not found");
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public Set<Order> findAll() throws RepositoryAccessException {
        Set<Order> meals = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM RESTAURANT_ORDER;");

            while (resultSet.next()) {
                Order order = mapResultSetToOrder(resultSet);
                meals.add(order);
            }

            return meals;
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }    }

    @Override
    public void save(Set<Order> entities) throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("""
                INSERT INTO RESTAURANT_ORDER (RESTAURANT_ID, DELIVERER_ID, DATE_AND_TIME) VALUES (?, ?, ?);
                """, Statement.RETURN_GENERATED_KEYS);

            for (Order order : entities) {
                stmt.setLong(1, order.getRestaurant().getId());
                stmt.setLong(2, order.getDeliverer().getId());
                stmt.setTimestamp(3, Timestamp.valueOf(order.getDeliveryDateAndTime()));
                stmt.executeUpdate();

                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getLong(1));
                }

                saveMealsForOrder(order);
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public Long findNextId() throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT MAX(ID) FROM RESTAURANT_ORDER;");

            if (resultSet.next()) {
                return resultSet.getLong(1) + 1;
            } else {
                return 1L;
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    private void saveMealsForOrder(Order order) {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("""
                INSERT INTO RESTAURANT_ORDER_MEAL (RESTAURANT_ORDER_ID, MEAL_ID) VALUES (?, ?);
                """);

            for (Meal meal : order.getMeals()) {
                stmt.setLong(1, order.getId());
                stmt.setLong(2, meal.getId());
                stmt.executeUpdate();
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    private Set<Meal> getMealsForOrder(Long orderId) {
        Set<Meal> meals = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("""
                SELECT m.*
                FROM RESTAURANT_ORDER_MEAL rom
                JOIN MEAL m ON rom.MEAL_ID = m.ID
                WHERE rom.RESTAURANT_ORDER_ID = ?;
                """);

            stmt.setLong(1, orderId);
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

    private Order mapResultSetToOrder(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("ID");
        Restaurant restaurant = restaurantRepository.findById(resultSet.getLong("RESTAURANT_ID"));
        Set<Meal> meals = getMealsForOrder(id);
        Deliverer deliverer = delivererRepository.findById(resultSet.getLong("DELIVERER_ID"));
        LocalDateTime deliveryDateAndTime = resultSet.getTimestamp("DATE_AND_TIME").toLocalDateTime();

        return new Order(id, restaurant, new ArrayList<>(meals), deliverer, deliveryDateAndTime);
    }
}
