package hr.javafx.coffe.caffee.javafxcaffee.model;

import java.io.Serializable;
import java.math.BigDecimal;

public non-sealed class Beverage extends Entity implements Alcoholic, Serializable {

    private BigDecimal price;
    private BigDecimal alcoholPercentage;
    private Origin origin;

    public Beverage(Long id, String name, BigDecimal price,
                    BigDecimal alcoholPercentage, Origin origin)
    {
        super(id, name);
        this.price = price;
        this.alcoholPercentage = alcoholPercentage;
        this.origin = origin;
    }

    public Beverage(String name, BigDecimal price,
                    BigDecimal alcoholPercentage, Origin origin)
    {
        super(name);
        this.price = price;
        this.alcoholPercentage = alcoholPercentage;
        this.origin = origin;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public BigDecimal calculatePercentage() {
        return alcoholPercentage;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }
}
