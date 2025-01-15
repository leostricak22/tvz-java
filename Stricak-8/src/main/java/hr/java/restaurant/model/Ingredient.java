package hr.java.restaurant.model;

import hr.java.restaurant.repository.IngredientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Represents an ingredient of a meal.
 */
public class Ingredient extends Entity implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Ingredient.class);

    private Category category;
    private BigDecimal kcal;
    private String preparationMethod;

    private static final IngredientRepository<Ingredient> ingredientRepository = new IngredientRepository<>();

    private Ingredient(Builder builder) {
        super(builder.id, builder.name);
        this.category = builder.category;
        this.kcal = builder.kcal;
        this.preparationMethod = builder.preparationMethod;
        logger.info("Ingredient created: {}", this);
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
