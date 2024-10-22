package hr.java.restaurant.model;

import hr.java.service.Input;

import java.math.BigDecimal;
import java.util.Scanner;

public class Waiter {
    private String firstName;
    private String lastName;
    private BigDecimal salary;

    public Waiter(String firstName, String lastName, BigDecimal salary) {
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
            BigDecimal waiterSalary = Input.bigDecimal(scanner, "Unesite plaću "+(i+1)+". konobara.");

            waiters[i] = new Waiter(waiterFirstName, waiterLastName, waiterSalary);
        }
    }

    public static String[] waiterNameArray(Waiter[] waiters) {
        String[] waiterNames = new String[waiters.length];

        for (int i = 0; i < waiters.length; i++) {
            waiterNames[i] = (waiters[i].getFirstName() + " " + waiters[i].getLastName());
        }

        return waiterNames;
    }
}