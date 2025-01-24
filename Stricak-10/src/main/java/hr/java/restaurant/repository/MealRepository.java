package hr.java.restaurant.repository;

import hr.java.restaurant.exception.EmptyRepositoryResultException;
import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Meal;
import hr.java.restaurant.model.dbo.MealDatabaseResponse;
import hr.java.restaurant.util.DatabaseUtil;
import hr.java.restaurant.util.ObjectMapper;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class MealRepository extends AbstractRepository<Meal> {

    @Override
    public synchronized Meal findById(Long id) throws RepositoryAccessException {
        while (DatabaseUtil.activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        DatabaseUtil.activeConnectionWithDatabase = true;
        MealDatabaseResponse mealDatabaseResponse;

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM MEAL WHERE ID = ?;");
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                mealDatabaseResponse = ObjectMapper.mapResultSetToMealDatabaseResponse(resultSet);
            } else {
                throw new EmptyRepositoryResultException("Meal with id " + id + " not found");
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        } finally {
            DatabaseUtil.activeConnectionWithDatabase = false;
            notifyAll();
        }

        return ObjectMapper.mapMealDatabaseResponseToMeal(mealDatabaseResponse);
    }

    @Override
    public synchronized Set<Meal> findAll() throws RepositoryAccessException {
        while (DatabaseUtil.activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        DatabaseUtil.activeConnectionWithDatabase = true;
        Set<MealDatabaseResponse> mealDatabaseResponses = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM MEAL;");

            while (resultSet.next()) {
                mealDatabaseResponses.add(ObjectMapper.mapResultSetToMealDatabaseResponse(resultSet));
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        } finally {
            DatabaseUtil.activeConnectionWithDatabase = false;
            notifyAll();
        }

        return ObjectMapper.mapMealDatabaseResponsesToMeals(mealDatabaseResponses);
    }

    @Override
    public synchronized void save(Set<Meal> entities) throws RepositoryAccessException {
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