package hr.java.restaurant.repository;

import hr.java.restaurant.model.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class WaiterRepository<T extends Waiter> extends AbstractRepository<T> {

    public final static String FILE_PATH = "dat/waiters.txt";
    private final ContractDatabaseRepository contractRepository = new ContractDatabaseRepository();

    @Override
    public T findById(Long id) {
        return findAll().stream()
                .filter(waiter -> waiter.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nepostojeći identifikator"));
    }

    @Override
    public void save(Set<T> entities) {
        try (PrintWriter printWriter = new PrintWriter(FILE_PATH)) {
            for (T waiter : entities) {
                printWriter.println(waiter.getId());
                printWriter.println(waiter.getFirstName());
                printWriter.println(waiter.getLastName());
                printWriter.println(waiter.getContract().getId());
                printWriter.println(waiter.getBonus().amount());
            }

            printWriter.flush();
        } catch (IOException e) {
            System.err.println("Greška pri zapisivanju u datoteku: " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<T> findAll() {
        Set<T> waiters = new HashSet<>();

        try (Stream<String> stream = Files.lines(Path.of(FILE_PATH))) {
            List<String> fileRows = stream.toList();

            while (!fileRows.isEmpty()) {
                Long id = Long.parseLong(fileRows.get(0));
                String firstName = fileRows.get(1);
                String lastName = fileRows.get(2);
                Long contractId = Long.parseLong(fileRows.get(3));
                BigDecimal bonus = new BigDecimal(fileRows.get(4));

                waiters.add((T) new Waiter.Builder(id)
                        .setFirstName(firstName)
                        .setLastName(lastName)
                        .setContract(contractRepository.findById(contractId))
                        .setBonus(new Bonus(bonus))
                        .build());

                fileRows = fileRows.subList(5, fileRows.size());
            }
        } catch (IOException e) {
            System.err.println("Greška pri čitanju datoteke: " + e.getMessage());
        }

        return waiters;
    }

    @Override
    public Long findNextId() {
        return findAll().stream()
                .map(Waiter::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }

    @Override
    public void save(T waiter) {
        Set<T> waiters = findAll();
        waiters.add(waiter);
        save(waiters);
    }
}
