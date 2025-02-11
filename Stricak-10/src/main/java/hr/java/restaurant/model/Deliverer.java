package hr.java.restaurant.model;

import hr.java.restaurant.util.DatabaseUtil;
import hr.java.restaurant.util.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Represents a deliverer in a restaurant.
 */
public class Deliverer extends Person implements Serializable {

    private final Contract contract;
    private Bonus bonus;

    /**
     * Constructs a Deliverer object using the provided builder.
     *
     * @param builder the builder instance containing deliverer information
     */
    private Deliverer(Builder builder) {
        super(builder.id, builder.firstName, builder.lastName, builder.bonus);
        this.contract = builder.contract;
        this.bonus = builder.bonus;
    }

    @Override
    public Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    @Override
    public BigDecimal getSalary() {
        return contract.getSalary();
    }

    @Override
    public Contract getContract() {
        return contract;
    }

    /**
     * Builder class for creating instances of {@link Deliverer}.
     */
    public static class Builder {
        private final Long id;
        private String firstName;
        private String lastName;
        private Contract contract;
        private Bonus bonus;

        public Builder(Long id) {
            this.id = id;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setContract(Contract contract) {
            this.contract = contract;
            return this;
        }

        public Builder setBonus(Bonus bonus) {
            this.bonus = bonus;
            return this;
        }

        public Deliverer build() {
            return new Deliverer(this);
        }
    }

    public Set<Order> getDeliveredOrders() {
        Set<Order> deliveredOrders = new HashSet<>();
        String query = """
                SELECT * FROM RESTAURANT_ORDER WHERE DELIVERED = TRUE AND DELIVERER_ID = ?;
                """;

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, getId());

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Order order = ObjectMapper.mapResultSetToOrder(resultSet);
                deliveredOrders.add(order);
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        return deliveredOrders;
    }

    public Set<Order> getWaitingOrders() {
        Set<Order> waitingOrders = new HashSet<>();
        String query = """
                SELECT * FROM RESTAURANT_ORDER WHERE WAITING = TRUE AND DELIVERER_ID = ?;
                """;

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, getId());

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Order order = ObjectMapper.mapResultSetToOrder(resultSet);
                waitingOrders.add(order);
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        return waitingOrders;
    }

    public Set<Order> getDeliverInProcessOrders() {
        Set<Order> deliverInProcessOrders = new HashSet<>();
        String query = """
                SELECT * FROM RESTAURANT_ORDER WHERE DELIVER_IN_PROCESS = TRUE AND DELIVERER_ID = ?;
                """;

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, getId());

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Order order = ObjectMapper.mapResultSetToOrder(resultSet);
                deliverInProcessOrders.add(order);
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        return deliverInProcessOrders;
    }

    public void deliverInProcessOrder(Order order) {
        String query = """
                UPDATE RESTAURANT_ORDER
                SET WAITING = FALSE, DELIVER_IN_PROCESS = TRUE, DELIVERED = FALSE
                WHERE ID = ?;
                """;

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, order.getId());

            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deliverOrder(Order order) {
        String query = """
                UPDATE RESTAURANT_ORDER
                SET WAITING = FALSE, DELIVER_IN_PROCESS = FALSE, DELIVERED = TRUE
                WHERE ID = ?;
                """;

        try (Connection connection = DatabaseUtil.connectToDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setLong(1, order.getId());

            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
