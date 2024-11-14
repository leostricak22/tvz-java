package hr.java.restaurant.model;

import hr.java.service.Input;
import hr.java.service.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Represents an address of a restaurant.
 * */
public class Address extends Entity {
    private static final Logger logger = LoggerFactory.getLogger(Address.class);

    private static Long counter = 0L;

    private final String street;
    private final String houseNumber;
    private final String city;
    private final String postalCode;

    /**
     * Constructs an Address object using the provided builder.
     * @param builder the builder instance containing address information
     */
    private Address(Builder builder) {
        super(builder.id);
        this.street = builder.street;
        this.houseNumber = builder.houseNumber;
        this.city = builder.city;
        this.postalCode = builder.postalCode;
    }

    /**
     * Prompts the user to input address details using a scanner.
     * @param scanner the Scanner object to read user input
     * @return a new Address instance with the entered details
     */
    public static Address inputAddress(Scanner scanner) {
        logger.info("Address input.");
        String addressStreet = Input.string(scanner, "Unesite ulicu restorana: ");
        String addressHouseNumber = Input.string(scanner, "Unesite kućni broj restorana: ");
        String addressCity = Input.string(scanner, "Unesite grad u kojem se nalazi restoran: ");

        String addressPostalCode = Input.postalCode(scanner, "Unesite poštanski broj restorana: ");

        return new Address.Builder()
                .id(++counter)
                .street(addressStreet)
                .houseNumber(addressHouseNumber)
                .city(addressCity)
                .postalCode(addressPostalCode)
                .build();
    }

    /**
     * Builder class for creating instances of {@link Address}.
     */
    public static class Builder {
        private Long id;
        private String street;
        private String houseNumber;
        private String city;
        private String postalCode;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder houseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }

    /**
     * Prints the address details with the specified number of tabulators.
     * @param tabulators the number of tabulators to format the output
     */
    public void print(Integer tabulators) {
        logger.info("Printing address.");
        Output.tabulatorPrint(tabulators);
        System.out.println("Id: " + this.getId() + ", Ulica: " + this.street + ", Kućni broj: " + this.houseNumber + ", Grad: " + this.city + ", Poštanski broj: " + this.postalCode);
    }
}