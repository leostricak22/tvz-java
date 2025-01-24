package hr.java.restaurant.repository;

import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Chef;
import hr.java.restaurant.util.DatabaseUtil;
import hr.java.restaurant.util.ObjectMapper;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ChefRepository extends AbstractRepository<Chef> {

    @Override
    public Chef findById(Long id) throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM CHEF WHERE ID = ?;");
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return ObjectMapper.mapResultSetToChef(resultSet);
            } else {
                throw new RepositoryAccessException("Chef with id " + id + " not found");
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public Set<Chef> findAll() throws RepositoryAccessException {
        Set<Chef> chefs = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM CHEF;");

            while (resultSet.next()) {
                Chef chef = ObjectMapper.mapResultSetToChef(resultSet);
                chefs.add(chef);
            }

            return chefs;
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public void save(Set<Chef> entities) throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO CHEF (FIRST_NAME, LAST_NAME, CONTRACT_ID, BONUS) VALUES (?, ?, ?, ?);");

            for (Chef chef : entities) {
                stmt.setString(1, chef.getFirstName());
                stmt.setString(2, chef.getLastName());
                stmt.setLong(3, chef.getContract().getId());
                stmt.setBigDecimal(4, chef.getBonus().amount());
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
            ResultSet resultSet = stmt.executeQuery("SELECT MAX(ID) FROM CHEF;");

            if (resultSet.next()) {
                return resultSet.getLong(1) + 1;
            } else {
                return 1L;
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    public Optional<Chef> findHighestPaidChef() {
        Set<Chef> chefs = findAll();

        return chefs.stream()
                .max((chef1, chef2) -> (chef1.getContract().getSalary().add(chef1.getBonus().amount()))
                        .compareTo(chef2.getContract().getSalary().add(chef2.getBonus().amount())));
    }
}
