package hr.java.restaurant.repository;

import hr.java.restaurant.exception.EmptyRepositoryResultException;
import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Meal;
import hr.java.restaurant.util.DatabaseUtil;
import hr.java.restaurant.util.ObjectMapper;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class MealRepository extends AbstractRepository<Meal> {

    @Override
    public Meal findById(Long id) throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM MEAL WHERE ID = ?;");
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return ObjectMapper.mapResultSetToMeal(resultSet);
            } else {
                throw new EmptyRepositoryResultException("Meal with id " + id + " not found");
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public Set<Meal> findAll() throws RepositoryAccessException {
        Set<Meal> meals = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM MEAL;");

            while (resultSet.next()) {
                Meal meal = ObjectMapper.mapResultSetToMeal(resultSet);
                meals.add(meal);
            }

            return meals;
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public void save(Set<Meal> entities) throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("""
                INSERT INTO MEAL (NAME, CATEGORY_ID, PRICE) VALUES (?, ?, ?);
                """, Statement.RETURN_GENERATED_KEYS);

            for (Meal meal : entities) {
                stmt.setString(1, meal.getName());
                stmt.setLong(2, meal.getCategory().getId());
                stmt.setBigDecimal(3, meal.getPrice());
                stmt.executeUpdate();

                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    meal.setId(generatedKeys.getLong(1));
                }

                MealIngredientsRepository.saveIngredientsForMeal(meal);
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public Long findNextId() throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT MAX(ID) FROM MEAL;");

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