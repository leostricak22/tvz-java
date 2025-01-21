package hr.java.restaurant.controller;

import hr.java.restaurant.model.Bonus;
import hr.java.restaurant.model.Chef;
import hr.java.restaurant.model.Contract;
import hr.java.restaurant.repository.ChefDatabaseRepository;
import hr.java.restaurant.util.SceneLoader;

public class ChefAddController extends PersonAddController<Chef> {

    private final ChefDatabaseRepository chefRepository = new ChefDatabaseRepository();

    @Override
    Chef addPerson(String firstName, String lastName, Contract contract, Bonus bonus) {
        return new Chef.Builder(chefRepository.findNextId())
                .setFirstName(firstName)
                .setLastName(lastName)
                .setContract(contract)
                .setBonus(bonus)
                .build();
    }

    @Override
    void savePerson(Chef person) {
        chefRepository.save(person);
    }

    @Override
    void loadPersonScene() {
        SceneLoader.loadScene("chefSearch", "Chef search");
    }
}
