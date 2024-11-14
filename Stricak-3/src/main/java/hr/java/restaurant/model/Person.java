package hr.java.restaurant.model;

import hr.java.service.Input;
import hr.java.service.Output;

import java.math.BigDecimal;

/**
 * Abstract class that represents a person.
 */
public abstract class Person extends  Entity{
    private final String firstName;
    private final String lastName;

    /**
     * Constructs a Person object using the provided data.
     * @param id the id of the person
     * @param firstName the first name of the person
     * @param lastName the last name of the person
     */
    public Person(Long id, String firstName, String lastName) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Finds the person with the highest salary.
     * @param people the array of people
     * @return the person with the highest salary
     */
    public static Person findHighestSalaryPerson(Person[] people) {
        Person highestSalaryPerson = people[0];

        for (Person person : people) {
            if (person.getSalary().compareTo(highestSalaryPerson.getSalary()) > 0) {
                highestSalaryPerson = person;
            }
        }

        return highestSalaryPerson;
    }

    /**
     * Finds the person with the longest contract.
     * @param people the array of people
     * @return the person with the longest contract
     */
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
    public String getLastName() {
        return lastName;
    }

    public abstract BigDecimal getSalary();
    public abstract Contract getContract();

    /**
     * Prints the person's information.
     * @param tabulators the number of tabulators to format the output
     */
    public void print(Integer tabulators) {
        System.out.println("Ime: " + firstName);
        System.out.println("Prezime: " + lastName);
    }

    /**
     * Prints the person's full information.
     * @param tabulators the number of tabulators to format the output
     * @param id the id of the person
     * @param firstName the first name of the person
     * @param lastName the last name of the person
     * @param contract the contract of the person
     * @param bonus the bonus of the person
     */
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
