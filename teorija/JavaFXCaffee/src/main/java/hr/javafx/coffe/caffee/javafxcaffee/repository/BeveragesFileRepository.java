package hr.javafx.coffe.caffee.javafxcaffee.repository;

import hr.javafx.coffe.caffee.javafxcaffee.model.Beverage;
import hr.javafx.coffe.caffee.javafxcaffee.model.Origin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeveragesFileRepository<T extends Beverage> extends AbstractRepository<T> {

    private static final String BEVERAGES_FILE_PATH = "dat/beverages.txt";
    private static final Integer NUMBER_OF_ROWS_PER_BEVERAGE = 5;

    @Override
    public T findById(Long id) {  // potrebno staviti public jer je package-private
        return findAll().stream()
                .filter(beverage -> beverage.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<T> findAll() {  // potrebno staviti public jer je package-private
        List<T> beverages = new ArrayList<>();

        // istražiti Path.of, Paths.get, Files.lines, try with resources
        try (Stream<String> stream = Files.lines(Path.of(BEVERAGES_FILE_PATH))) {
            // ovo bi moglo biti na kolokviju
            List<String> fileRows = stream.collect(Collectors.toList());

            for (Integer recordNumber = 0; recordNumber < (fileRows.size() / NUMBER_OF_ROWS_PER_BEVERAGE);recordNumber++) {
                // potreban je parse, jer je u file-u zapisan kao string
                // UTF-8 je default u javi, pa rade hrvatski znakovi (od verzije Java21)
                Long id = Long.parseLong(fileRows.get(
                        recordNumber * NUMBER_OF_ROWS_PER_BEVERAGE));
                String name = fileRows.get(
                        recordNumber * NUMBER_OF_ROWS_PER_BEVERAGE + 1);
                BigDecimal price = BigDecimal.valueOf(
                        Float.parseFloat(fileRows.get(recordNumber * NUMBER_OF_ROWS_PER_BEVERAGE + 2)));
                BigDecimal alcoholPercentage = BigDecimal.valueOf(
                        Float.parseFloat(fileRows.get(recordNumber * NUMBER_OF_ROWS_PER_BEVERAGE + 3)));
                Origin origin = Origin.valueOf(fileRows.get(recordNumber * NUMBER_OF_ROWS_PER_BEVERAGE + 4));

                Beverage beverage = new Beverage(id, name, price, alcoholPercentage, origin);

                // castanje u T
                beverages.add((T) beverage);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return beverages;
    }

    @Override
    public void save(List<T> entities) { // potrebno staviti public jer je package-private
        // moguće je koristiti i sa new File.of()
        try (PrintWriter writer = new PrintWriter(BEVERAGES_FILE_PATH)) {
            for (T entity : entities) {
                writer.println(entity.getId());
                writer.println(entity.getName());
                writer.println(entity.getPrice());
                writer.println(entity.calculatePercentage());
                writer.println(entity.getOrigin());
            }

            writer.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(T entity) { // potrebno staviti public jer je package-private
        List<T> entities = findAll();
        if (Optional.ofNullable(entity.getId()).isEmpty()) {
            entity.setId(generateNewId());
        }
        entities.add(entity);
        save(entities);
    }

    private Long generateNewId() {
        return findAll().stream()
                .map(b -> b.getId())
                .max((i1, i2) -> i1.compareTo(i2)) // traži se zadnji (najveći) id
                .orElse(0L) + 1; // ako nema id-eva, vraća 0 (+1 jer je id početni 1)
    }
}
