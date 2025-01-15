package hr.java.restaurant.model;

import hr.java.restaurant.exception.DuplicateEntryException;
import hr.java.restaurant.util.Constants;
import hr.java.restaurant.util.Input;
import hr.java.restaurant.util.Output;
import hr.java.restaurant.util.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Represents a category of a meal.
 */
public class Category extends Entity implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Category.class);

    private String description;

    /**
     * Constructs a Category object using the provided builder.
     * @param builder the builder instance containing category information
     */
    private Category(Builder builder) {
        super(builder.id, builder.name);
        this.description = builder.description;
        logger.info("Category created: {}", this);
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
        private Long id;
        private String name;
        private String description;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Category build() {
            return new Category(this)   ;
        }
    }
}