package hr.java.restaurant.repository;

import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Category;
import hr.java.restaurant.model.Meal;
import hr.java.restaurant.util.DatabaseUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class MealDatabaseRepository extends AbstractRepository<Meal> {

    @Override
    public Meal findById(Long id) throws RepositoryAccessException {
        return null;
    }

    @Override
    public Set<Meal> findAll() throws RepositoryAccessException {
        Set<Meal> meals = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM MEAL;");

            while (resultSet.next()) {
                //Meal meal = mapResultSetToMeal(resultSet);
                //meals.add(meal);
            }

            return meals;
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public void save(Set<Meal> entities) throws RepositoryAccessException {

    }

    @Override
    public Long findNextId() throws RepositoryAccessException {
        return 0L;
    }

    private void mapResultSetToMeal(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("ID");
        String name = resultSet.getString("NAME");
    }
}