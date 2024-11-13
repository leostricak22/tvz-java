package hr.java.restaurant.model;

import hr.java.service.Input;
import hr.java.service.Output;

import java.util.Scanner;

public class Address extends Entity {
    private static Long counter = 0L;

    private String street;
    private String houseNumber;
    private String city;
    private String postalCode;

    private Address(Builder builder) {
        super(builder.id);
        this.street = builder.street;
        this.houseNumber = builder.houseNumber;
        this.city = builder.city;
        this.postalCode = builder.postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public static Address inputAddress(Scanner scanner) {
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

    public void print(Integer tabulators) {
        Output.tabulatorPrint(tabulators);
        System.out.println("Id: " + this.getId() + ", Ulica: " + this.street + ", Kućni broj: " + this.houseNumber + ", Grad: " + this.city + ", Poštanski broj: " + this.postalCode);
    }

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
}