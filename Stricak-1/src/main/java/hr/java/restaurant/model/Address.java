package hr.java.restaurant.model;

import hr.java.service.Input;

import java.util.Scanner;

public class Address {
    private String street;
    private String houseNumber;
    private String city;
    private String postalCode;

    public Address(String street, String houseNumber, String city, String postalCode) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
        this.postalCode = postalCode;
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


        return new Address(addressStreet, addressHouseNumber, addressCity, addressPostalCode);
    }
}