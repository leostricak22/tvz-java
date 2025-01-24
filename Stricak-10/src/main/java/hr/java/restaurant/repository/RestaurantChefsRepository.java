package hr.java.restaurant.repository;

import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Chef;
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

public class RestaurantChefsRepository {

    public static void saveChefsForRestaurant(Restaurant restaurant) {
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

    public static Set<Chef> getChefsForRestaurant(Long restaurantId) {
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
                Chef chef = ObjectMapper.mapResultSetToChef(resultSet);
                chefs.add(chef);
            }

            return chefs;
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }
}
