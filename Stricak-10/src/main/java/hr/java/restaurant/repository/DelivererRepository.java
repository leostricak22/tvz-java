package hr.java.restaurant.repository;

import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Deliverer;
import hr.java.restaurant.model.Person;
import hr.java.restaurant.model.dbo.PersonDatabaseResponse;
import hr.java.restaurant.util.DatabaseUtil;
import hr.java.restaurant.util.ObjectMapper;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DelivererRepository extends AbstractRepository<Deliverer> {

    @Override
    public synchronized Deliverer findById(Long id) throws RepositoryAccessException {
        while (DatabaseUtil.activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        DatabaseUtil.activeConnectionWithDatabase = true;
        PersonDatabaseResponse delivererDatabaseResponse;

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM DELIVERER WHERE ID = ?;");
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                delivererDatabaseResponse = ObjectMapper.mapResultSetToPersonDatabaseResponse(resultSet);
            } else {
                throw new RepositoryAccessException("Deliverer with id " + id + " not found");
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        } finally {
            DatabaseUtil.activeConnectionWithDatabase = false;
            notifyAll();
        }

        return ObjectMapper.mapDelivererDatabaseResponseToDeliverer(delivererDatabaseResponse);
    }

    @Override
    public synchronized Set<Deliverer> findAll() throws RepositoryAccessException {
        while (DatabaseUtil.activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        DatabaseUtil.activeConnectionWithDatabase = true;
        Set<PersonDatabaseResponse> delivererDatabaseResponses = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM DELIVERER;");

            while (resultSet.next()) {
                delivererDatabaseResponses.add(ObjectMapper.mapResultSetToPersonDatabaseResponse(resultSet));
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        } finally {
            DatabaseUtil.activeConnectionWithDatabase = false;
            notifyAll();
        }

        return ObjectMapper.mapDelivererDatabaseResponsesToDeliverers(delivererDatabaseResponses);
    }

    @Override
    public synchronized void save(Set<Deliverer> entities) throws RepositoryAccessException {
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
        } finally {
            DatabaseUtil.activeConnectionWithDatabase = false;
            notifyAll();
        }
    }

    @Override
    public synchronized Long findNextId() throws RepositoryAccessException {
        return 0L;
    }

    public Optional<Deliverer> findHighestPaidDeliverer() {
        Set<Deliverer> deliverers = findAll();

        return deliverers.stream()
                .max((deliverer1, deliverer) -> (deliverer1.getContract().getSalary().add(deliverer1.getBonus().amount()))
                        .compareTo(deliverer.getContract().getSalary().add(deliverer.getBonus().amount())));
    }
}
