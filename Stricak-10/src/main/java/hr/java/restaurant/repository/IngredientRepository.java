package hr.java.restaurant.repository;

import hr.java.restaurant.exception.EmptyRepositoryResultException;
import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Category;
import hr.java.restaurant.model.Ingredient;
import hr.java.restaurant.model.dbo.IngredientDatabaseResponse;
import hr.java.restaurant.util.DatabaseUtil;
import hr.java.restaurant.util.ObjectMapper;

import javax.xml.transform.Result;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class IngredientRepository extends AbstractRepository<Ingredient> {

    @Override
    public synchronized Ingredient findById(Long id) throws RepositoryAccessException {
        while (DatabaseUtil.activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        DatabaseUtil.activeConnectionWithDatabase = true;
        IngredientDatabaseResponse ingredientDatabaseResponse;

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM INGREDIENT WHERE ID = ?;");
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                ingredientDatabaseResponse = ObjectMapper.mapResultSetToIngredientDatabaseResponse(resultSet);
            } else {
                throw new EmptyRepositoryResultException("Ingredient with id " + id + " not found");
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        } finally {
            DatabaseUtil.activeConnectionWithDatabase = false;
            notifyAll();
        }

        return ObjectMapper.mapIngredientDatabaseResponseToIngredient(ingredientDatabaseResponse);
    }

    @Override
    public synchronized Set<Ingredient> findAll() throws RepositoryAccessException {
        while (DatabaseUtil.activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        DatabaseUtil.activeConnectionWithDatabase = true;
        Set<IngredientDatabaseResponse> ingredientDatabaseResponses = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM INGREDIENT;");

            while (resultSet.next()) {
                IngredientDatabaseResponse ingredientDatabaseResponse =
                        ObjectMapper.mapResultSetToIngredientDatabaseResponse(resultSet);
                ingredientDatabaseResponses.add(ingredientDatabaseResponse);
            }

        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        } finally {
            DatabaseUtil.activeConnectionWithDatabase = false;
            notifyAll();
        }

        return ObjectMapper.mapIngredientDatabaseResponsesToIngredients(ingredientDatabaseResponses);
    }

    @Override
    public synchronized void save(Set<Ingredient> ingredients) throws RepositoryAccessException {
        while (DatabaseUtil.activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        DatabaseUtil.activeConnectionWithDatabase = true;

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO INGREDIENT (NAME, CATEGORY_ID, KCAL, PREPARATION_METHOD) VALUES (?, ?, ?, ?);");

            for (Ingredient ingredient : ingredients) {
                stmt.setString(1, ingredient.getName());
                stmt.setLong(2, ingredient.getCategory().getId());
                stmt.setBigDecimal(3, ingredient.getKcal());
                stmt.setString(4, ingredient.getPreparationMethod());
                stmt.executeUpdate();
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
