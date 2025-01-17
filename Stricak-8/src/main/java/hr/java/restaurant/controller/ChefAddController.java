package hr.java.restaurant.controller;

import hr.java.restaurant.model.Bonus;
import hr.java.restaurant.model.Chef;
import hr.java.restaurant.model.Contract;
import hr.java.restaurant.repository.ChefRepository;
import hr.java.restaurant.util.SceneLoader;

import java.math.BigDecimal;

public class ChefAddController extends PersonAddController<Chef> {

    private final ChefRepository<Chef> chefRepository = new ChefRepository<>();

    @Override
    Chef addPerson(String firstName, String lastName, Contract contract, BigDecimal bonus) {
        return new Chef.Builder(chefRepository.getNextId())
                .setFirstName(firstName)
                .setLastName(lastName)
                .setContract(contract)
                .setBonus(new Bonus(bonus))
                .build();
    }

    @Override
    void savePerson(Chef person) {
        chefRepository.add(person);
    }

    @Override
    void loadPersonScene() {
        SceneLoader.loadScene("chefSearch", "Chef search");
    }
}
