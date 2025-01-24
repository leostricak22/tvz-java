package hr.java.restaurant.model;

import hr.java.restaurant.util.Input;
import hr.java.restaurant.util.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Scanner;

/**
 * Represents an address of a restaurant.
 * */
public class Address extends Entity implements Serializable {

    private final String street;
    private final String houseNumber;
    private final String city;
    private final String postalCode;

    /**
     * Constructs an Address object using the provided builder.
     * @param builder the builder instance containing address information
     */
    private Address(Builder builder) {
        super(builder.id, builder.name);
        this.street = builder.street;
        this.houseNumber = builder.houseNumber;
        this.city = builder.city;
        this.postalCode = builder.postalCode;
    }

    public String getCity() {
        return this.city;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public String getStreet() {
        return this.street;
    }

    public String getHouseNumber() {
        return this.houseNumber;
    }

    /**
     * Builder class for creating instances of {@link Address}.
     */
    public static class Builder {
        private final Long id;
        private String name;
        private String street;
        private String houseNumber;
        private String city;
        private String postalCode;

        public Builder(Long id) {
            this.id = id;
        }

        public Builder setStreet(String street) {
            this.street = street;
            this.name = street;
            return this;
        }

        public Builder setHouseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }
}