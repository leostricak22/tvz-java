package hr.java.restaurant.model;

import java.io.*;

/**
 * Represents a category of a meal.
 */
public class Category extends Entity implements Serializable {

    private String description;

    /**
     * Constructs a Category object using the provided builder.
     * @param builder the builder instance containing category information
     */
    private Category(Builder builder) {
        super(builder.id, builder.name);
        this.description = builder.description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Builder class for creating instances of {@link Category}.
     */
    public static class Builder {
        private final Long id;
        private String name;
        private String description;

        public Builder(Long id) {
            this.id = id;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Category build() {
            return new Category(this)   ;
        }
    }
}