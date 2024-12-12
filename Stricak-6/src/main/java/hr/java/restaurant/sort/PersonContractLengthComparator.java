package hr.java.restaurant.sort;

import hr.java.restaurant.model.Person;

import java.util.Comparator;

public class PersonContractLengthComparator implements Comparator<Person> {
    public int compare(Person p1, Person p2) {
        return p1.getContract().getStartDate().compareTo(p2.getContract().getStartDate());
    }
}
