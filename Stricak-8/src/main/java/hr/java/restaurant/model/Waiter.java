package hr.java.restaurant.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Represents a waiter.
 */
public class Waiter extends Person implements Serializable {
    private static Long counter = 0L;
    private final Contract contract;
    private Bonus bonus;
    private static final Logger logger = LoggerFactory.getLogger(Waiter.class);

    /**
     * Constructs a Waiter object using the provided builder.
     * @param builder the builder instance containing waiter information
     */
    public Waiter(Builder builder) {
        super(builder.id, builder.firstName, builder.lastName, builder.bonus);
        this.contract = builder.contract;
    }


    /**
     * Checks if the waiter with the provided name already exists in the array of waiters.
     * @param waiters the waiters
     * @param firstName the first name of the waiter
     * @param lastName the last name of the waiter
     * @return the index of the waiter if it exists, -1 otherwise
     */
    public static Integer existsByName(Set<Waiter> waiters, String firstName, String lastName) {
        int i=0;
        for (Waiter waiter : waiters) {
            if(firstName.equals(waiter.getFirstName()) && lastName.equals(waiter.getLastName())) {
                return i;
            }
            i++;
        }

        return -1;
    }

    /**
     * Returns the waiter's first name.
     * @return the waiter's first name
     */
    public static List<String> waiterNameArray(Set<Waiter> waiters) {
        List<String> waiterNames = new ArrayList<>();

        for (Waiter waiter : waiters) {
            waiterNames.add(waiter.getFirstName() + " " + waiter.getLastName());
        }

        return waiterNames;
    }

    /**
     * Builder class for creating instances of {@link Waiter}.
     */
    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private Contract contract;
        private Bonus bonus;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder contract(Contract contract) {
            this.contract = contract;
            return this;
        }

        public Builder bonus(Bonus bonus) {
            this.bonus = bonus;
            return this;
        }

        public Waiter build() {
            return new Waiter(this);
        }
    }

    @Override
    public BigDecimal getSalary() {
        return contract.getSalary();
    }

    @Override
    public Contract getContract() {
        return contract;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Waiter waiter = (Waiter) o;
        return Objects.equals(contract, waiter.contract) && Objects.equals(bonus, waiter.bonus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contract, bonus);
    }

    /**
     * Prints the waiter's information.
     * @param tabulators the number of tabulators to format the output
     */
    @Override
    public void print(Integer tabulators) {
        logger.info("Waiter print");
        Person.outputFullData(tabulators, getId(), getFirstName(), getLastName() , contract, bonus);
    }
}