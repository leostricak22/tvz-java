package hr.java.restaurant.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Represents a bonus that can be given to a person.
 */
public record Bonus(BigDecimal amount) implements Serializable {
}
