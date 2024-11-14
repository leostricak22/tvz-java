package hr.java.restaurant.model;

import hr.java.restaurant.exception.DuplicateEntryException;
import hr.java.service.Input;
import hr.java.service.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Scanner;

public class Waiter extends Person {
    private static Long counter = 0L;
    private Contract contract;
    private Bonus bonus;
    private static final Logger logger = LoggerFactory.getLogger(Waiter.class);

    public Waiter(Builder builder) {
        super(builder.id, builder.firstName, builder.lastName);
        this.contract = builder.contract;
        this.bonus = builder.bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    public static Integer existsByName(Waiter[] waiters, String firstName, String lastName) {
        for (int j=0;j<waiters.length;j++) {
            if (firstName.equals(waiters[j].getFirstName()) && lastName.equals(waiters[j].getLastName())) {
                return j;
            }
        }
        return -1;
    }

    public static void inputWaiter(Waiter[] waiters, Person[] people, Scanner scanner) {
        for (int i = 0; i < waiters.length; i++) {
            logger.info("Waiter input");
            String waiterFirstName, waiterLastName;

            while (true) {
                waiterFirstName = Input.string(scanner, "Unesite ime " + (i + 1) + ". konobara.");
                waiterLastName = Input.string(scanner, "Unesite prezime " + (i + 1) + ". konobara.");

                try {
                    Validation.checkDuplicatePerson(people, waiterFirstName + " " + waiterLastName);
                    Validation.checkDuplicatePerson(waiters, waiterFirstName + " " + waiterLastName);
                    break;
                } catch (DuplicateEntryException e) {
                    logger.error("Duplicate waiter entry");
                    System.out.println("Konobar s tim imenom i prezimenom već postoji. Molimo unesite drugo ime i prezime.");
                }
            }

            Contract contract = Input.contract(scanner, "Unesite ugovor  "+(i+1)+". konobara.");
            BigDecimal bonus = Input.bigDecimal(scanner, "Unesite bonus "+(i+1)+". konobara.");

            waiters[i] = new Waiter.Builder()
                    .id(++counter)
                    .firstName(waiterFirstName)
                    .lastName(waiterLastName)
                    .contract(contract)
                    .bonus(new Bonus(bonus))
                    .build();
        }
    }

    public static String[] waiterNameArray(Waiter[] waiters) {
        String[] waiterNames = new String[waiters.length];

        for (int i = 0; i < waiters.length; i++) {
            waiterNames[i] = (waiters[i].getFirstName() + " " + waiters[i].getLastName());
        }

        return waiterNames;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    @Override
    public void print(Integer tabulators) {
        logger.info("Waiter print");
        Person.outputFullData(tabulators, getId(), getFirstName(), getLastName() , contract, bonus);
    }

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
}