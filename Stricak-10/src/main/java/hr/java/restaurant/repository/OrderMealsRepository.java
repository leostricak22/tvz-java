package hr.java.restaurant.repository;

import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Meal;
import hr.java.restaurant.model.Order;
import hr.java.restaurant.model.dbo.MealDatabaseResponse;
import hr.java.restaurant.util.DatabaseUtil;
import hr.java.restaurant.util.ObjectMapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class OrderMealsRepository {

    public static void saveMealsForOrder(Order order) {
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

    public static Set<Meal> getMealsForOrder(Long orderId) {
        Set<MealDatabaseResponse> mealDatabaseResponses = new HashSet<>();

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
                mealDatabaseResponses.add(ObjectMapper.mapResultSetToMealDatabaseResponse(resultSet));
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }

        return ObjectMapper.mapMealDatabaseResponsesToMeals(mealDatabaseResponses);
    }
}
