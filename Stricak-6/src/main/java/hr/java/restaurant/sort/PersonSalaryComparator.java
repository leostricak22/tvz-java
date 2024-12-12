package hr.java.restaurant.sort;

import hr.java.restaurant.model.Person;

import java.util.Comparator;

public class PersonSalaryComparator implements Comparator<Person> {
    @Override
    public int compare(Person p1, Person p2) {
        return p2.getSalary().compareTo(p1.getSalary());
    }
}
