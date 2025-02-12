package hr.java.restaurant.model;

import hr.java.restaurant.util.Output;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Abstract class that represents a person.
 */
public abstract class Person extends Entity implements Serializable {
    private final String firstName;
    private final String lastName;
    private Bonus bonus;

    /**
     * Constructs a Person object using the provided data.
     * @param id the id of the person
     * @param firstName the first name of the person
     * @param lastName the last name of the person
     */
    public Person(Long id, String firstName, String lastName, Bonus bonus) {
        super(id, firstName + " " + lastName);
        this.firstName = firstName;
        this.lastName = lastName;
        this.bonus = bonus;
    }

    public Bonus getBonus() {
        return bonus;
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
}
