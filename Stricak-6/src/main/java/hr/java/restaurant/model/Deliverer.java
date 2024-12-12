package hr.java.restaurant.model;

import hr.java.restaurant.exception.DuplicateEntryException;
import hr.java.service.Input;
import hr.java.service.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Represents a deliverer in a restaurant.
 */
public class Deliverer extends Person implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Deliverer.class);

    private static Long counter = 0L;
    private final Contract contract;
    private Bonus bonus;

    /**
     * Constructs a Deliverer object using the provided builder.
     * @param builder the builder instance containing deliverer information
     */
    private Deliverer(Builder builder) {
        super(builder.id, builder.firstName, builder.lastName);
        this.contract = builder.contract;
        this.bonus = builder.bonus;
    }

    public Bonus getBonus() {
        return bonus;
    }

    /**
     * Checks if the deliverer with the provided name already exists in the array of deliverers.
     * @param deliverers the deliverers
     * @param firstName the first name of the deliverer
     * @param lastName the last name of the deliverer
     */
    public static Integer existsByName(Set<Deliverer> deliverers, String firstName, String lastName) {
        int i=0;
        for (Deliverer deliverer : deliverers) {
            if (deliverer.getFirstName().equals(firstName) && deliverer.getLastName().equals(lastName)) {
                return i;
            }

            i++;
        }

        return -1;
    }

    /**
     * Inputs deliverer data from the console.
     * @param numOfElements the deliverers
     * @param people the Person array
     * @param scanner the scanner object used for input
     */
    public static Set<Deliverer> inputDeliverer(int numOfElements, Set<Person> people, Scanner scanner) {
        Set<Deliverer> deliverers = new HashSet<>();

        for (int i = 0; i < numOfElements; i++) {
            logger.info("Deliverer input");
            String delivererFirstName, delivererLastName;

            while (true) {
                delivererFirstName = Input.string(scanner, "Unesite ime "+(i+1)+". dostavljača.");
                delivererLastName = Input.string(scanner, "Unesite prezime "+(i+1)+". dostavljača.");

                try {
                    Validation.checkDuplicatePerson(people, delivererFirstName + " " + delivererLastName);
                    Validation.checkDuplicateDeliverer(deliverers, delivererFirstName + " " + delivererLastName);
                    break;
                } catch (DuplicateEntryException e) {
                    System.out.println("Dostavljač s tim imenom i prezimenom već postoji. Molimo unesite drugo ime i prezime.");
                }
            }

            Contract contract = Input.contract(scanner, "Unesite ugovor "+(i+1)+". dostavljača.");
            BigDecimal bonus = Input.bigDecimal(scanner, "Unesite bonus "+(i+1)+". dostavljača.");

            deliverers.add(
                    new Builder()
                    .id(++counter)
                    .firstName(delivererFirstName)
                    .lastName(delivererLastName)
                    .contract(contract)
                    .bonus(new Bonus(bonus))
                    .build()
            );
        }

        return deliverers;
    }

    /**
     * Returns the names of the deliverers in the array.
     * @param deliverers the deliverers
     * @return the array of deliverer names
     */
    public static List<String> delivererNameArray(Set<Deliverer> deliverers) {
        List<String> delivererNames = new ArrayList<>();

        for (Deliverer deliverer : deliverers) {
            delivererNames.add(deliverer.getFirstName() + " " + deliverer.getLastName());
        }

        return delivererNames;
    }

    /**
     * Returns the number of deliveries for the deliverer.
     * @param orders the orders
     * @return the number of deliveries
     */
    public Integer findNumberOfDeliveries(List<Order> orders) {
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

    public static Deliverer getDelivererByIdentifiers(String deliverersIdentifiers, Set<Deliverer> deliverers) {
        String[] identifiers = deliverersIdentifiers.split(",");
        for (String identifier : identifiers) {
            for (Deliverer deliverer : deliverers) {
                if (deliverer.getId().equals(Long.parseLong(identifier))) {
                    return deliverer;
                }
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deliverer deliverer = (Deliverer) o;
        return Objects.equals(contract, deliverer.contract) && Objects.equals(bonus, deliverer.bonus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contract, bonus);
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
