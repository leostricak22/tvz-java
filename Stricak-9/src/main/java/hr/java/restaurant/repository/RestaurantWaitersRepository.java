package hr.java.restaurant.repository;

import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Restaurant;
import hr.java.restaurant.model.Waiter;
import hr.java.restaurant.util.DatabaseUtil;
import hr.java.restaurant.util.ObjectMapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class RestaurantWaitersRepository {

    public static Set<Waiter> getWaitersForRestaurant(Long restaurantId) {
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
                Waiter waiter = ObjectMapper.mapResultSetToWaiter(resultSet);
                waiters.add(waiter);
            }

            return waiters;
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    public static void saveWaitersForRestaurant(Restaurant restaurant) {
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
}
