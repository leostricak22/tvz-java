package hr.java.restaurant.model;

import hr.java.restaurant.repository.IngredientRepository;
import hr.java.restaurant.util.EntityFinder;
import hr.java.restaurant.util.Input;
import hr.java.restaurant.util.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Represents an ingredient of a meal.
 */
public class Ingredient extends Entity implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Ingredient.class);

    private final Category category;
    private final BigDecimal kcal;
    private final String preparationMethod;

    private static final IngredientRepository<Ingredient> ingredientRepository = new IngredientRepository<>();

    private Ingredient(Builder builder) {
        super(builder.id, builder.name);
        this.category = builder.category;
        this.kcal = builder.kcal;
        this.preparationMethod = builder.preparationMethod;
    }

    public Category getCategory() {
        return category;
    }

    public String getPreparationMethod() {
        return preparationMethod;
    }

    public static Set<Ingredient> getIngredientsByIdentifiers(String ingredientsIdentifiers) {
        Set<Ingredient> ingredientsList = new HashSet<>();
        String[] identifiers = ingredientsIdentifiers.split(",");
        for (String identifier : identifiers) {
            ingredientsList.add(ingredientRepository.findById(Long.parseLong(identifier)));
        }

        return ingredientsList;
    }

    public BigDecimal getKcal() {
        return kcal;
    }

    /**
     * Checks if the ingredient with the provided name already exists in the array of ingredients.txt.
     * @param ingredients the ingredients.txt
     * @param ingredientName the name of the ingredient
     * @return the index of the ingredient if it exists, -1 otherwise
     */
    public static Integer existsByName(Set<Ingredient> ingredients, String ingredientName) {
        int index = 0;
        for (Ingredient ingredient : ingredients) {
            System.out.println(ingredient.getName() + " " + ingredientName);
            if (ingredientName.equals(ingredient.getName())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * Returns an array of ingredient names.
     * @param ingredients the ingredients.txt
     * @return the string list of ingredient names
     */
    public static List<String> ingredientNameArray(Set<Ingredient> ingredients) {
        List<String> ingredientsNames = new ArrayList<>();

        for (Ingredient ingredient : ingredients) {
            ingredientsNames.add(ingredient.getName());
        }

        return ingredientsNames;
    }

    static Set<Ingredient> enterArrayOfIngredients(Set<Ingredient> ingredients, Scanner scanner) {
        int numberOfIngredients = Input.integer(scanner, "Unesite broj sastojaka koji želite dodati: ");
        Set<Ingredient> ingredientsEntered = new java.util.HashSet<>(Set.of());
        for(int j=0;j<numberOfIngredients;j++) {
            ingredientsEntered.add(EntityFinder.ingredientName(scanner,"Unesite naziv sastojka kojeg želite dodati u jelo: ", ingredients));
        }

        return ingredientsEntered;
    }

    /**
     * Prints the ingredient information.
     * @param tabulators the number of tabulators to be printed before the ingredient information
     */
    public void print(Integer tabulators) {
        logger.info("Ingredient print");
        Output.tabulatorPrint(tabulators);
        System.out.println("Id: " + this.getId() + ", Naziv: " + super.getName() + ", Kalorije: " + this.kcal + ", Metoda pripreme: " + this.preparationMethod);

        Output.tabulatorPrint(tabulators);
        System.out.println("Kategorija:");
        category.print(tabulators+1);
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
