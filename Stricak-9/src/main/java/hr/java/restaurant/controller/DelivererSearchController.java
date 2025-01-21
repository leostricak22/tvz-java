package hr.java.restaurant.controller;

import hr.java.restaurant.model.Deliverer;
import hr.java.restaurant.repository.DelivererDatabaseRepository;

import java.util.ArrayList;
import java.util.List;

public class DelivererSearchController extends PersonSearchController<Deliverer> {

    private final DelivererDatabaseRepository delivererRepository = new DelivererDatabaseRepository();

    @Override
    protected List<Deliverer> fetchAllPeople() {
        return new ArrayList<>(delivererRepository.findAll());
    }
}
