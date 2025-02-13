package hr.javafx.coffe.caffee.javafxcaffee.repository;

import hr.javafx.coffe.caffee.javafxcaffee.exception.EmptyRepositoryResultException;
import hr.javafx.coffe.caffee.javafxcaffee.exception.RepositoryAccessException;
import hr.javafx.coffe.caffee.javafxcaffee.model.Beverage;
import hr.javafx.coffe.caffee.javafxcaffee.model.Origin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PipedInputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class BeveragesDatabaseRepository<T extends Beverage> extends AbstractRepository<T> {

    private static Boolean DATABASE_ACCESS_IN_PROGRESS = false;

    private Connection connectToDatabase() throws IOException, SQLException {
        // dohvaćanje podataka za spajanje na bazu iz properties datoteke
        Properties props = new Properties();
        props.load(new FileReader("database.properties"));

        // DriverManager je klasa koja upravlja JDBC driverima
        return DriverManager.getConnection(props.getProperty("databaseUrl"),
                props.getProperty("username"),
                props.getProperty("password"));
    }

    private void disconnectFromDatabase(Connection connection) throws SQLException {
        connection.close();
    }

    // synchronized ne može biti u abstract klasi
    @Override
    public synchronized T findById(Long id) {
        // petlja se izvršava dok je pristup bazi podataka u tijeku
        while (DATABASE_ACCESS_IN_PROGRESS) {
            try {
                // čekanje dok se pristup ne završi
                wait();
                // wait() baca InterruptedException, pa je potrebno catchati
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // postavljanje pristupa bazi podataka
        DATABASE_ACCESS_IN_PROGRESS = true;

        // connection se zatvara automatski jer je implementiran AutoCloseable
        try (Connection connection = connectToDatabase()) {
            // PreparedStatement se koristi za izvršavanje SQL upita koji se ponavljaju
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM BEVERAGE WHERE ID = ?");
            // postavljanje parametara upita
            // počinje od 1, a ne od 0
            stmt.setLong(1, id);

            // executeQuery vraća ResultSet objekt koji sadrži sve retke koji zadovoljavaju upit
            ResultSet resultSet = stmt.executeQuery();

            // next() vraća true ako ima redaka u resultSet-u
            // ovdje se očekuje samo jedan redak
            if (resultSet.next()) {
                return (T) extractBeverageFromResultSet(resultSet);
            } else {
                throw new EmptyRepositoryResultException("No beverage with id " + id);
            }
        } catch (IOException | SQLException ex) {
            throw new RepositoryAccessException(ex);
        } finally {
            // u finally se ulazi i nakon return-a

            // postavljanje pristupa bazi podataka
            DATABASE_ACCESS_IN_PROGRESS = false;
            // obavještavanje svih niti koje čekaju
            notifyAll();
        }
    }

    @Override
    public List<T> findAll() throws RepositoryAccessException {
        List<T> beverages = new ArrayList<>();

        // connection se zatvara automatski jer je implementiran AutoCloseable
        try (Connection connection = connectToDatabase()) {
            // Statement je interface, a connection.createStatement() vraća objekt koji ga implementira
            // Statement se koristi za izvršavanje SQL upita koji se ne moraju ponavljati
            Statement stmt = connection.createStatement();

            // ResultSet je interface, a stmt.executeQuery() vraća objekt koji ga implementira
            // executeQuery vraća ResultSet objekt koji sadrži sve retke koji zadovoljavaju upit
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM BEVERAGE");

            // stmt.getResultSet() vraća ResultSet objekt koji sadrži sve retke koji zadovoljavaju upit
            //ResultSet resultSet = stmt.getResultSet();
            // petlja se izvršava dok ima redaka u resultSet-u
            while (resultSet.next()) {
                Beverage beverage = extractBeverageFromResultSet(resultSet);
                // potrebno castanje jer je Beverage nadklasa
                beverages.add((T) beverage);
            }

            // kada se connection zatvori, automatski se zatvaraju i stmt i resultSet

            return beverages;
        } catch (IOException | SQLException ex) {
            throw new RepositoryAccessException(ex);
        }
    }

    @Override
    public void save(List<T> entities) {
        // connection se zatvara automatski jer je implementiran AutoCloseable
        try (Connection connection = connectToDatabase()) {
            // PreparedStatement se koristi za izvršavanje SQL upita koji se ponavljaju
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO BEVERAGE (NAME, PRICE, ALCOHOL_PERCENTAGE, ORIGIN) VALUES (?, ?, ?, ?)");

            // petlja se izvršava za svaki entitet
            for (T entity : entities) {
                // postavljanje parametara upita
                stmt.setString(1, entity.getName());
                stmt.setBigDecimal(2, entity.getPrice());
                stmt.setBigDecimal(3, entity.calculatePercentage());
                stmt.setString(4, entity.getOrigin().toString());

                // executeUpdate vraća broj redaka koji su promijenjeni
                stmt.executeUpdate();
            }
        } catch (IOException | SQLException ex) {
            throw new RepositoryAccessException(ex);
        }
    }

    @Override
    public synchronized void save(T entity) {
        // petlja se izvršava dok je pristup bazi podataka u tijeku
        while (DATABASE_ACCESS_IN_PROGRESS) {
            try {
                // čekanje dok se pristup ne završi
                wait();
                // wait() baca InterruptedException, pa je potrebno catchati
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // postavljanje pristupa bazi podataka
        DATABASE_ACCESS_IN_PROGRESS = true;

        try (Connection connection = connectToDatabase()) {
            // PreparedStatement se koristi za izvršavanje SQL upita koji se ponavljaju
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO BEVERAGE (NAME, PRICE, ALCOHOL_PERCENTAGE, ORIGIN) VALUES (?, ?, ?, ?)");

            // postavljanje parametara upita
            stmt.setString(1, entity.getName());
            stmt.setBigDecimal(2, entity.getPrice());
            stmt.setBigDecimal(3, entity.calculatePercentage());
            stmt.setString(4, entity.getOrigin().toString());

            // executeUpdate vraća broj redaka koji su promijenjeni
            stmt.executeUpdate();
        } catch (IOException | SQLException ex) {
            throw new RepositoryAccessException(ex);
        } finally {
            // u finally se ulazi i nakon return-a

            // postavljanje pristupa bazi podataka
            DATABASE_ACCESS_IN_PROGRESS = false;
            // obavještavanje svih niti koje čekaju
            notifyAll();
        }
    }

    private T extractBeverageFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        BigDecimal price = resultSet.getBigDecimal("price");
        BigDecimal alcoholPercentage = resultSet.getBigDecimal("alcohol_percentage");
        String origin = resultSet.getString("origin");

        return (T) new Beverage(id, name, price, alcoholPercentage, Origin.valueOf(origin));
    }

    public synchronized Optional<T> findCheapestBeverage() {
        // petlja se izvršava dok je pristup bazi podataka u tijeku
        while (DATABASE_ACCESS_IN_PROGRESS) {
            try {
                // čekanje dok se pristup ne završi
                wait();
                // wait() baca InterruptedException, pa je potrebno catchati
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // postavljanje pristupa bazi podataka
        DATABASE_ACCESS_IN_PROGRESS = true;

        try (Connection connection = connectToDatabase()) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM BEVERAGE WHERE PRICE = (SELECT MIN(PRICE) FROM BEVERAGE)");

            if (resultSet.next()) {
                return Optional.of(extractBeverageFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (IOException | SQLException ex) {
            throw new RepositoryAccessException("An error occured while fetching the cheapest beverage from database!", ex);
        } finally {
            // u finally se ulazi i nakon return-a

            // postavljanje pristupa bazi podataka
            DATABASE_ACCESS_IN_PROGRESS = false;
            // obavještavanje svih niti koje čekaju
            notifyAll();
        }
    }
}
