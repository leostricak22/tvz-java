package hr.java.restaurant.repository;

import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Deliverer;
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

public class RestaurantDeliverersRepository {

    public static void saveDeliverersForRestaurant(Restaurant restaurant) {
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

    public static Set<Deliverer> getDeliverersForRestaurant(Long restaurantId) {
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
                Deliverer deliverer = ObjectMapper.mapResultSetToDeliverer(resultSet);
                deliverers.add(deliverer);
            }

            return deliverers;
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }
}
