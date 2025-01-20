package hr.java.restaurant.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Represents a deliverer in a restaurant.
 */
public class Deliverer extends Person implements Serializable {

    private final Contract contract;
    private Bonus bonus;

    /**
     * Constructs a Deliverer object using the provided builder.
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
}
