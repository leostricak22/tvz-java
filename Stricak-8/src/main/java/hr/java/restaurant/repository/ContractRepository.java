package hr.java.restaurant.repository;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.model.Contract;
import hr.java.restaurant.model.Ingredient;
import hr.java.restaurant.model.Restaurant;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class ContractRepository extends AbstractRepository<Contract> {

    private final static String FILE_PATH = "dat/contracts.txt";

    @Override
    public Contract findById(Long id) {
        return findAll().stream()
                .filter(contract -> contract.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nepostojeći identifikator"));
    }

    @Override
    public Set<Contract> findAll() {
        Set<Contract> contracts = new HashSet<>();

        try (Stream<String> stream = Files.lines(Path.of(FILE_PATH))) {
            List<String> fileRows = stream.toList();

            while (!fileRows.isEmpty()) {
                Long id = Long.parseLong(fileRows.get(0));
                String name = fileRows.get(1);
                BigDecimal salary = new BigDecimal(fileRows.get(2));
                LocalDate startDate = LocalDate.parse(fileRows.get(3));
                LocalDate endDate = LocalDate.parse(fileRows.get(4));
                ContractType contractType = ContractType.valueOf(fileRows.get(5));

                contracts.add(new Contract(id, name, salary, startDate, endDate, contractType));

                fileRows = fileRows.subList(6, fileRows.size());
            }
        } catch (Exception e) {
            System.err.println("Greška pri čitanju datoteke: " + e.getMessage());
        }

        return contracts;
    }

    @Override
    public void save(Set<Contract> entity) {
        List<String> fileRows = new ArrayList<>();

        for (Contract contract : entity) {
            fileRows.add(contract.getId().toString());
            fileRows.add(contract.getName());
            fileRows.add(contract.getSalary().toString());
            fileRows.add(contract.getStartDate().toString());
            fileRows.add(contract.getEndDate().toString());
            fileRows.add(contract.getContractType().toString());
        }

        try {
            Files.write(Path.of(FILE_PATH), fileRows);
        } catch (Exception e) {
            System.err.println("Greška pri zapisivanju u datoteku: " + e.getMessage());
        }
    }

    public Long getNextId() {
        return findAll().stream()
                .map(Contract::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }

    public void add(Contract contract) {
        Set<Contract> contracts = findAll();
        contracts.add(contract);
        save(contracts);
    }
}
