package hr.java.restaurant.repository;

import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Ingredient;
import hr.java.restaurant.model.Meal;
import hr.java.restaurant.model.dbo.IngredientDatabaseResponse;
import hr.java.restaurant.util.DatabaseUtil;
import hr.java.restaurant.util.ObjectMapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class MealIngredientsRepository {

    public static Set<Ingredient> getIngredientsForMeal(Long mealId) {
        Set<IngredientDatabaseResponse> ingredientDatabaseResponses = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("""
                SELECT i.ID, i.NAME, i.CATEGORY_ID, i.KCAL, i.PREPARATION_METHOD
                FROM MEAL_INGREDIENT mi
                JOIN INGREDIENT i ON mi.INGREDIENT_ID = i.ID
                WHERE mi.MEAL_ID = ?;
                """);

            stmt.setLong(1, mealId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                ingredientDatabaseResponses.add(ObjectMapper.mapResultSetToIngredientDatabaseResponse(resultSet));
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }

        return ObjectMapper.mapIngredientDatabaseResponsesToIngredients(ingredientDatabaseResponses);
    }

    public static void saveIngredientsForMeal(Meal meal) {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("""
                INSERT INTO MEAL_INGREDIENT (MEAL_ID, INGREDIENT_ID) VALUES (?, ?);
                """);

            for (Ingredient ingredient : meal.getIngredients()) {
                stmt.setLong(1, meal.getId());
                stmt.setLong(2, ingredient.getId());
                stmt.executeUpdate();
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }
}
