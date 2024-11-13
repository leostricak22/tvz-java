package hr.java.restaurant.model;

import hr.java.service.Input;

import java.math.BigDecimal;
import java.util.Scanner;

public class Chef extends Person {
    private static Long counter = 0L;
    private Contract contract;
    private Bonus bonus;

    private Chef(Builder builder) {
        super(builder.id, builder.firstName, builder.lastName);
        this.contract = builder.contract;
        this.bonus = builder.bonus;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public static Integer existsByName(Chef[] chefs, String firstName, String lastName) {
        for (int j=0;j<chefs.length;j++) {
            if (firstName.equals(chefs[j].getFirstName()) && lastName.equals(chefs[j].getLastName())) {
                return j;
            }
        }
        return -1;
    }

    public static void inputChef(Chef[] chefs, Scanner scanner) {
        for (int i = 0; i < chefs.length; i++) {
            String chefFirstName = Input.string(scanner, "Unesite ime "+(i+1)+". kuhara.");
            String chefLastName = Input.string(scanner, "Unesite prezime "+(i+1)+". kuhara.");
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

    public static String[] chefNameArray(Chef[] chefs) {
        String[] chefNames = new String[chefs.length];

        for (int i = 0; i < chefs.length; i++) {
            chefNames[i] = (chefs[i].getFirstName() + " " + chefs[i].getLastName());
        }

        return chefNames;
    }

    @Override
    public void print(Integer tabulators) {
        Person.outputFullData(tabulators, getId(), getFirstName(), getLastName() , contract, bonus);
    }

    public Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
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
}
