package hr.java.restaurant.thread;

import hr.java.restaurant.main.RestaurantApplication;
import hr.java.restaurant.model.Chef;
import hr.java.restaurant.model.Deliverer;
import hr.java.restaurant.model.Person;
import hr.java.restaurant.model.Waiter;
import hr.java.restaurant.repository.ChefRepository;
import hr.java.restaurant.repository.DelivererRepository;
import hr.java.restaurant.repository.WaiterRepository;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.util.Optional;

public class HighestPaidPersonThread {

    private static final ChefRepository chefRepository = new ChefRepository();
    private static final DelivererRepository delivererRepository = new DelivererRepository();
    private static final WaiterRepository waiterRepository = new WaiterRepository();

    public static void start() {
        Timeline highestPersonTimeLine = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    Task<Optional<Person>> task = new Task<>() {
                        @Override
                        protected Optional<Person> call() {
                            Optional<Chef> chef = chefRepository.findHighestPaidChef();
                            Optional<Deliverer> deliverer = delivererRepository.findHighestPaidDeliverer();
                            Optional<Waiter> waiter = waiterRepository.findHighestPaidWaiter();

                            BigDecimal highestSalary = BigDecimal.ZERO;
                            Optional<Person> highestPaidPerson = Optional.empty();

                            if (chef.isPresent() &&
                                    (chef.get().getContract().getSalary().add(chef.get().getBonus().amount()))
                                            .compareTo(highestSalary) > 0) {
                                highestSalary = chef.get().getContract().getSalary().add(chef.get().getBonus().amount());
                                highestPaidPerson = Optional.of(chef.get());
                            }

                            if (waiter.isPresent() &&
                                    (waiter.get().getContract().getSalary().add(waiter.get().getBonus().amount()))
                                            .compareTo(highestSalary) > 0) {
                                highestSalary = waiter.get().getContract().getSalary().add(waiter.get().getBonus().amount());
                                highestPaidPerson = Optional.of(waiter.get());
                            }

                            if (deliverer.isPresent() &&
                                    (deliverer.get().getContract().getSalary().add(deliverer.get().getBonus().amount()))
                                            .compareTo(highestSalary) > 0) {
                                highestSalary = deliverer.get().getContract().getSalary().add(deliverer.get().getBonus().amount());
                                highestPaidPerson = Optional.of(deliverer.get());
                            }

                            return highestPaidPerson;
                        }
                    };

                    task.setOnSucceeded(event -> {
                        Optional<Person> highestPaidPerson = task.getValue();
                        highestPaidPerson.ifPresent(person -> {
                            System.out.println("Highest Paid Person: " + person.getName());
                            RestaurantApplication.getMainStage().setTitle("Highest Paid Person: " + person.getName());
                        });
                    });

                    task.setOnFailed(event -> {
                        System.err.println("Failed to compute highest-paid person: " + task.getException());
                    });

                    new Thread(task).start();
                })
        );

        highestPersonTimeLine.setCycleCount(Timeline.INDEFINITE);
        highestPersonTimeLine.play();
    }
}
