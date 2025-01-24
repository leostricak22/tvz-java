package hr.java.restaurant.repository;

import hr.java.restaurant.exception.EmptyRepositoryResultException;
import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.*;
import hr.java.restaurant.model.dbo.RestaurantDatabaseResponse;
import hr.java.restaurant.util.DatabaseUtil;
import hr.java.restaurant.util.ObjectMapper;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class RestaurantRepository extends AbstractRepository<Restaurant> {

    @Override
    public synchronized Restaurant findById(Long id) throws RepositoryAccessException {
        while (DatabaseUtil.activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        DatabaseUtil.activeConnectionWithDatabase = true;
        RestaurantDatabaseResponse restaurantDatabaseResponse;

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RESTAURANT WHERE ID = ?;");
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                restaurantDatabaseResponse = ObjectMapper.mapResultSetToRestaurantDatabaseResponse(resultSet);
            } else {
                throw new EmptyRepositoryResultException("Restaurant with id " + id + " not found");
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        } finally {
            DatabaseUtil.activeConnectionWithDatabase = false;
            notifyAll();
        }

        return ObjectMapper.mapRestaurantDatabaseResponseToRestaurant(restaurantDatabaseResponse);
    }

    public synchronized Restaurant findByName(String name) throws RepositoryAccessException {
        while (DatabaseUtil.activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        DatabaseUtil.activeConnectionWithDatabase = true;
        RestaurantDatabaseResponse restaurantDatabaseResponse;

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RESTAURANT WHERE NAME = ?;");
            stmt.setString(1, name);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                restaurantDatabaseResponse = ObjectMapper.mapResultSetToRestaurantDatabaseResponse(resultSet);
            } else {
                return null;
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        } finally {
            DatabaseUtil.activeConnectionWithDatabase = false;
            notifyAll();
        }

        return ObjectMapper.mapRestaurantDatabaseResponseToRestaurant(restaurantDatabaseResponse);
    }

    @Override
    public synchronized Set<Restaurant> findAll() throws RepositoryAccessException {
        while (DatabaseUtil.activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        DatabaseUtil.activeConnectionWithDatabase = true;
        Set<RestaurantDatabaseResponse> restaurantDatabaseResponse = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM RESTAURANT;");

            while (resultSet.next()) {
                restaurantDatabaseResponse.add(ObjectMapper.mapResultSetToRestaurantDatabaseResponse(resultSet));
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        } finally {
            DatabaseUtil.activeConnectionWithDatabase = false;
            notifyAll();
        }

        return ObjectMapper.mapRestaurantDatabaseResponsesToRestaurants(restaurantDatabaseResponse);
    }

    @Override
    public synchronized void save(Set<Restaurant> entities) throws RepositoryAccessException {
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

                RestaurantMealsRepository.saveMealsForRestaurant(restaurant);
                RestaurantChefsRepository.saveChefsForRestaurant(restaurant);
                RestaurantWaitersRepository.saveWaitersForRestaurant(restaurant);
                RestaurantDeliverersRepository.saveDeliverersForRestaurant(restaurant);
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
