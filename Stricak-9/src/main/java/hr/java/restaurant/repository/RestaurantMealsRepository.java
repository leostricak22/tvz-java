package hr.java.restaurant.repository;

import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Meal;
import hr.java.restaurant.model.Restaurant;
import hr.java.restaurant.util.DatabaseUtil;
import hr.java.restaurant.util.ObjectMapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class RestaurantMealsRepository {

    public static Set<Meal> getMealsForRestaurant(Long restaurantId) {
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
                Meal meal = ObjectMapper.mapResultSetToMeal(resultSet);
                meals.add(meal);
            }

            return meals;
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    public static void saveMealsForRestaurant(Restaurant restaurant) {
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
}
