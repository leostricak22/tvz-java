package hr.java.restaurant.repository;

import hr.java.restaurant.exception.EmptyRepositoryResultException;
import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Category;
import hr.java.restaurant.util.DatabaseUtil;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryDatabaseRepository extends AbstractRepository<Category> {

    @Override
    public Category findById(Long id) {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM CATEGORY WHERE ID = ?;");
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToCategory(resultSet);
            } else {
                throw new EmptyRepositoryResultException("Category with id " + id + " not found");
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException("Error while connecting to database", e);
        }
    }

    @Override
    public Set<Category> findAll() throws RepositoryAccessException {
        List<Category> categories = new ArrayList<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM CATEGORY;");

            while (resultSet.next()) {
                Category category = mapResultSetToCategory(resultSet);
                categories.add(category);
            }

            return Set.copyOf(categories);
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException("Error while connecting to database", e);
        }
    }

    @Override
    public void save(Set<Category> entities) {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO CATEGORY (NAME, DESCRIPTION) VALUES (?, ?);");

            for (Category category : entities) {
                stmt.setString(1, category.getName());
                stmt.setString(2, category.getDescription());
                stmt.executeUpdate();

            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException("Error while connecting to database", e);
        }
    }

    @Override
    public void save(Category category) {
        save(Set.of(category));
    }

    @Override
    public Long findNextId() {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT MAX(ID) FROM CATEGORY;");

            if (resultSet.next()) {
                return resultSet.getLong(1) + 1;
            } else {
                return 1L;
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException("Error while connecting to database", e);
        }
    }

    private static Category mapResultSetToCategory(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("ID");
        String name = resultSet.getString("NAME");
        String description = resultSet.getString("DESCRIPTION");

        return new Category.Builder(id, name)
                .setDescription(description)
                .build();
    }
}
