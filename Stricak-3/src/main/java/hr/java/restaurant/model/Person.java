package hr.java.restaurant.model;

import hr.java.service.Input;
import hr.java.service.Output;

import java.math.BigDecimal;

public abstract class Person extends  Entity{
    private String firstName;
    private String lastName;

    public Person(Long id, String firstName, String lastName) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static Person findHighestSalaryPerson(Person[] people) {
        Person highestSalaryPerson = people[0];

        for (Person person : people) {
            if (person.getSalary().compareTo(highestSalaryPerson.getSalary()) > 0) {
                highestSalaryPerson = person;
            }
        }

        return highestSalaryPerson;
    }

    public static Person findLongestContractPerson(Person[] people) {
        Person longestContractPerson = people[0];

        for (Person person : people) {
            if (person.getContract().getStartDate().isBefore(longestContractPerson.getContract().getStartDate())) {
                longestContractPerson = person;
            }
        }

        return longestContractPerson;
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

    public abstract BigDecimal getSalary();
    public abstract Contract getContract();

    public void print(Integer tabulators) {
        System.out.println("Ime: " + firstName);
        System.out.println("Prezime: " + lastName);
    }

    public static void outputFullData(Integer tabulators, Long id, String firstName, String lastName, Contract contract, Bonus bonus) {
        Output.tabulatorPrint(tabulators);
        System.out.print("Id: " + id + ", Ime: " + firstName + ", Prezime: " + lastName);

        if(bonus.amount().compareTo(BigDecimal.ZERO) > 0) {
            System.out.println(", Bonus: " + bonus.amount());
        } else {
            System.out.println();
        }


        Output.tabulatorPrint(tabulators+1);
        System.out.println("Ugovor: ");
        contract.print(tabulators+2);
    }
}
