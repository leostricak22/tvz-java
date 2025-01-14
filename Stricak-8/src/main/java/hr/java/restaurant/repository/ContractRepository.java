package hr.java.restaurant.repository;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.model.Contract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContractRepository {
    public static List<Contract> contracts = new ArrayList<>();

    public void initializeList() {
        contracts.add(new Contract(BigDecimal.valueOf(1000), LocalDate.of(2021, 1, 1), LocalDate.of(2021, 12, 31), ContractType.FULL_TIME));
        contracts.add(new Contract(BigDecimal.valueOf(800), LocalDate.of(2021, 1, 1), LocalDate.of(2023, 12, 31), ContractType.PART_TIME));
        contracts.add(new Contract(BigDecimal.valueOf(1200), LocalDate.of(2021, 1, 1), LocalDate.of(2022, 12, 31), ContractType.FULL_TIME));
        contracts.add(new Contract(BigDecimal.valueOf(1250), LocalDate.of(2021, 1, 1), LocalDate.of(2024, 12, 31), ContractType.FULL_TIME));
    }

    public List<Contract> getAll() {
        return contracts;
    }

    public void save(Contract contract) {
        contracts.add(contract);
    }
}
