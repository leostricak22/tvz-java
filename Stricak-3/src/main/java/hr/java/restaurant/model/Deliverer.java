package hr.java.restaurant.model;

import hr.java.restaurant.exception.DuplicateEntryException;
import hr.java.service.Input;
import hr.java.service.Validation;

import java.math.BigDecimal;
import java.util.Scanner;

public class Deliverer  extends Person {
    private static Long counter = 0L;
    private Contract contract;
    private Bonus bonus;

    private Deliverer(Builder builder) {
        super(builder.id, builder.firstName, builder.lastName);
        this.contract = builder.contract;
        this.bonus = builder.bonus;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    public static Integer existsByName(Deliverer[] deliverers, String firstName, String lastName) {
        for (int j=0;j<deliverers.length;j++) {
            if (firstName.equals(deliverers[j].getFirstName()) && lastName.equals(deliverers[j].getLastName())) {
                return j;
            }
        }
        return -1;
    }

    public static void inputDeliverer(Deliverer[] deliverers, Person[] people, Scanner scanner) {
        for (int i = 0; i < deliverers.length; i++) {
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

    public static String[] delivererNameArray(Deliverer[] deliverers) {
        String[] delivererNames = new String[deliverers.length];

        for (int i = 0; i < deliverers.length; i++) {
            delivererNames[i] = (deliverers[i].getFirstName() + " " + deliverers[i].getLastName());
        }

        return delivererNames;
    }

    public Integer findNumberOfDeliveries(Order[] orders) {
        Integer deliveryCounter=0;
        Deliverer orderDeliverer = this;

        for(int i=0;i<orders.length;i++) {
            if(orderDeliverer.equals(orders[i].getDeliverer())) {
                deliveryCounter++;
            }
        }

        return deliveryCounter;
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
}
