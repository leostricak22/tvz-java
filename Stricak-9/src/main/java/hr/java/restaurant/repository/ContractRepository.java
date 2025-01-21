package hr.java.restaurant.repository;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Contract;
import hr.java.restaurant.util.DatabaseUtil;
import hr.java.restaurant.util.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ContractRepository extends AbstractRepository<Contract> {

    @Override
    public Contract findById(Long id) throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM CONTRACT WHERE ID = ?;");
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return ObjectMapper.mapResultSetToContract(resultSet);
            } else {
                throw new RepositoryAccessException("Contract with id " + id + " not found");
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public Set<Contract> findAll() throws RepositoryAccessException {
        Set<Contract> contracts = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM CONTRACT;");

            while (resultSet.next()) {
                Contract contract = ObjectMapper.mapResultSetToContract(resultSet);
                contracts.add(contract);
            }

            return contracts;
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public void save(Set<Contract> entities) throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO CONTRACT (SALARY, BONUS, START_DATE, END_DATE, TYPE) VALUES (?, ?, ?, ?, ?);");

            for (Contract contract : entities) {
                stmt.setBigDecimal(1, contract.getSalary());
                stmt.setBigDecimal(2, BigDecimal.ZERO); // TODO: remove bonus
                stmt.setDate(3, Date.valueOf(contract.getStartDate()));
                stmt.setDate(4, Date.valueOf(contract.getEndDate()));
                stmt.setString(5, contract.getContractType().name());
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
            ResultSet resultSet = stmt.executeQuery("SELECT MAX(ID) FROM CONTRACT;");

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
