package hr.java.restaurant.model;

import hr.java.service.Input;

import java.math.BigDecimal;
import java.util.Scanner;

public class Waiter extends Person {
    private static Long counter = 0L;
    private Contract contract;
    private Bonus bonus;

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

    public static void inputWaiter(Waiter[] waiters, Scanner scanner) {
        for (int i = 0; i < waiters.length; i++) {
            String waiterFirstName = Input.string(scanner, "Unesite ime "+(i+1)+". konobara.");
            String waiterLastName = Input.string(scanner, "Unesite prezime "+(i+1)+". konobara.");
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