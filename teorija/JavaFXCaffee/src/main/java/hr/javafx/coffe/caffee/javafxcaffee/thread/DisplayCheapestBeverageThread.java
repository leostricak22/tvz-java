package hr.javafx.coffe.caffee.javafxcaffee.thread;

import hr.javafx.coffe.caffee.javafxcaffee.model.Beverage;
import hr.javafx.coffe.caffee.javafxcaffee.repository.AbstractRepository;
import hr.javafx.coffe.caffee.javafxcaffee.repository.BeveragesDatabaseRepository;
import javafx.scene.control.Label;

import java.util.Optional;

// niti trebaju implementirati Runnable
public class DisplayCheapestBeverageThread implements Runnable {

    private AbstractRepository<Beverage> repository;
    private Label cheapestBeverageLabel;

    public DisplayCheapestBeverageThread(
            AbstractRepository<Beverage> repository,
            Label cheapestBeverageLabel) {
        this.repository = repository;
        this.cheapestBeverageLabel = cheapestBeverageLabel;
    }

    // run metoda se izvršava kada se pokrene nit
    @Override
    public void run() {
        Optional<Beverage> cheapestBeverage = Optional.empty();

        if (repository instanceof BeveragesDatabaseRepository bdr) {
            cheapestBeverage = bdr.findCheapestBeverage();
        }

        if (cheapestBeverage.isPresent()) {
            cheapestBeverageLabel.setText(
                    "Najjeftinije piće je: " + cheapestBeverage.get().getName() + " po cijeni " + cheapestBeverage.get().getPrice());
        }
    }
}
