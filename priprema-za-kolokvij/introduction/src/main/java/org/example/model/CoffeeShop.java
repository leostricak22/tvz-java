package org.example.model;

public class CoffeeShop {
    private String name;
    private String address;
    private String phone;
    private String email;
    private String owner;
    private Beverage[] priceList;

    // konstruktor -> metoda koja služi za kreiranje objekata neke klase
    public CoffeeShop(String name, String address, String phone, String email, String owner, Beverage[] priceList) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.owner = owner;
        this.priceList = priceList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Beverage[] getPriceList() {
        return priceList;
    }

    public void setPriceList(Beverage[] priceList) {
        this.priceList = priceList;
    }
}
