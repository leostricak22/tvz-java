package hr.java.restaurant.model;

import hr.java.restaurant.exception.DuplicateEntryException;
import hr.java.service.Input;
import hr.java.service.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

/**
 * Represents a waiter.
 */
public class Waiter extends Person {
    private static Long counter = 0L;
    private final Contract contract;
    private final Bonus bonus;
    private static final Logger logger = LoggerFactory.getLogger(Waiter.class);

    /**
     * Constructs a Waiter object using the provided builder.
     * @param builder the builder instance containing waiter information
     */
    public Waiter(Builder builder) {
        super(builder.id, builder.firstName, builder.lastName);
        this.contract = builder.contract;
        this.bonus = builder.bonus;
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
     * Inputs waiter data from the console.
     * @param numOfElements the waiters
     * @param people the Person array
     * @param scanner the scanner object used for input
     */
    public static Set<Waiter> inputWaiterSet(int numOfElements, Set<Person> people, Scanner scanner) {
        Set<Waiter> waiters = new HashSet<>();

        for (int i = 0; i < numOfElements; i++) {
            logger.info("Waiter input");
            String waiterFirstName, waiterLastName;

            while (true) {
                waiterFirstName = Input.string(scanner, "Unesite ime " + (i + 1) + ". konobara.");
                waiterLastName = Input.string(scanner, "Unesite prezime " + (i + 1) + ". konobara.");

                try {
                    Validation.checkDuplicatePerson(people, waiterFirstName + " " + waiterLastName);
                    Validation.checkDuplicateWaiter(waiters, waiterFirstName + " " + waiterLastName);
                    break;
                } catch (DuplicateEntryException e) {
                    logger.error("Duplicate waiter entry");
                    System.out.println("Konobar s tim imenom i prezimenom već postoji. Molimo unesite drugo ime i prezime.");
                }
            }

            Contract contract = Input.contract(scanner, "Unesite ugovor  "+(i+1)+". konobara.");
            BigDecimal bonus = Input.bigDecimal(scanner, "Unesite bonus "+(i+1)+". konobara.");

            waiters.add(
                    new Builder()
                    .id(++counter)
                    .firstName(waiterFirstName)
                    .lastName(waiterLastName)
                    .contract(contract)
                    .bonus(new Bonus(bonus))
                    .build()
            );
        }

        return waiters;
    }

    /**
     * Returns the waiter's first name.
     * @return the waiter's first name
     */
    public static String[] waiterNameArray(Set<Waiter> waiters) {
        String[] waiterNames = new String[waiters.size()];

        int i = 0;
        for (Waiter waiter : waiters) {
            waiterNames[i] = waiter.getFirstName() + " " + waiter.getLastName();
            i++;
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