package hr.java.restaurant.controller;

import hr.java.restaurant.model.Waiter;
import hr.java.restaurant.repository.WaiterRepository;

import java.util.ArrayList;
import java.util.List;

public class WaiterSearchController extends PersonSearchController<Waiter> {

    private final WaiterRepository waiterRepository = new WaiterRepository();

    @Override
    protected List<Waiter> fetchAllPeople() {
        return new ArrayList<>(waiterRepository.findAll());
    }
}
