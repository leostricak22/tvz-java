package hr.java.restaurant.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Represents a chef in a restaurant.
 */
public class Chef extends Person  implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Chef.class);

    private static Long counter = 0L;
    private Contract contract;
    private Bonus bonus;

    /**
     * Constructs a Chef object using the provided builder.
     *
     * @param builder the builder instance containing chef information
     */
    private Chef(Builder builder) {
        super(builder.id, builder.firstName, builder.lastName);
        this.contract = builder.contract;
        this.bonus = builder.bonus;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    /**
     * Checks if the chef with the provided name already exists in the array of chefs.
     * @param chefs the chefs
     * @param firstName the first name of the chef
     * @param lastName the last name of the chef
     * @return the index of the chef if it exists, -1 otherwise
     */
    public static Integer existsByName(Set<Chef> chefs, String firstName, String lastName) {
        int i=0;
        for (Chef chef : chefs) {
            if (chef.getFirstName().equals(firstName) && chef.getLastName().equals(lastName)) {
                return i;
            }
            i++;
        }

        return -1;
    }

    public Bonus getBonus() {
        return bonus;
    }

    /**
     * Returns an array of chef names.
     * @param chefs the chefs
     * @return the array of chef names
     */
    public static List<String> chefNameArray(Set<Chef> chefs) {
        List<String> chefNames = new ArrayList<>();

        for(Chef chef : chefs) {
            chefNames.add(chef.getFirstName() + " " + chef.getLastName());
        }

        return chefNames;
    }

    /**
     * Builder class for creating instances of {@link Chef}.
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

        public Chef build() {
            return new Chef(this);
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
        Chef chef = (Chef) o;
        return Objects.equals(contract, chef.contract) && Objects.equals(bonus, chef.bonus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contract, bonus);
    }

    /**
     * Prints the chef details with the specified number of tabulators.
     * @param tabulators the number of tabulators to format the output
     */
    @Override
    public void print(Integer tabulators) {
        logger.info("Printing chef.");
        Person.outputFullData(tabulators, getId(), getFirstName(), getLastName() , contract, bonus);
    }
}
