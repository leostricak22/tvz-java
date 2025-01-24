package hr.java.restaurant.repository;

import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Person;
import hr.java.restaurant.model.Waiter;
import hr.java.restaurant.model.dbo.PersonDatabaseResponse;
import hr.java.restaurant.util.DatabaseUtil;
import hr.java.restaurant.util.ObjectMapper;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class WaiterRepository extends AbstractRepository<Waiter> {

    @Override
    public synchronized Waiter findById(Long id) throws RepositoryAccessException {
        while (DatabaseUtil.activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        DatabaseUtil.activeConnectionWithDatabase = true;
        PersonDatabaseResponse waiterDatabaseResponse;

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM WAITER WHERE ID = ?;");
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                waiterDatabaseResponse = ObjectMapper.mapResultSetToPersonDatabaseResponse(resultSet);
            } else {
                throw new RepositoryAccessException("Waiter with id " + id + " not found");
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        } finally {
            DatabaseUtil.activeConnectionWithDatabase = false;
            notifyAll();
        }

        return ObjectMapper.mapWaiterDatabaseResponseToWaiter(waiterDatabaseResponse);
    }

    @Override
    public synchronized Set<Waiter> findAll() throws RepositoryAccessException {
        while (DatabaseUtil.activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        DatabaseUtil.activeConnectionWithDatabase = true;
        Set<PersonDatabaseResponse> waiterDatabaseResponses = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM WAITER;");

            while (resultSet.next()) {
                waiterDatabaseResponses.add(ObjectMapper.mapResultSetToPersonDatabaseResponse(resultSet));
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        } finally {
            DatabaseUtil.activeConnectionWithDatabase = false;
            notifyAll();
        }

        return ObjectMapper.mapWaiterDatabaseResponsesToWaiters(waiterDatabaseResponses);
    }

    @Override
    public synchronized void save(Set<Waiter> entities) throws RepositoryAccessException {
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
                    "INSERT INTO WAITER (FIRST_NAME, LAST_NAME, CONTRACT_ID, BONUS) VALUES (?, ?, ?, ?);");

            for (Waiter waiter : entities) {
                stmt.setString(1, waiter.getFirstName());
                stmt.setString(2, waiter.getLastName());
                stmt.setLong(3, waiter.getContract().getId());
                stmt.setBigDecimal(4, waiter.getBonus().amount());
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

    public Optional<Waiter> findHighestPaidWaiter() {
        Set<Waiter> waiters = findAll();

        return waiters.stream()
                .max((waiter1, waiter2) -> (waiter1.getContract().getSalary().add(waiter1.getBonus().amount()))
                        .compareTo(waiter2.getContract().getSalary().add(waiter2.getBonus().amount())));
    }
}
