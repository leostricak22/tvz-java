package hr.java.restaurant.model;

import hr.java.service.Input;
import hr.java.service.Output;

import java.util.Scanner;

public class Category extends Entity {
    private static Long counter = 0L;

    private String name;
    private String description;

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

    public void setDescription(String description) {
        this.description = description;
    }

    public static Integer existsByName(Category[] categories, String categoryName) {
        for (int j=0;j<categories.length;j++) {
            if (categoryName.equals(categories[j].getName())) {
                return j;
            }
        }
        return -1;
    }

    public static void inputCategory(Category[] categories, Scanner scanner) {
        for(int i = 0; i < categories.length; i++) {
            String categoryName = Input.string(scanner, "Unesite naziv "+ (i+1) +". kategorije: ");
            String categoryDescription = Input.string(scanner, "Unesite opis  "+ (i+1) +". kategorije: ");

            categories[i] = new Builder()
                    .id(++counter)
                    .name(categoryName)
                    .description(categoryDescription)
                    .build();
        }
    }

    public static String[] categoryNameArray(Category[] categories) {
        String[] categoryNames = new String[categories.length];

        for (int i = 0; i < categories.length; i++) {
            categoryNames[i] = categories[i].getName();
        }

        return categoryNames;
    }

    public void print(Integer tabulators) {
        Output.tabulatorPrint(tabulators);
        System.out.println("Id: " + this.getId() + ", Naziv kategorije: " + this.name + ", Opis kategorije: "+ this.description);
    }

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