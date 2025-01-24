package hr.java.restaurant.repository;

import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.*;
import hr.java.restaurant.model.dbo.OrderDatabaseResponse;
import hr.java.restaurant.util.DatabaseUtil;
import hr.java.restaurant.util.ObjectMapper;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class OrderRepository extends AbstractRepository<Order> {

    @Override
    public  synchronized Order findById(Long id) throws RepositoryAccessException {
        while (DatabaseUtil.activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        DatabaseUtil.activeConnectionWithDatabase = true;
        OrderDatabaseResponse orderDatabaseResponse;

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RESTAURANT_ORDER WHERE ID = ?;");
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                orderDatabaseResponse = ObjectMapper.mapResultSetToOrderDatabaseResponse(resultSet);
            } else {
                throw new RepositoryAccessException("Order with id " + id + " not found");
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        } finally {
            DatabaseUtil.activeConnectionWithDatabase = false;
            notifyAll();
        }

        return ObjectMapper.mapOrderDatabaseResponseToOrder(orderDatabaseResponse);
    }

    @Override
    public synchronized Set<Order> findAll() throws RepositoryAccessException {
        while (DatabaseUtil.activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        DatabaseUtil.activeConnectionWithDatabase = true;
        Set<OrderDatabaseResponse> orderDatabaseResponses = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM RESTAURANT_ORDER;");

            while (resultSet.next()) {
                orderDatabaseResponses.add(ObjectMapper.mapResultSetToOrderDatabaseResponse(resultSet));
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        } finally {
            DatabaseUtil.activeConnectionWithDatabase = false;
            notifyAll();
        }

        return ObjectMapper.mapOrderDatabaseResponsesToOrders(orderDatabaseResponses);
    }

    @Override
    public synchronized void save(Set<Order> entities) throws RepositoryAccessException {
        while (DatabaseUtil.activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        DatabaseUtil.activeConnectionWithDatabase = true;

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
        } finally {
            DatabaseUtil.activeConnectionWithDatabase = false;
            notifyAll();
        }
    }

    @Override
    public synchronized Long findNextId() throws RepositoryAccessException {
        return 0L;
    }
}
