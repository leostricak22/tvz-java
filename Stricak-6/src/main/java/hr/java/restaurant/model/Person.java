package hr.java.restaurant.model;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.service.Constants;
import hr.java.service.Output;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Abstract class that represents a person.
 */
public abstract class Person extends Entity implements Serializable {
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
    public static Person findHighestSalaryPerson(List<Person> people) {
        Person highestSalaryPerson = people.getFirst();

        for (Person person : people) {
            if (person.getSalary().compareTo(highestSalaryPerson.getSalary()) > 0) {
                highestSalaryPerson = person;
            }
        }

        return highestSalaryPerson;
    }

    public static Set<Person> readPeopleFromFile(String filename) {
        Set<Person> people = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int id = Integer.parseInt(line.trim());
                String firstName = reader.readLine().trim();
                String lastName = reader.readLine().trim();
                BigDecimal salary = new BigDecimal(reader.readLine().trim());
                LocalDate contractStartDate = LocalDate.parse(reader.readLine().trim());
                LocalDate contractEndDate = LocalDate.parse(reader.readLine().trim());
                String contractType = reader.readLine().trim();
                BigDecimal bonus = new BigDecimal(reader.readLine().trim());

                Contract contract = new Contract(salary, contractStartDate, contractEndDate, ContractType.valueOf(contractType));

                switch (filename) {
                    case Constants.FILENAME_DELIVERERS -> {
                        people.add(
                                new Deliverer.Builder()
                                        .id((long) id)
                                        .firstName(firstName)
                                        .lastName(lastName)
                                        .contract(contract)
                                        .bonus(new Bonus(bonus))
                                        .build()
                        );
                    }
                    case Constants.FILENAME_CHEFS -> {
                        people.add(
                                new Chef.Builder()
                                        .id((long) id)
                                        .firstName(firstName)
                                        .lastName(lastName)
                                        .contract(contract)
                                        .bonus(new Bonus(bonus))
                                        .build()
                        );
                    }
                    case Constants.FILENAME_WAITERS -> {
                        people.add(
                                new Waiter.Builder()
                                        .id((long) id)
                                        .firstName(firstName)
                                        .lastName(lastName)
                                        .contract(contract)
                                        .bonus(new Bonus(bonus))
                                        .build()
                        );
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Greška pri čitanju datoteke: " + e.getMessage());
        }

        return people;
    }

    /**
     * Finds the person with the longest contract.
     * @param people the array of people
     * @return the person with the longest contract
     */
    public static Person findLongestContractPerson(List<Person> people) {
        Person longestContractPerson = people.getFirst();

        for (Person person : people) {
            if (person.getContract().getStartDate().isBefore(longestContractPerson.getContract().getStartDate())) {
                longestContractPerson = person;
            }
        }

        return longestContractPerson;
    }

    public static <T extends Person> Set<T> getPersonByIdentifiers(String deliverersInRestaurantIdentifiers, Set<T> people) {
        Set<T> peopleByIdentifiers = new HashSet<>();
        String[] identifiers = deliverersInRestaurantIdentifiers.split(",");

        for (String identifier : identifiers) {
            for (T person : people) {
                if (person.getId().equals(Long.parseLong(identifier))) {
                    peopleByIdentifiers.add(person);
                }
            }
        }

        return peopleByIdentifiers;
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
