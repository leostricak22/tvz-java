package hr.java.restaurant.model;

import hr.java.service.Input;

import java.math.BigDecimal;
import java.util.Scanner;

public class Chef {
    private String firstName;
    private String lastName;
    private BigDecimal salary;

    public Chef(String firstName, String lastName, BigDecimal salary) {
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
            BigDecimal chefSalary = Input.bigDecimal(scanner, "Unesite plaću "+(i+1)+". kuhara.");

            chefs[i] = new Chef(chefFirstName, chefLastName, chefSalary);
        }
    }

    public static String[] chefNameArray(Chef[] chefs) {
        String[] chefNames = new String[chefs.length];

        for (int i = 0; i < chefs.length; i++) {
            chefNames[i] = (chefs[i].getFirstName() + " " + chefs[i].getLastName());
        }

        return chefNames;
    }

    public void print(Integer tabulators) {
        Input.tabulatorPrint(tabulators);
        System.out.println("Ime: " + this.firstName + ", Prezime: " + this.lastName + ", Plaća: " + this.salary);
    }
}
