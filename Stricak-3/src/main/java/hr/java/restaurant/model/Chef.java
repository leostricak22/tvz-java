package hr.java.restaurant.model;

import hr.java.restaurant.exception.DuplicateEntryException;
import hr.java.service.Input;
import hr.java.service.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Scanner;

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
    public static Integer existsByName(Chef[] chefs, String firstName, String lastName) {
        for (int j=0;j<chefs.length;j++) {
            if (firstName.equals(chefs[j].getFirstName()) && lastName.equals(chefs[j].getLastName())) {
                return j;
            }
        }
        return -1;
    }

    /**
     * Inputs chef data from the console.
     * @param chefs the chefs
     * @param people the Person array
     * @param scanner the scanner object used for input
     */
    public static void inputChef(Chef[] chefs, Person[] people, Scanner scanner) {
        for (int i = 0; i < chefs.length; i++) {
            logger.info("Chef input");
            String chefFirstName, chefLastName;

            while (true) {
                chefFirstName = Input.string(scanner, "Unesite ime "+(i+1)+". kuhara.");
                chefLastName = Input.string(scanner, "Unesite prezime "+(i+1)+". kuhara.");

                try {
                    Validation.checkDuplicatePerson(people, chefFirstName + " " + chefLastName);
                    Validation.checkDuplicatePerson(chefs, chefFirstName + " " + chefLastName);
                    break;
                } catch (DuplicateEntryException e) {
                    System.out.println("Kuhar s tim imenom i prezimenom već postoji. Molimo unesite drugo ime i prezime.");
                }
            }

            Contract contract = Input.contract(scanner, "Unesite ugovor "+(i+1)+". kuhara.");
            BigDecimal bonus = Input.bigDecimal(scanner, "Unesite bonus "+(i+1)+". kuhara.");

            chefs[i] = new Builder()
                    .id(++counter)
                    .firstName(chefFirstName)
                    .lastName(chefLastName)
                    .contract(contract)
                    .bonus(new Bonus(bonus))
                    .build();
        }
    }

    /**
     * Returns an array of chef names.
     * @param chefs the chefs
     * @return the array of chef names
     */
    public static String[] chefNameArray(Chef[] chefs) {
        String[] chefNames = new String[chefs.length];

        for (int i = 0; i < chefs.length; i++) {
            chefNames[i] = (chefs[i].getFirstName() + " " + chefs[i].getLastName());
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
