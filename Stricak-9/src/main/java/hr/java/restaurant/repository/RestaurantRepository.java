package hr.java.restaurant.repository;

import hr.java.restaurant.exception.EmptyRepositoryResultException;
import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.*;
import hr.java.restaurant.util.DatabaseUtil;
import hr.java.restaurant.util.ObjectMapper;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class RestaurantRepository extends AbstractRepository<Restaurant> {

    @Override
    public Restaurant findById(Long id) throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RESTAURANT WHERE ID = ?;");
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return ObjectMapper.mapResultSetToRestaurant(resultSet);
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
                return ObjectMapper.mapResultSetToRestaurant(resultSet);
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
                Restaurant restaurant = ObjectMapper.mapResultSetToRestaurant(resultSet);
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

                RestaurantMealsRepository.saveMealsForRestaurant(restaurant);
                RestaurantChefsRepository.saveChefsForRestaurant(restaurant);
                RestaurantWaitersRepository.saveWaitersForRestaurant(restaurant);
                RestaurantDeliverersRepository.saveDeliverersForRestaurant(restaurant);
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
}
