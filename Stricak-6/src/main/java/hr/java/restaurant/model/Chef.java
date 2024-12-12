package hr.java.restaurant.model;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.exception.DuplicateEntryException;
import hr.java.service.Constants;
import hr.java.service.Input;
import hr.java.service.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Represents a chef in a restaurant.
 */
public class Chef extends Person {
    private static final Logger logger = LoggerFactory.getLogger(Chef.class);

    private static Long counter = 0L;
    private Contract contract;
    private final Bonus bonus;

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

    /**
     * Inputs chef data from the console.
     *
     * @param numOfElements the chefs
     * @param people        the Person array
     * @param scanner       the scanner object used for input
     * @return returns Set of Chef
     */
    public static Set<Chef> inputChefSet(int numOfElements, Set<Person> people, Scanner scanner) {
        Set<Chef> chefs = new HashSet<>();

        for (int i = 0; i < numOfElements; i++) {
            logger.info("Chef input");
            String chefFirstName, chefLastName;

            while (true) {
                chefFirstName = Input.string(scanner, "Unesite ime "+(i+1)+". kuhara.");
                chefLastName = Input.string(scanner, "Unesite prezime "+(i+1)+". kuhara.");

                try {
                    Validation.checkDuplicatePerson(people, chefFirstName + " " + chefLastName);
                    Validation.checkDuplicateChef(chefs, chefFirstName + " " + chefLastName);
                    break;
                } catch (DuplicateEntryException e) {
                    System.out.println("Kuhar s tim imenom i prezimenom već postoji. Molimo unesite drugo ime i prezime.");
                }
            }

            Contract contract = Input.contract(scanner, "Unesite ugovor "+(i+1)+". kuhara.");
            BigDecimal bonus = Input.bigDecimal(scanner, "Unesite bonus "+(i+1)+". kuhara.");

            chefs.add(
                    new Builder()
                    .id(++counter)
                    .firstName(chefFirstName)
                    .lastName(chefLastName)
                    .contract(contract)
                    .bonus(new Bonus(bonus))
                    .build()
            );
        }
        return chefs;
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
