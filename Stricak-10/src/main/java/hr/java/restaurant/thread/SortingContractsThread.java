package hr.java.restaurant.thread;

import hr.java.restaurant.model.Contract;
import hr.java.restaurant.repository.ContractRepository;

import java.util.*;

public class SortingContractsThread implements Runnable {

    private final ContractRepository contractRepository;
    private final List<Contract> contracts;

    public SortingContractsThread(ContractRepository contractRepository, List<Contract> contracts) {
        this.contractRepository = contractRepository;
        this.contracts = contracts;
    }

    @Override
    public void run() {
        List<Contract> sortedContracts = contractRepository.findAllSortedBySalary();
        contracts.clear();
        contracts.addAll(sortedContracts);
    }
}
