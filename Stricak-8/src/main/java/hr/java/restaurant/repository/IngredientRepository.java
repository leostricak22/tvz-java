package hr.java.restaurant.repository;

import hr.java.restaurant.model.Category;
import hr.java.restaurant.model.Ingredient;
import hr.java.restaurant.util.EntityFinder;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class IngredientRepository<T extends Ingredient> extends AbstractRepository<T> {
    public final static String FILE_PATH = "dat/ingredients.txt";

    public final CategoryRepository<Category> categoryRepository = new CategoryRepository<>();

    @Override
    public T findById(Long id) {
        return findAll().stream()
                .filter(ingredient -> ingredient.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nepostojeći identifikator"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<T> findAll() {
        Set<T> ingredients = new HashSet<>();

        try (Stream<String> stream = Files.lines(Path.of(FILE_PATH))) {
            List<String> fileRows = stream.toList();

            while (!fileRows.isEmpty()) {
                Long id = Long.parseLong(fileRows.get(0));
                String name = fileRows.get(1);
                Long categoryId = Long.parseLong(fileRows.get(2));
                BigDecimal kcal = new BigDecimal(fileRows.get(3));
                String preparationMethod = fileRows.get(4);

                Category category = EntityFinder.categoryById(categoryId, categoryRepository.findAll());

                ingredients.add((T) new Ingredient(id, name, category, kcal, preparationMethod));

                fileRows = fileRows.subList(5, fileRows.size());
            }
        } catch (IOException e) {
            System.err.println("Greška pri čitanju datoteke: " + e.getMessage());
        }

        return ingredients;
    }

    @Override
    public void save(Set<T> entity) {
        try (PrintWriter printWriter = new PrintWriter(FILE_PATH)) {
            for(T ingredient : entity) {
                printWriter.println(ingredient.getId());
                printWriter.println(ingredient.getName());
                printWriter.println(ingredient.getCategory().getId());
                printWriter.println(ingredient.getKcal());
                printWriter.println(ingredient.getPreparationMethod());
            }
        } catch (IOException e) {
            System.err.println("Greška pri zapisivanju u datoteku: " + e.getMessage());
        }
    }
}
