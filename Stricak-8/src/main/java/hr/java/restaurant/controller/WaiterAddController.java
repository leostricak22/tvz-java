package hr.java.restaurant.controller;

import hr.java.restaurant.model.Bonus;
import hr.java.restaurant.model.Contract;
import hr.java.restaurant.model.Waiter;
import hr.java.restaurant.repository.WaiterRepository;
import hr.java.restaurant.util.SceneLoader;

import java.math.BigDecimal;

public class WaiterAddController extends PersonAddController<Waiter> {

    private final WaiterRepository<Waiter> waiterRepository = new WaiterRepository<>();

    @Override
    Waiter addPerson(String firstName, String lastName, Contract contract, BigDecimal bonus) {
        return new Waiter.Builder(waiterRepository.getNextId())
                .setFirstName(firstName)
                .setLastName(lastName)
                .setContract(contract)
                .setBonus(new Bonus(bonus))
                .build();
    }

    @Override
    void savePerson(Waiter person) {
        waiterRepository.add(person);
    }

    @Override
    void loadPersonScene() {
        SceneLoader.loadScene("waiterSearch", "Waiter search");
    }
}
