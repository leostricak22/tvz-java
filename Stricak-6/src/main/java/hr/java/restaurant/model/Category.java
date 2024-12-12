package hr.java.restaurant.model;

import hr.java.restaurant.exception.DuplicateEntryException;
import hr.java.service.Constants;
import hr.java.service.Input;
import hr.java.service.Output;
import hr.java.service.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Represents a category of a meal.
 */
public class Category extends Entity implements Serializable {
    private static Long counter = 0L;
    private static final Logger logger = LoggerFactory.getLogger(Category.class);

    private String name;
    private String description;

    /**
     * Constructs a Category object using the provided builder.
     * @param builder the builder instance containing category information
     */
    private Category(Builder builder) {
        super(builder.id);
        this.name = builder.name;
        this.description = builder.description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Checks if the category with the provided name already exists in the array of categories.
     * @param categories the categories
     * @param categoryName the name of the category
     * @return the index of the category if it exists, -1 otherwise
     */
    public static Integer existsByName(List<Category> categories, String categoryName) {
        for (int j=0;j<categories.size();j++) {
            if (categoryName.equals(categories.get(j).getName())) {
                return j;
            }
        }
        return -1;
    }

    /**
     * Inputs the category information.
     *
     * @param numOfElements number of categories
     * @param scanner       the scanner object used for input
     * @return
     */
    public static List<Category> inputCategoryList(int numOfElements, Scanner scanner) {
        logger.info("Category input");
        List<Category> categories = new ArrayList<>();

        for(int i = 0; i < numOfElements; i++) {
            String categoryName;

            while(true) {
                categoryName = Input.string(scanner, "Unesite naziv "+ (i+1) +". kategorije: ");

                try {
                    Validation.checkDuplicateCategory(categories, categoryName);
                    break;
                } catch (DuplicateEntryException e) {
                    System.out.println("Kategorija s tim nazivom već postoji. Molimo unesite drugi naziv.");
                }
            }

            String categoryDescription = Input.string(scanner, "Unesite opis  "+ (i+1) +". kategorije: ");

            categories.add(
                    new Builder()
                    .id(++counter)
                    .name(categoryName)
                    .description(categoryDescription)
                    .build()
            );
        }
        return categories;
    }

    public static Set<Category> readCategoryFromFile() {
        Set<Category> categories = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.FILENAME_CATEGORIES))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int id = Integer.parseInt(line.trim());
                String name = reader.readLine().trim();
                String description = reader.readLine().trim();
                categories.add(new Builder()
                                .id((long) id)
                                .name(name)
                                .description(description)
                                .build());
            }
        } catch (IOException e) {
            System.err.println("Greška pri čitanju datoteke: " + e.getMessage());
        }
        return categories;
    }

    /**
     * Returns the names of the categories in the array.
     * @param categories the categories
     * @return the category names
     */
    public static List<String> categoryNameArray(List<Category> categories) {
        List<String> categoryNames = new ArrayList<>();

        for (Category category : categories) {
            categoryNames.add(category.getName());
        }

        return categoryNames;
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

    /**
     * Prints the category details with the specified number of tabulators.
     * @param tabulators the number of tabulators to format the output
     */
    public void print(Integer tabulators) {
        logger.info("Printing category.");
        Output.tabulatorPrint(tabulators);
        System.out.println("Id: " + this.getId() + ", Naziv kategorije: " + this.name + ", Opis kategorije: "+ this.description);
    }

    public static void serializeToFile() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("categories.dat"));
            Category category = new Category.Builder()
                    .id((long) 15)
                    .name("Nez haha")
                    .description("hmhmmh")
                    .build();

            out.writeObject(category);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static void deserializeFromFile() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("categories.dat"));
            Category category  = (Category) in.readObject();
            System.out.println("Podaci o pročitanom objektu:");
            System.out.println("Naziv kategorije: " + category.getName() + ", Opis kategorije: " + category.description);

            in.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}