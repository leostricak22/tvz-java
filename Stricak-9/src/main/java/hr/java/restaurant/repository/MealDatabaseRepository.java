package hr.java.restaurant.repository;

import hr.java.restaurant.exception.EmptyRepositoryResultException;
import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Category;
import hr.java.restaurant.model.Ingredient;
import hr.java.restaurant.model.Meal;
import hr.java.restaurant.util.DatabaseUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class MealDatabaseRepository extends AbstractRepository<Meal> {

    private final CategoryDatabaseRepository categoryRepository = new CategoryDatabaseRepository();
    private final IngredientDatabaseRepository ingredientRepository = new IngredientDatabaseRepository();

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
                Meal meal = mapResultSetToMeal(resultSet);
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
                """);

            for (Meal meal : entities) {
                stmt.setString(1, meal.getName());
                stmt.setLong(2, meal.getCategory().getId());
                stmt.setBigDecimal(3, meal.getPrice());
                stmt.executeUpdate();

                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    meal.setId(generatedKeys.getLong(1));
                }

                saveIngredientsForMeal(meal);
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

    private Set<Ingredient> getIngredientsForMeal(Long mealId) {
        Set<Ingredient> ingredients = new HashSet<>();

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
                Ingredient ingredient = ingredientRepository.mapResultSetToIngredient(resultSet);
                ingredients.add(ingredient);
            }

            return ingredients;
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    private void saveIngredientsForMeal(Meal meal) {
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

    private Meal mapResultSetToMeal(ResultSet resultSet) throws SQLException, IOException {
        Long id = resultSet.getLong("ID");
        String name = resultSet.getString("NAME");
        Category category = categoryRepository.findById(resultSet.getLong("CATEGORY_ID"));
        BigDecimal price = resultSet.getBigDecimal("PRICE");
        Set<Ingredient> ingredients = getIngredientsForMeal(id);

        return new Meal.Builder(id, name)
                .setCategory(category)
                .setPrice(price)
                .setIngredients(ingredients)
                .build();
    }
}