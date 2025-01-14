package hr.java.restaurant.controller;

import hr.java.restaurant.model.Chef;
import hr.java.restaurant.model.Waiter;
import hr.java.restaurant.repository.ChefRepository;
import hr.java.restaurant.repository.WaiterRepository;

import java.util.ArrayList;
import java.util.List;

public class WaiterSearchController extends PersonSearchController<Waiter> {
    private final static WaiterRepository<Waiter> waiterRepository = new WaiterRepository<>();

    @Override
    protected List<Waiter> fetchAllPeople() {
        return new ArrayList<>(waiterRepository.findAll());
    }
}
