package hr.java.restaurant.repository;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.model.Bonus;
import hr.java.restaurant.model.Category;
import hr.java.restaurant.model.Chef;
import hr.java.restaurant.model.Contract;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class ChefRepository<T extends Chef> extends AbstractRepository<T> {
    public final static String FILE_PATH = "dat/chefs.txt";

    @Override
    public T findById(Long id) {
        return findAll().stream()
                .filter(chef -> chef.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nepostojeći identifikator"));
    }

    @Override
    public void save(Set<T> entities) {
        try (PrintWriter printWriter = new PrintWriter(FILE_PATH)) {
            for (T chef : entities) {
                printWriter.println(chef.getId());
                printWriter.println(chef.getFirstName());
                printWriter.println(chef.getLastName());
                printWriter.println(chef.getContract().getSalary());
                printWriter.println(chef.getContract().getStartDate());
                printWriter.println(chef.getContract().getEndDate());
                printWriter.println(chef.getContract().getContractType());
                printWriter.println(chef.getBonus().amount());
            }

            printWriter.flush();
        } catch (IOException e) {
            System.err.println("Greška pri zapisivanju u datoteku: " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<T> findAll() {
        Set<T> chefs = new HashSet<>();

        try (Stream<String> stream = Files.lines(Path.of(FILE_PATH))) {
            List<String> fileRows = stream.toList();

            while (!fileRows.isEmpty()) {
                Long id = Long.parseLong(fileRows.get(0));
                String firstName = fileRows.get(1);
                String lastName = fileRows.get(2);
                BigDecimal salary = new BigDecimal(fileRows.get(3));
                LocalDate contractStartDate = LocalDate.parse(fileRows.get(4));
                LocalDate contractEndDate = LocalDate.parse(fileRows.get(5));
                String contractType = fileRows.get(6);
                BigDecimal bonus = new BigDecimal(fileRows.get(7));

                chefs.add((T) new Chef.Builder()
                        .id(id)
                        .firstName(firstName)
                        .lastName(lastName)
                        .contract(new Contract(salary, contractStartDate, contractEndDate, ContractType.valueOf(contractType)))
                        .bonus(new Bonus(bonus))
                        .build());

                fileRows = fileRows.subList(8, fileRows.size());
            }
        } catch (IOException e) {
            System.err.println("Greška pri čitanju datoteke: " + e.getMessage());
        }

        return chefs;
    }
}
