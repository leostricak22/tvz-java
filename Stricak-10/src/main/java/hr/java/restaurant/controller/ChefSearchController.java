package hr.java.restaurant.controller;

import hr.java.restaurant.model.Chef;
import hr.java.restaurant.repository.ChefRepository;

import java.util.ArrayList;
import java.util.List;

public class ChefSearchController extends PersonSearchController<Chef> {
    private final ChefRepository chefRepository = new ChefRepository();

    @Override
    protected List<Chef> fetchAllPeople() {
        return new ArrayList<>(chefRepository.findAll());
    }
}
