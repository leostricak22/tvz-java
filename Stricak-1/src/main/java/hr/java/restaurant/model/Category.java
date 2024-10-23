package hr.java.restaurant.model;

import hr.java.service.Input;

import java.util.Scanner;

public class Category {
    private String name;
    private String description;

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
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

            categories[i] = new Category(categoryName, categoryDescription);
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
        Input.tabulatorPrint(tabulators);
        System.out.println("Naziv kategorije: " + this.name + ", Opis kategorije: "+ this.description);
    }
}