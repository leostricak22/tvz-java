package hr.java.restaurant.repository;

import hr.java.restaurant.exception.EmptyRepositoryResultException;
import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Address;
import hr.java.restaurant.util.DatabaseUtil;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class AddressRepository extends AbstractRepository<Address> {
    @Override
    public Address findById(Long id) throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM ADDRESS WHERE ID = ?;");
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToAddress(resultSet);
            } else {
                throw new EmptyRepositoryResultException("Address with id " + id + " not found");
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public Set<Address> findAll() throws RepositoryAccessException {
        Set<Address> addresses = new HashSet<>();

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM ADDRESS;");

            while (resultSet.next()) {
                Address address = mapResultSetToAddress(resultSet);
                addresses.add(address);
            }

            return addresses;
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    @Override
    public void save(Set<Address> entities) throws RepositoryAccessException {
        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO ADDRESS (STREET, HOUSE_NUMBER, CITY, POSTAL_CODE) VALUES (?, ?, ?, ?);");

            for (Address address : entities) {
                stmt.setString(1, address.getStreet());
                stmt.setString(2, address.getHouseNumber());
                stmt.setString(3, address.getCity());
                stmt.setString(4, address.getPostalCode());
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
            ResultSet resultSet = stmt.executeQuery("SELECT MAX(ID) FROM ADDRESS;");

            if (resultSet.next()) {
                return resultSet.getLong(1) + 1;
            } else {
                return 1L;
            }
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    private Address mapResultSetToAddress(ResultSet resultSet) throws SQLException {
        return new Address.Builder(resultSet.getLong("ID"))
                .setStreet(resultSet.getString("STREET"))
                .setHouseNumber(resultSet.getString("HOUSE_NUMBER"))
                .setCity(resultSet.getString("CITY"))
                .setPostalCode(resultSet.getString("POSTAL_CODE"))
                .build();
    }
}
