package hr.java.restaurant.repository;

import hr.java.restaurant.model.Category;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class CategoryRepository<T extends Category> extends AbstractRepository<T> {
    public final static String FILE_PATH = "dat/categories.txt";

    @Override
    public T findById(Long id) {
        return findAll().stream()
                .filter(category -> category.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nepostojeći identifikator"));
    }

    @Override
    public void save(Set<T> entities) {
        try(PrintWriter printWriter = new PrintWriter(FILE_PATH)) {
            for(T entity : entities) {
                printWriter.println(entity.getId());
                printWriter.println(entity.getName());
                printWriter.println(entity.getDescription());
            }

            printWriter.flush();
        } catch (IOException e) {
            System.err.println("Greška pri zapisivanju u datoteku: " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<T> findAll() {
        Set<T> categories = new HashSet<>();

        try (Stream<String> stream = Files.lines(Path.of(FILE_PATH))) {
            List<String> fileRows = stream.toList();

            while (!fileRows.isEmpty()) {
                Long id = Long.parseLong(fileRows.get(0));
                String name = fileRows.get(1);
                String description = fileRows.get(2);

                categories.add((T) new Category.Builder()
                        .id(id)
                        .name(name)
                        .description(description)
                        .build());

                fileRows = fileRows.subList(3, fileRows.size());
            }
        } catch (IOException e) {
            System.err.println("Greška pri čitanju datoteke: " + e.getMessage());
        }

        return categories;
    }
}
