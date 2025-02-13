package hr.javafx.coffe.caffee.javafxcaffee.repository;

import hr.javafx.coffe.caffee.javafxcaffee.model.Beverage;
import hr.javafx.coffe.caffee.javafxcaffee.model.CoffeeShop;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CoffeeShopRepository<T extends CoffeeShop> extends AbstractRepository<T> {

    private static final String COFFEE_SHOPS_FILE_PATH = "files/coffeeShops.txt";
    private static final Integer NUMBER_OF_ROWS_PER_COFFEE_SHOP = 7;

    @Override
    public T findById(Long id) {  // potrebno staviti public jer je package-private
        return findAll().stream()
                .filter(coffeeShop -> coffeeShop.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<T> findAll() {
        List<T> coffeShops = new ArrayList<>();
        BeveragesFileRepository<Beverage> beveragesFileRepository = new BeveragesFileRepository<>();

        // istražiti Path.of, Paths.get, Files.lines, try with resources
        try (Stream<String> stream = Files.lines(Path.of(COFFEE_SHOPS_FILE_PATH))) {
            // ovo bi moglo biti na kolokviju
            List<String> fileRows = stream.collect(Collectors.toList());

            for (Integer recordNumber = 0; recordNumber < (fileRows.size() / NUMBER_OF_ROWS_PER_COFFEE_SHOP);recordNumber++) {
                // potreban je parse, jer je u file-u zapisan kao string
                // UTF-8 je default u javi, pa rade hrvatski znakovi (od verzije Java21)
                Long id = Long.parseLong(fileRows.get(
                        recordNumber * NUMBER_OF_ROWS_PER_COFFEE_SHOP));
                String name = fileRows.get(
                        recordNumber * NUMBER_OF_ROWS_PER_COFFEE_SHOP + 1);
                String address = fileRows.get(
                        recordNumber * NUMBER_OF_ROWS_PER_COFFEE_SHOP + 2);
                String phone = fileRows.get(
                        recordNumber * NUMBER_OF_ROWS_PER_COFFEE_SHOP + 3);
                String email = fileRows.get(
                        recordNumber * NUMBER_OF_ROWS_PER_COFFEE_SHOP + 4);
                String owner = fileRows.get(
                        recordNumber * NUMBER_OF_ROWS_PER_COFFEE_SHOP + 5);

                // Dohvat svih pića u kafiću
                String beveragesIds = fileRows.get(recordNumber * NUMBER_OF_ROWS_PER_COFFEE_SHOP + 6);

                // razdvajanje stringa po razmacima, pretvaranje u long, dohvat pića po id-u
                List<Beverage> beverages = Arrays.stream(beveragesIds.split(" "))
                        .map(idString -> Long.parseLong(idString))
                        .map(idLong -> beveragesFileRepository.findById(idLong))
                        .collect(Collectors.toList());

                CoffeeShop coffeShop = new CoffeeShop(id, name, address, phone, email, owner, beverages);

                // castanje u T
                coffeShops.add((T) coffeShop);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return coffeShops;
    }

    @Override
    void save(List<T> entities) {
        // moguće je koristiti i sa new File.of()
        try (PrintWriter writer = new PrintWriter(COFFEE_SHOPS_FILE_PATH)) {
            for (T entity : entities) {
                writer.println(entity.getId());
                writer.println(entity.getName());
                writer.println(entity.getAddress());
                writer.println(entity.getPhone());
                writer.println(entity.getEmail());
                writer.println(entity.getOwner());

                // string je immutable, pa je bolje koristiti StringBuilder jer je mutable
                StringBuilder beveragesIdBuilder = new StringBuilder();
                // mutable znači da se ne stvara nova instanca, nego se mijenja trenutna

                for (Beverage beverage : entity.getPriceList()) {
                    beveragesIdBuilder.append(beverage.getId()).append(" ");
                    // append vraća StringBuilder, pa se može koristiti za dodavanje više stringova
                }

                writer.println(beveragesIdBuilder);
            }

            writer.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(T entity) {

    }
}
