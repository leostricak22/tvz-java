package hr.java.restaurant.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Represents a chef in a restaurant.
 */
public class Chef extends Person implements Serializable {

    private Contract contract;

    /**
     * Constructs a Chef object using the provided builder.
     *
     * @param builder the builder instance containing chef information
     */
    private Chef(Builder builder) {
        super(builder.id, builder.firstName, builder.lastName, builder.bonus);
        this.contract = builder.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
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
     * Builder class for creating instances of {@link Chef}.
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

        public Chef build() {
            return new Chef(this);
        }
    }
}
