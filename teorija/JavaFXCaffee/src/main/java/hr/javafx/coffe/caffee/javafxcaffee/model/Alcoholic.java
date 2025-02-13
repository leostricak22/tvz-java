package hr.javafx.coffe.caffee.javafxcaffee.model;

import java.math.BigDecimal;

public sealed interface Alcoholic permits Beverage {
    BigDecimal calculatePercentage();
}
