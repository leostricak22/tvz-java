package hr.java.restaurant.controller;

import hr.java.restaurant.model.Deliverer;
import hr.java.restaurant.model.Waiter;
import hr.java.restaurant.repository.DelivererRepository;
import hr.java.restaurant.repository.WaiterRepository;

import java.util.ArrayList;
import java.util.List;

public class DelivererSearchController extends PersonSearchController<Deliverer> {
    private final static DelivererRepository<Deliverer> delivererRepository = new DelivererRepository<>();

    @Override
    protected List<Deliverer> fetchAllPeople() {
        return new ArrayList<>(delivererRepository.findAll());
    }
}
