package hr.java.restaurant.model;

import hr.java.restaurant.exception.DuplicateEntryException;
import hr.java.service.Input;
import hr.java.service.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Represents a deliverer in a restaurant.
 */
public class Deliverer  extends Person {
    private static final Logger logger = LoggerFactory.getLogger(Deliverer.class);

    private static Long counter = 0L;
    private final Contract contract;
    private final Bonus bonus;

    /**
     * Constructs a Deliverer object using the provided builder.
     * @param builder the builder instance containing deliverer information
     */
    private Deliverer(Builder builder) {
        super(builder.id, builder.firstName, builder.lastName);
        this.contract = builder.contract;
        this.bonus = builder.bonus;
    }

    /**
     * Checks if the deliverer with the provided name already exists in the array of deliverers.
     * @param deliverers the deliverers
     * @param firstName the first name of the deliverer
     * @param lastName the last name of the deliverer
     */
    public static Integer existsByName(Deliverer[] deliverers, String firstName, String lastName) {
        for (int j=0;j<deliverers.length;j++) {
            if (firstName.equals(deliverers[j].getFirstName()) && lastName.equals(deliverers[j].getLastName())) {
                return j;
            }
        }
        return -1;
    }

    /**
     * Inputs deliverer data from the console.
     * @param deliverers the deliverers
     * @param people the Person array
     * @param scanner the scanner object used for input
     */
    public static void inputDeliverer(Deliverer[] deliverers, Person[] people, Scanner scanner) {
        for (int i = 0; i < deliverers.length; i++) {
            logger.info("Deliverer input");
            String delivererFirstName, delivererLastName;

            while (true) {
                delivererFirstName = Input.string(scanner, "Unesite ime "+(i+1)+". dostavljača.");
                delivererLastName = Input.string(scanner, "Unesite prezime "+(i+1)+". dostavljača.");

                try {
                    Validation.checkDuplicatePerson(people, delivererFirstName + " " + delivererLastName);
                    Validation.checkDuplicatePerson(deliverers, delivererFirstName + " " + delivererLastName);
                    break;
                } catch (DuplicateEntryException e) {
                    System.out.println("Dostavljač s tim imenom i prezimenom već postoji. Molimo unesite drugo ime i prezime.");
                }
            }

            Contract contract = Input.contract(scanner, "Unesite ugovor "+(i+1)+". dostavljača.");
            BigDecimal bonus = Input.bigDecimal(scanner, "Unesite bonus "+(i+1)+". dostavljača.");

            deliverers[i] = new Builder()
                    .id(++counter)
                    .firstName(delivererFirstName)
                    .lastName(delivererLastName)
                    .contract(contract)
                    .bonus(new Bonus(bonus))
                    .build();
        }
    }

    /**
     * Returns the names of the deliverers in the array.
     * @param deliverers the deliverers
     * @return the array of deliverer names
     */
    public static String[] delivererNameArray(Deliverer[] deliverers) {
        String[] delivererNames = new String[deliverers.length];

        for (int i = 0; i < deliverers.length; i++) {
            delivererNames[i] = (deliverers[i].getFirstName() + " " + deliverers[i].getLastName());
        }

        return delivererNames;
    }

    /**
     * Returns the number of deliveries for the deliverer.
     * @param orders the orders
     * @return the number of deliveries
     */
    public Integer findNumberOfDeliveries(Order[] orders) {
        Integer deliveryCounter=0;
        Deliverer orderDeliverer = this;

        for (Order order : orders) {
            if (orderDeliverer.equals(order.getDeliverer())) {
                deliveryCounter++;
            }
        }

        return deliveryCounter;
    }

    /**
     * Builder class for creating instances of {@link Deliverer}.
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

        public Deliverer build() {
            return new Deliverer(this);
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
     * Prints the deliverer details with the specified number of tabulators.
     * @param tabulators the number of tabulators to format the output
     */
    @Override
    public void print(Integer tabulators) {
        logger.info("Printing deliverer.");
        Person.outputFullData(tabulators, getId(), getFirstName(), getLastName() , contract, bonus);
    }
}
