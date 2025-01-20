package hr.java.restaurant.controller;

import hr.java.restaurant.model.Bonus;
import hr.java.restaurant.model.Contract;
import hr.java.restaurant.model.Waiter;
import hr.java.restaurant.repository.WaiterRepository;
import hr.java.restaurant.util.SceneLoader;

public class WaiterAddController extends PersonAddController<Waiter> {

    private final WaiterRepository<Waiter> waiterRepository = new WaiterRepository<>();

    @Override
    Waiter addPerson(String firstName, String lastName, Contract contract, Bonus bonus) {
        return new Waiter.Builder(waiterRepository.findNextId())
                .setFirstName(firstName)
                .setLastName(lastName)
                .setContract(contract)
                .setBonus(bonus)
                .build();
    }

    @Override
    void savePerson(Waiter person) {
        waiterRepository.save(person);
    }

    @Override
    void loadPersonScene() {
        SceneLoader.loadScene("waiterSearch", "Waiter search");
    }
}
