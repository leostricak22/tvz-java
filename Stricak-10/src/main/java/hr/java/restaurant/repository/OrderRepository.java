package hr.java.restaurant.repository;

import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.*;
import hr.java.restaurant.util.DatabaseUtil;
import hr.java.restaurant.util.ObjectMapper;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class OrderRepository extends AbstractRepository<Order> {

    @Override
    public Order findById(Long id) throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RESTAURANT_ORDER WHERE ID = ?;");
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return ObjectMapper.mapResultSetToOrder(resultSet);
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
                Order order = ObjectMapper.mapResultSetToOrder(resultSet);
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

                OrderMealsRepository.saveMealsForOrder(order);
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
}
