package hr.java.restaurant.model;

import hr.java.restaurant.exception.DuplicateEntryException;
import hr.java.restaurant.repository.IngredientRepository;
import hr.java.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Represents an ingredient of a meal.
 */
public class Ingredient extends Entity implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Ingredient.class);

    private String name;
    private final Category category;
    private final BigDecimal kcal;
    private final String preparationMethod;

    private static final IngredientRepository<Ingredient> ingredientRepository = new IngredientRepository<>();

    /**
     * Constructs an Ingredient object
     * @param name the name of the ingredient
     * @param category the category of the ingredient
     * @param kcal the calories of the ingredient
     * @param preparationMethod the preparation method of the ingredient
     */
    public Ingredient(Long id, String name, Category category, BigDecimal kcal, String preparationMethod) {
        super(id);
        this.name = name;
        this.category = category;
        this.kcal = kcal;
        this.preparationMethod = preparationMethod;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
     * Inputs the ingredient information.
     * @param numOfElements number of elements
     * @param categories the categories
     * @param scanner the scanner object used for input
     */
    public static Set<Ingredient> inputIngredientSet(int numOfElements, List<Category> categories, Scanner scanner) {
        Set<Ingredient> ingredients = new java.util.HashSet<>(Set.of());
        for(int i = 0; i < numOfElements; i++) {
            logger.info("Ingredient input");

            String ingredientName;

            while(true) {
                ingredientName = Input.string(scanner, "Unesite naziv "+ (i+1) +". sastojka: ");

                try {
                    Validation.checkDuplicateIngredient(ingredients, ingredientName);
                    break;
                } catch (DuplicateEntryException e) {
                    System.out.println("Sastojak s tim nazivom već postoji. Molimo unesite drugi naziv.");
                }
            }

            Category ingredientCategory = EntityFinder.categoryName(scanner, "Unesite naziv kategorije "+ (i+1) +". sastojka", categories);
            BigDecimal ingredientKcal = Input.bigDecimal(scanner, "Unesite kalorije " + (i+1) + ". sastojka.");
            String ingredientPreparationMethod = Input.string(scanner, "Unesite metodu pripremanja " + (i+1) + ". sastojka: ");

            ingredients.add(new Ingredient((long) 0 ,ingredientName, ingredientCategory, ingredientKcal, ingredientPreparationMethod));
        }

        return ingredients;
    }

    public static Set<Ingredient> readIngredientFromFile() {
        Set<Ingredient> ingredients = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.FILENAME_INGREDIENTS))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int id = Integer.parseInt(line.trim());

                String name = reader.readLine().trim();
                Long categoryId = Long.parseLong(reader.readLine().trim());
                BigDecimal kcal = new BigDecimal(reader.readLine().trim());
                String preparationMethod = reader.readLine().trim();

                Category category = EntityFinder.categoryById(categoryId, Category.readCategoryFromFile());


                ingredients.add(new Ingredient((long) id, name, category, kcal, preparationMethod));
            }
        } catch (IOException e) {
            System.err.println("Greška pri čitanju datoteke: " + e.getMessage());
        }

        return ingredients;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(name, that.name) && Objects.equals(category, that.category) && Objects.equals(kcal, that.kcal) && Objects.equals(preparationMethod, that.preparationMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, kcal, preparationMethod);
    }

    /**
     * Prints the ingredient information.
     * @param tabulators the number of tabulators to be printed before the ingredient information
     */
    public void print(Integer tabulators) {
        logger.info("Ingredient print");
        Output.tabulatorPrint(tabulators);
        System.out.println("Id: " + this.getId() + ", Naziv: " + this.name + ", Kalorije: " + this.kcal + ", Metoda pripreme: " + this.preparationMethod);

        Output.tabulatorPrint(tabulators);
        System.out.println("Kategorija:");
        category.print(tabulators+1);
    }
}
