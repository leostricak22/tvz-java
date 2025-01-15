package hr.java.restaurant.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Represents an ingredient of a meal.
 */
public class Ingredient extends Entity implements Serializable {

    private Category category;
    private BigDecimal kcal;
    private String preparationMethod;

    private Ingredient(Builder builder) {
        super(builder.id, builder.name);
        this.category = builder.category;
        this.kcal = builder.kcal;
        this.preparationMethod = builder.preparationMethod;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getKcal() {
        return kcal;
    }

    public void setKcal(BigDecimal kcal) {
        this.kcal = kcal;
    }

    public String getPreparationMethod() {
        return preparationMethod;
    }

    public void setPreparationMethod(String preparationMethod) {
        this.preparationMethod = preparationMethod;
    }

    public static class Builder {
        private final Long id;
        private final String name;
        private Category category;
        private BigDecimal kcal;
        private String preparationMethod;

        public Builder(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Builder setCategory(Category category) {
            this.category = category;
            return this;
        }

        public Builder setKcal(BigDecimal kcal) {
            this.kcal = kcal;
            return this;
        }

        public Builder setPreparationMethod(String preparationMethod) {
            this.preparationMethod = preparationMethod;
            return this;
        }

        public Ingredient build() {
            return new Ingredient(this);
        }
    }
}
