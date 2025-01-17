package hr.java.restaurant.controller;

import hr.java.restaurant.model.Bonus;
import hr.java.restaurant.model.Contract;
import hr.java.restaurant.model.Deliverer;
import hr.java.restaurant.repository.DelivererRepository;
import hr.java.restaurant.util.SceneLoader;

import java.math.BigDecimal;

public class DelivererAddController extends PersonAddController<Deliverer> {

    private final DelivererRepository<Deliverer> delivererRepository = new DelivererRepository<>();

    @Override
    Deliverer addPerson(String firstName, String lastName, Contract contract, Bonus bonus) {
        return new Deliverer.Builder(delivererRepository.getNextId())
                .setFirstName(firstName)
                .setLastName(lastName)
                .setContract(contract)
                .setBonus(bonus)
                .build();
    }

    @Override
    void savePerson(Deliverer person) {
        delivererRepository.add(person);
    }

    @Override
    void loadPersonScene() {
        SceneLoader.loadScene("delivererSearch", "Deliverer search");
    }
}
