package hr.java.restaurant.repository;

import hr.java.restaurant.enumeration.MealTypeEnum;
import hr.java.restaurant.model.*;
import hr.java.restaurant.util.EntityFinder;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class MealRepository<T extends Meal> extends AbstractRepository<T> {
    public final static String FILE_PATH = "dat/meals.txt";

    public final CategoryDatabaseRepository categoryRepository = new CategoryDatabaseRepository();

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
                MealTypeEnum mealType = MealTypeEnum.valueOf(fileRows.get(2));
                Long categoryId = Long.parseLong(fileRows.get(3));
                String ingredientsIdentifiers = fileRows.get(4);
                String price = fileRows.get(5);

                Category category = categoryRepository.findById(categoryId);
                Set<Ingredient> ingredients = EntityFinder.ingredientsByIdentifiers(ingredientsIdentifiers);

                if(mealType == MealTypeEnum.VEGAN) {
                    String proteinSource = fileRows.get(6);
                    boolean organic = Boolean.parseBoolean(fileRows.get(7));
                    boolean glutenFree = Boolean.parseBoolean(fileRows.get(8));

                    meals.add((T) new VeganMeal.Builder(id, name)
                            .setMealType(mealType)
                            .setCategory(category)
                            .setIngredients(ingredients)
                            .setPrice(new java.math.BigDecimal(price))
                            .setProteinSource(proteinSource)
                            .setOrganic(organic)
                            .setGlutenFree(glutenFree)
                            .build());
                } else if(mealType == MealTypeEnum.VEGETARIAN) {
                    String proteinSource = fileRows.get(6);
                    boolean containsDairy = Boolean.parseBoolean(fileRows.get(7));
                    boolean containsEggs = Boolean.parseBoolean(fileRows.get(8));

                    meals.add((T) new VegetarianMeal.Builder(id, name)
                            .setMealType(mealType)
                            .setCategory(category)
                            .setIngredients(ingredients)
                            .setPrice(new java.math.BigDecimal(price))
                            .setProteinSource(proteinSource)
                            .setContainsDairy(containsDairy)
                            .setContainsEggs(containsEggs)
                            .build());
                } else {
                    String meatType = fileRows.get(6);
                    boolean meatOrganic = Boolean.parseBoolean(fileRows.get(7));
                    boolean meatEcoFriendly = Boolean.parseBoolean(fileRows.get(8));

                    meals.add((T) new MeatMeal.Builder(id, name)
                            .setMealType(mealType)
                            .setCategory(category)
                            .setIngredients(ingredients)
                            .setPrice(new java.math.BigDecimal(price))
                            .setMeatType(meatType)
                            .setEcoFriendly(meatEcoFriendly)
                            .setOrganic(meatOrganic)
                            .build());
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
                printWriter.println(EntityFinder.getIngredientsIdentifiers(new ArrayList<>(meal.getIngredients())));
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
                    printWriter.println(((MeatMeal) meal).isEcoFriendly());
                    printWriter.println(((MeatMeal) meal).isOrganic());
                }
            }

            printWriter.flush();
        } catch (IOException e) {
            System.err.println("Greška pri zapisivanju u datoteku: " + e.getMessage());
        }
    }

    @Override
    public Long findNextId() {
        return findAll().stream()
                .map(Meal::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }

    @Override
    public void save(T entity) {
        Set<T> meals = findAll();
        meals.add(entity);
        save(meals);
    }
}
