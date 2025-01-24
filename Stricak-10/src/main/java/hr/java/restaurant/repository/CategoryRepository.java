package hr.java.restaurant.repository;

import hr.java.restaurant.exception.EmptyRepositoryResultException;
import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Category;
import hr.java.restaurant.util.DatabaseUtil;
import hr.java.restaurant.util.ObjectMapper;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class CategoryRepository extends AbstractRepository<Category> {

    @Override
    public Category findById(Long id) {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM CATEGORY WHERE ID = ?;");
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return ObjectMapper.mapResultSetToCategory(resultSet);
            } else {
                throw new EmptyRepositoryResultException("Category with id " + id + " not found");
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public Set<Category> findAll() throws RepositoryAccessException {
        Set<Category> categories = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM CATEGORY;");

            while (resultSet.next()) {
                Category category = ObjectMapper.mapResultSetToCategory(resultSet);
                categories.add(category);
            }

            return categories;
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
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
            throw new RepositoryAccessException(e);
        }
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
            throw new RepositoryAccessException(e);
        }
    }


}
