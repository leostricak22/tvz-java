package hr.javafx.coffe.caffee.javafxcaffee.model;

import java.util.List;
import java.util.Objects;

public class CoffeeShop extends Entity {

    private String address;
    private String phone;
    private String email;
    private String owner;
    private List<Beverage> priceList;

    public CoffeeShop(Long id, String name, String address, String phone, String email, String owner, List<Beverage> priceList) {
        super(id, name);
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.owner = owner;
        this.priceList = priceList;
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

    public List<Beverage> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<Beverage> priceList) {
        this.priceList = priceList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoffeeShop that = (CoffeeShop) o;
        return Objects.equals(address, that.address) && Objects.equals(phone, that.phone) && Objects.equals(email, that.email) && Objects.equals(owner, that.owner) && Objects.equals(priceList, that.priceList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, phone, email, owner, priceList);
    }
}
