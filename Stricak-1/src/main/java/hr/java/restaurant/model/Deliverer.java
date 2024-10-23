package hr.java.restaurant.model;

import hr.java.service.Input;

import java.math.BigDecimal;
import java.util.Scanner;

public class Deliverer {
    private String firstName;
    private String lastName;
    private BigDecimal salary;

    public Deliverer(String firstName, String lastName, BigDecimal salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public static Integer existsByName(Deliverer[] deliverers, String firstName, String lastName) {
        for (int j=0;j<deliverers.length;j++) {
            if (firstName.equals(deliverers[j].getFirstName()) && lastName.equals(deliverers[j].getLastName())) {
                return j;
            }
        }
        return -1;
    }

    public static void inputDeliverer(Deliverer[] deliverers, Scanner scanner) {
        for (int i = 0; i < deliverers.length; i++) {
            String delivererFirstName = Input.string(scanner, "Unesite ime "+(i+1)+". dostavljača.");
            String delivererLastName = Input.string(scanner, "Unesite prezime "+(i+1)+". dostavljača.");
            BigDecimal delivererSalary = Input.bigDecimal(scanner, "Unesite plaću "+(i+1)+". dostavljača.");

            deliverers[i] = new Deliverer(delivererFirstName, delivererLastName, delivererSalary);
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

    public void print(Integer tabulators) {
        Input.tabulatorPrint(tabulators);
        System.out.println("Ime: " + this.firstName + ", Prezime: " + this.lastName + ", Plaća: " + this.salary);
    }
}
