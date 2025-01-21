package hr.java.restaurant.controller;

import hr.java.restaurant.model.Bonus;
import hr.java.restaurant.model.Contract;
import hr.java.restaurant.model.Deliverer;
import hr.java.restaurant.repository.DelivererDatabaseRepository;
import hr.java.restaurant.util.SceneLoader;

public class DelivererAddController extends PersonAddController<Deliverer> {

    private final DelivererDatabaseRepository delivererRepository = new DelivererDatabaseRepository();

    @Override
    Deliverer addPerson(String firstName, String lastName, Contract contract, Bonus bonus) {
        return new Deliverer.Builder(delivererRepository.findNextId())
                .setFirstName(firstName)
                .setLastName(lastName)
                .setContract(contract)
                .setBonus(bonus)
                .build();
    }

    @Override
    void savePerson(Deliverer person) {
        delivererRepository.save(person);
    }

    @Override
    void loadPersonScene() {
        SceneLoader.loadScene("delivererSearch", "Deliverer search");
    }
}
