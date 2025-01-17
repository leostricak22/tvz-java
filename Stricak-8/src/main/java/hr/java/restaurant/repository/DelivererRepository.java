package hr.java.restaurant.repository;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.model.*;

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

public class DelivererRepository<T extends Deliverer> extends AbstractRepository<T> {
    public final static String FILE_PATH = "dat/deliverers.txt";

    @Override
    public T findById(Long id) {
        return findAll().stream()
                .filter(deliverer -> deliverer.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nepostojeći identifikator"));
    }

    @Override
    public void save(Set<T> entities) {
        try (PrintWriter printWriter = new PrintWriter(FILE_PATH)) {
            for (T deliverer : entities) {
                printWriter.println(deliverer.getId());
                printWriter.println(deliverer.getFirstName());
                printWriter.println(deliverer.getLastName());
                printWriter.println(deliverer.getContract().getSalary());
                printWriter.println(deliverer.getContract().getStartDate());
                printWriter.println(deliverer.getContract().getEndDate());
                printWriter.println(deliverer.getContract().getContractType());
                printWriter.println(deliverer.getBonus().amount());
            }

            printWriter.flush();
        } catch (IOException e) {
            System.err.println("Greška pri zapisivanju u datoteku: " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<T> findAll() {
        Set<T> deliverers = new HashSet<>();

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

                deliverers.add((T) new Deliverer.Builder(id)
                        .setFirstName(firstName)
                        .setLastName(lastName)
                        .setContract(new Contract(salary, contractStartDate, contractEndDate, ContractType.valueOf(contractType)))
                        .setBonus(new Bonus(bonus))
                        .build());

                fileRows = fileRows.subList(8, fileRows.size());
            }
        } catch (IOException e) {
            System.err.println("Greška pri čitanju datoteke: " + e.getMessage());
        }

        return deliverers;
    }

    public Long getNextId() {
        return findAll().stream()
                .map(Deliverer::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }

    public void add(T deliverer) {
        Set<T> deliverers = findAll();
        deliverers.add(deliverer);
        save(deliverers);
    }
}
