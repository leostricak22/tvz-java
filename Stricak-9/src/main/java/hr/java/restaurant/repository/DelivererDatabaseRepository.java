package hr.java.restaurant.repository;

import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Bonus;
import hr.java.restaurant.model.Deliverer;
import hr.java.restaurant.model.Deliverer;
import hr.java.restaurant.util.DatabaseUtil;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DelivererDatabaseRepository extends AbstractRepository<Deliverer> {

    private final ContractDatabaseRepository contractRepository = new ContractDatabaseRepository();

    @Override
    public Deliverer findById(Long id) throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM DELIVERER WHERE ID = ?;");
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToDeliverer(resultSet);
            } else {
                throw new RepositoryAccessException("Deliverer with id " + id + " not found");
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public Set<Deliverer> findAll() throws RepositoryAccessException {
        Set<Deliverer> deliverers = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM DELIVERER;");

            while (resultSet.next()) {
                Deliverer deliverer = mapResultSetToDeliverer(resultSet);
                deliverers.add(deliverer);
            }

            return deliverers;
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public void save(Set<Deliverer> entities) throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO DELIVERER (FIRST_NAME, LAST_NAME, CONTRACT_ID, BONUS) VALUES (?, ?, ?, ?);");

            for (Deliverer deliverer : entities) {
                stmt.setString(1, deliverer.getFirstName());
                stmt.setString(2, deliverer.getLastName());
                stmt.setLong(3, deliverer.getContract().getId());
                stmt.setBigDecimal(4, deliverer.getBonus().amount());
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
            ResultSet resultSet = stmt.executeQuery("SELECT MAX(ID) FROM DELIVERER;");

            if (resultSet.next()) {
                return resultSet.getLong(1) + 1;
            } else {
                return 1L;
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    private Deliverer mapResultSetToDeliverer(ResultSet resultSet) throws SQLException {
        return new Deliverer.Builder(resultSet.getLong("ID"))
                .setFirstName(resultSet.getString("FIRST_NAME"))
                .setLastName(resultSet.getString("LAST_NAME"))
                .setContract(contractRepository.findById(resultSet.getLong("CONTRACT_ID")))
                .setBonus(new Bonus(resultSet.getBigDecimal("BONUS")))
                .build();
    }
}
