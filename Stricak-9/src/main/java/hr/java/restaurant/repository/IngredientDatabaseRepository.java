package hr.java.restaurant.repository;

import hr.java.restaurant.exception.EmptyRepositoryResultException;
import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Category;
import hr.java.restaurant.model.Ingredient;
import hr.java.restaurant.util.DatabaseUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class IngredientDatabaseRepository extends AbstractRepository<Ingredient> {

    private final CategoryDatabaseRepository categoryRepository = new CategoryDatabaseRepository();

    @Override
    public Ingredient findById(Long id) throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM INGREDIENT WHERE ID = ?;");
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToIngredient(resultSet);
            } else {
                throw new EmptyRepositoryResultException("Ingredient with id " + id + " not found");
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public Set<Ingredient> findAll() throws RepositoryAccessException {
        Set<Ingredient> ingredients = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM INGREDIENT;");

            while (resultSet.next()) {
                Ingredient ingredient = mapResultSetToIngredient(resultSet);
                ingredients.add(ingredient);
            }

            return ingredients;
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public void save(Set<Ingredient> ingredients) throws RepositoryAccessException {
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
        }
    }

    @Override
    public Long findNextId() throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT MAX(ID) FROM INGREDIENT;");

            if (resultSet.next()) {
                return resultSet.getLong(1) + 1;
            } else {
                return 1L;
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    private Ingredient mapResultSetToIngredient(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("ID");
        String name = resultSet.getString("NAME");
        Category category = categoryRepository.findById(resultSet.getLong("CATEGORY_ID"));
        BigDecimal kcal = resultSet.getBigDecimal("KCAL");
        String preparationMethod = resultSet.getString("PREPARATION_METHOD");

        return new Ingredient.Builder(id, name)
                .setCategory(category)
                .setKcal(kcal)
                .setPreparationMethod(preparationMethod)
                .build();
    }
}
