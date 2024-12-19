package hr.java.restaurant.repository;

import hr.java.restaurant.model.*;
import hr.java.service.EntityFinder;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class MealRepository<T extends Meal> extends AbstractRepository<T> {
    public final static String FILE_PATH = "dat/meals.txt";
    public final static Integer NUMBER_OF_VEGAN_MEALS = 3;
    public final static Integer NUMBER_OF_VEGETARIAN_MEALS = 3;
    public final static Integer NUMBER_OF_MEAT_MEALS = 3;

    public final CategoryRepository<Category> categoryRepository = new CategoryRepository<>();

    @Override
    public T findById(Long id) {
        return findAll().stream()
                .filter(meal -> meal.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nepostojeći identifikator"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<T> findAll() {
        Set<T> meals = new HashSet<>();

        try (Stream<String> stream = Files.lines(Path.of(FILE_PATH))) {
            List<String> fileRows = stream.toList();
            int counter = 0;

            while (!fileRows.isEmpty()) {
                Long id = Long.parseLong(fileRows.get(0));
                String name = fileRows.get(1);
                String mealType = fileRows.get(2);
                Long categoryId = Long.parseLong(fileRows.get(3));
                String ingredientsIdentifiers = fileRows.get(4);
                String price = fileRows.get(5);

                Category category = categoryRepository.findById(categoryId);
                Set<Ingredient> ingredients = Ingredient.getIngredientsByIdentifiers(ingredientsIdentifiers);

                if(mealType.equals("vegan")) {
                    String proteinSource = fileRows.get(6);
                    boolean organic = Boolean.parseBoolean(fileRows.get(7));
                    boolean glutenFree = Boolean.parseBoolean(fileRows.get(8));

                    meals.add((T) new VeganMeal(id, name, mealType, category, ingredients, new java.math.BigDecimal(price), proteinSource, organic, glutenFree));
                } else if(mealType.equals("vegetarian")) {
                    String proteinSource = fileRows.get(6);
                    boolean containsDairy = Boolean.parseBoolean(fileRows.get(7));
                    boolean containsEggs = Boolean.parseBoolean(fileRows.get(8));

                    meals.add((T) new VegetarianMeal(id, name, mealType, category, ingredients, new java.math.BigDecimal(price), proteinSource, containsDairy, containsEggs));
                } else {
                    String meatType = fileRows.get(6);
                    String meatOrigin = fileRows.get(7);
                    String meatCookingType = fileRows.get(8);

                    meals.add((T) new MeatMeal(id, name, mealType, category, ingredients, new java.math.BigDecimal(price), meatType, meatOrigin, meatCookingType));
                }

                counter++;
                fileRows = fileRows.subList(9, fileRows.size());
            }
        } catch (IOException e) {
            System.err.println("Greška pri čitanju datoteke: " + e.getMessage());
        }

        return meals;
    }

    @Override
    public void save(Set<T> entity) {
        try(PrintWriter printWriter = new PrintWriter(FILE_PATH)) {
            for(T meal : entity) {
                printWriter.println(meal.getId());
                printWriter.println(meal.getName());
                printWriter.println(meal.getMealType());
                printWriter.println(meal.getCategory().getId());
                printWriter.println(meal.getIngredientsIdentifiers());
                printWriter.println(meal.getPrice());

                if(meal instanceof VeganMeal) {
                    printWriter.println(((VeganMeal) meal).getProteinSource());
                    printWriter.println(((VeganMeal) meal).isOrganic());
                    printWriter.println(((VeganMeal) meal).isGlutenFree());
                } else if(meal instanceof VegetarianMeal) {
                    printWriter.println(((VegetarianMeal) meal).getProteinSource());
                    printWriter.println(((VegetarianMeal) meal).isContainsDairy());
                    printWriter.println(((VegetarianMeal) meal).isContainsEggs());
                } else {
                    printWriter.println(((MeatMeal) meal).getMeatType());
                    printWriter.println(((MeatMeal) meal).getMeatOrigin());
                    printWriter.println(((MeatMeal) meal).getMeatCookingType());
                }
            }

            printWriter.flush();
        } catch (IOException e) {
            System.err.println("Greška pri zapisivanju u datoteku: " + e.getMessage());
        }
    }
}
