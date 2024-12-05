package hr.java.production.main;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.generics.RestaurantLabourExchangeOffice;
import hr.java.restaurant.model.*;
import hr.java.service.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the main class.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting the application");

        Scanner scanner = new Scanner(System.in);

        //List<Category> categories = Category.inputCategoryList(Constants.NUM_OF_CATEGORIES, scanner);
        List<Category> categories = new ArrayList<>();
        categories.add(new Category.Builder().name("Juha").description("Topla i kremasta jela od povrća ili mesa.").build());
        categories.add(new Category.Builder().name("Salata").description("Svježe i zdrave miješane salate.").build());
        categories.add(new Category.Builder().name("Tjestenina").description("Talijanska tjestenina s različitim umacima.").build());

        //Set<Ingredient> ingredients = Ingredient.inputIngredientSet(Constants.NUM_OF_INGREDIENTS, categories, scanner);
        Set<Ingredient> ingredients = new HashSet<>();
        ingredients.add(new Ingredient("Mrkva", categories.getFirst(), BigDecimal.valueOf(41), "Kuhano"));
        ingredients.add(new Ingredient("Rajčica", categories.get(1), BigDecimal.valueOf(18.5), "Svježe"));
        ingredients.add(new Ingredient("Špageti", categories.get(2), BigDecimal.valueOf(158), "Kuhano"));
        ingredients.add(new Ingredient("Krumpir", categories.getFirst(), BigDecimal.valueOf(77), "Pečeno"));
        ingredients.add(new Ingredient("Krastavac", categories.get(1), BigDecimal.valueOf(16), "Svježe"));

        //Set<Meal> meals = Meal.inputMealSet(categories, ingredients, scanner);
        Set<Meal> meals = new HashSet<>();
        meals.add(new VeganMeal("Juha od mrkve", categories.getFirst(), Set.of(ingredients.stream().toList().get(0), ingredients.stream().toList().get(3)), BigDecimal.valueOf(1500), "soja", true, false));
        meals.add(new VeganMeal("Špagete bolonjez", categories.get(2), Set.of(ingredients.stream().toList().get(2), ingredients.stream().toList().get(1)), BigDecimal.valueOf(20), "soja", true, true));
        meals.add(new VeganMeal("Salata od krastavca", categories.get(1), Set.of(ingredients.stream().toList().get(4)), BigDecimal.valueOf(18), "tofu", true, true));
        meals.add(new VegetarianMeal("Juha od koprive", categories.getFirst(), Set.of(ingredients.stream().toList().get(0), ingredients.stream().toList().get(3)), BigDecimal.valueOf(15), "tofu", false, false));
        meals.add(new VegetarianMeal("Špagete karbonare", categories.get(2), Set.of(ingredients.stream().toList().get(2)), BigDecimal.valueOf(20), "soja", false, false));
        meals.add(new VegetarianMeal("Zelena salata", categories.get(1), Set.of(ingredients.stream().toList().get(4)), BigDecimal.valueOf(18), "tofu", false, true));
        meals.add(new MeatMeal("Juha sa piletinom", categories.getFirst(), Set.of(ingredients.stream().toList().get(0), ingredients.stream().toList().get(3), ingredients.stream().toList().get(4)), BigDecimal.valueOf(15), "Piletina", "Kokoš", "Pečeno"));
        meals.add(new MeatMeal("Pužići sa saftom", categories.get(2), Set.of(ingredients.stream().toList().get(2)), BigDecimal.valueOf(20), "Govedina", "Krava", "kuhano"));
        meals.add(new MeatMeal("Zelena salata s mesom", categories.get(1), Set.of(ingredients.stream().toList().get(4)), BigDecimal.valueOf(18), "Piletina", "Kokoš", "Pečeno"));

        //Set<Chef> chefs = Chef.inputChefSet(Constants.NUM_OF_CHEFS, people, scanner);
        Set<Chef> chefs = new HashSet<>();
        chefs.add(new Chef.Builder().firstName("Nikola").lastName("Jambrešić").contract(new Contract(BigDecimal.valueOf(1200), LocalDate.parse("2023-10-12"), LocalDate.parse("2023-12-12"), ContractType.FULL_TIME)).bonus(new Bonus(BigDecimal.valueOf(0))).build());
        chefs.add(new Chef.Builder().firstName("Luka").lastName("Vincelj").contract(new Contract(BigDecimal.valueOf(1300), LocalDate.parse("2023-11-12"), LocalDate.parse("2023-12-12"), ContractType.PART_TIME)).bonus(new Bonus(BigDecimal.valueOf(100))).build());
        chefs.add(new Chef.Builder().firstName("Mateo").lastName("Spevec").contract(new Contract(BigDecimal.valueOf(1400), LocalDate.parse("2023-12-12"), LocalDate.parse("2025-12-12"), ContractType.FULL_TIME)).bonus(new Bonus(BigDecimal.valueOf(0))).build());

        Set<Person> people = new HashSet<>(chefs);

        //Set<Waiter> waiters = Waiter.inputWaiterSet(Constants.NUM_OF_WAITERS, people, scanner);
        Set<Waiter> waiters = new HashSet<>();
        waiters.add(new Waiter.Builder().firstName("Julia").lastName("Janković").contract(new Contract(BigDecimal.valueOf(1500), LocalDate.parse("2023-12-12"), LocalDate.parse("2025-12-12"), ContractType.PART_TIME)).bonus(new Bonus(BigDecimal.valueOf(500))).build());
        waiters.add(new Waiter.Builder().firstName("Matija").lastName("Trstenjak").contract(new Contract(BigDecimal.valueOf(1400), LocalDate.parse("2022-12-12"), LocalDate.parse("2025-12-12"), ContractType.FULL_TIME)).bonus(new Bonus(BigDecimal.valueOf(0))).build());
        waiters.add(new Waiter.Builder().firstName("Karlo").lastName("Puklek").contract(new Contract(BigDecimal.valueOf(1300), LocalDate.parse("2024-01-01"), LocalDate.parse("2025-12-12"), ContractType.PART_TIME)).bonus(new Bonus(BigDecimal.valueOf(0))).build());
        people.addAll(waiters);

        //Set<Deliverer> deliverers = Deliverer.inputDeliverer(Constants.NUM_OF_DELIVERERS, people, scanner);
        Set<Deliverer> deliverers = new HashSet<>();
        deliverers.add(new Deliverer.Builder().firstName("Natali").lastName("Kovačić").contract(new Contract(BigDecimal.valueOf(1200), LocalDate.parse("2022-12-12"), LocalDate.parse("2025-12-12"), ContractType.PART_TIME)).bonus(new Bonus(BigDecimal.valueOf(0))).build());
        deliverers.add(new Deliverer.Builder().firstName("Karlo").lastName("Kontrec").contract(new Contract(BigDecimal.valueOf(1300), LocalDate.parse("2023-12-12"), LocalDate.parse("2025-12-12"), ContractType.FULL_TIME)).bonus(new Bonus(BigDecimal.valueOf(0))).build());
        deliverers.add(new Deliverer.Builder().firstName("Matija").lastName("Puklek").contract(new Contract(BigDecimal.valueOf(500), LocalDate.parse("2022-12-12"), LocalDate.parse("2023-12-12"), ContractType.FULL_TIME)).bonus(new Bonus(BigDecimal.valueOf(50))).build());

        people.addAll(deliverers);

        //List<Restaurant> restaurants = Restaurant.inputRestaurantList(Constants.NUM_OF_RESTAURANTS, meals, chefs, waiters, deliverers, scanner);
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(new Restaurant(
                "McDonalds",
                new Address.Builder().street("Jarunska ulica").houseNumber("2").city("Zagreb").postalCode("10000").build(),
                meals.stream().filter(meal -> Set.of("Juha od mrkve", "Salata od krastavaca", "Špagete karbonare", "Zelena salata s mesom").contains(meal.getName())).collect(Collectors.toSet()),
                chefs.stream().filter(chef -> Set.of("Nikola Jambrešić", "Luka Vincelj").contains(chef.getFirstName() + " " + chef.getLastName())).collect(Collectors.toSet()),
                waiters.stream().filter(waiter -> Set.of("Julia Janković", "Matija Trstenjak").contains(waiter.getFirstName() + " " + waiter.getLastName())).collect(Collectors.toSet()),
                deliverers.stream().filter(deliverer -> Set.of("Matija Puklek", "Natali Kovačić", "Karlo Kontrec").contains(deliverer.getFirstName() + " " + deliverer.getLastName())).collect(Collectors.toSet())));

        restaurants.add(new Restaurant(
                "Arka",
                new Address.Builder().street("Ulica Ivana Plemenitog Zajca").houseNumber("13").city("Čakovec").postalCode("40000").build(),
                meals.stream().filter(meal -> Objects.equals("Špagete bolonjez", meal.getName())).collect(Collectors.toSet()),
                chefs.stream().filter(chef -> Set.of("Nikola Jambrešić").contains(chef.getFirstName() + " " + chef.getLastName())).collect(Collectors.toSet()),
                waiters.stream().filter(waiter -> Set.of("Karlo Puklek", "Matija Trstenjak").contains(waiter.getFirstName() + " " + waiter.getLastName())).collect(Collectors.toSet()),
                deliverers.stream().filter(deliverer -> Set.of("Matija Puklek", "Karlo Kontrec").contains(deliverer.getFirstName() + " " + deliverer.getLastName())).collect(Collectors.toSet())));

        restaurants.add(new Restaurant(
                "Laganini",
                new Address.Builder().street("Športska ulica").houseNumber("8").city("Čakovec").postalCode("40000").build(),
                meals.stream().filter(meal -> Set.of("Juha od mrkve", "Špagete bolonjez", "Zelena salata s mesom").contains(meal.getName())).collect(Collectors.toSet()),
                chefs.stream().filter(chef -> Set.of("Mateo Spevec").contains(chef.getFirstName() + " " + chef.getLastName())).collect(Collectors.toSet()),
                waiters.stream().filter(waiter -> Set.of("Karlo Puklek").contains(waiter.getFirstName() + " " + waiter.getLastName())).collect(Collectors.toSet()),
                deliverers.stream().filter(deliverer -> Set.of("Karlo Kontrec").contains(deliverer.getFirstName() + " " + deliverer.getLastName())).collect(Collectors.toSet())));


        //List<Order> orders = Order.inputOrder(Constants.NUM_OF_ORDERS, restaurants, scanner);
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(
                restaurants.stream().filter(restaurant -> Objects.equals("Arka", restaurant.getName())).toList().getFirst(),
                meals.stream().filter(meal -> Set.of("Špagete bolonjez").contains(meal.getName())).collect(Collectors.toList()),
                deliverers.stream().filter(deliverer -> Objects.equals("Matija Puklek", deliverer.getFirstName() + " " + deliverer.getLastName())).findFirst().get(),
                LocalDateTime.parse("2023-10-22T14:30:45")));

        orders.add(new Order(
                restaurants.stream().filter(restaurant -> Objects.equals("Arka", restaurant.getName())).toList().getFirst(),
                meals.stream().filter(meal -> Set.of("Špagete bolonjez", "Špagete karbonare").contains(meal.getName())).collect(Collectors.toList()),
                deliverers.stream().filter(deliverer -> Objects.equals("Matija Puklek", deliverer.getFirstName() + " " + deliverer.getLastName())).findFirst().get(),
                LocalDateTime.parse("2023-11-11T11:32:35")));

        orders.add(new Order(
                restaurants.stream().filter(restaurant -> Objects.equals("McDonalds", restaurant.getName())).toList().getFirst(),
                meals.stream().filter(meal -> Set.of("Juha od mrkve", "Salata od krastavaca", "Zelena salata s mesom", "Špagete karbonare").contains(meal.getName())).collect(Collectors.toList()),
                deliverers.stream().filter(deliverer -> Objects.equals("Natali Kovačić", deliverer.getFirstName() + " " + deliverer.getLastName())).findFirst().get(),
                LocalDateTime.parse("2023-11-17T23:32:31")));

        Map<Meal, List<Restaurant>> mealRestaurants = Restaurant.addMealsToMealRestaurantMap(restaurants);
//        Restaurant.findMealInRestaurants(mealRestaurants, meals, scanner);

        RestaurantLabourExchangeOffice<Restaurant> restaurantLabourExchangeOffice = new RestaurantLabourExchangeOffice<>(restaurants);
        List<Restaurant> restaurants1 = restaurantLabourExchangeOffice.getRestaurants();

        System.out.println("Restoran s najviše radnika: ");
        Optional<Restaurant> restaurantWithMostEmployees = restaurants1.stream().max((r1, r2) -> Integer.compare(r1.getTotalEmployees(), r2.getTotalEmployees()));
        restaurantWithMostEmployees.ifPresent(restaurant -> restaurant.print(0));

        System.out.println("\nNajčešće naručivano jelo: ");
        orders.stream()
                .flatMap(order -> order.getMeals().stream())
                .collect(Collectors.groupingBy(meal -> meal, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .ifPresent(meal -> meal.print(0));

        System.out.println("\nSve namirnice koje su korištene u naručenim jelima: ");
        orders.stream()
                .flatMap(order -> order.getMeals().stream())
                .toList().stream()
                .flatMap(meal -> meal.getIngredients().stream())
                .collect(Collectors.toSet())
                .forEach(ingredient -> System.out.print(ingredient.getName() + " "));

        System.out.println("\nUkupne cijene narudžbi");
        orders.stream()
                .collect(Collectors.toMap(order -> order, Order::getMeals))
                .forEach((key, value) -> {
                    BigDecimal price = value.stream()
                            .map(Meal::getPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    System.out.println("Narudžba " + key.getId() + ": " + price);
                });

        System.out.println("\nRestorani po gradovima");
        restaurants1.stream()
                .collect(Collectors.groupingBy(
                        restaurant -> restaurant.getAddress().getCity(),
                        Collectors.mapping(
                                Restaurant::getName,
                                Collectors.toList()
                        ))
                )
                .forEach((city, restarauntName) -> {
                    System.out.println(city + " - " + restarauntName);
                });

        scanner.close();
    }
}


/*
TEST CASE:
Juha
Topla i kremasta jela od povrća ili mesa.
Salata
Svježe i zdrave miješane salate.
Juha
Tjestenina
Talijanska tjestenina s različitim umacima.
Mrkva
Voće
Juha
abcjelo
41
Kuhano
Mrkva
Rajčica
Salata
18,5
Svježe
Špageti
Tjestenina
158
Kuhano
Krumpir
Juha
77
Pečeno
Krastavac
Salata
16
Svježe

Juha od mrkve
Juha
-1
0
2
Mrkva
Krumpir
1500
15
soja
da
ne

Juha od mrkve
Špagete bolonjez
Tjestenina
2
Špageti
Rajčica
-20
20
soja
da
da

Juha od mrkve

Salata od krastavca
Salata
1
Krastavac
18
tofu
da
da

Juha od mrkve

Juha od koprive
Juha
-1
0
2
Mrkva
Krumpir
15
tofu
ne
ne

Juha od mrkve

Špagete karbonare
Tjestenina
1
Špageti
20
soja
ne
ne

Juha od mrkve

Zelena salata
Salata
1
Krastavac
18
tofu
ne
da


Juha od mrkve

Juha sa piletinom
Juha
-1
0
2
Mrkva
Krumpir
15
Piletina
Kokoš
Pečeno

Juha od mrkve

Pužići sa saftom
Tjestenina
1
Špageti
20
Govedina
Krava
kuhano

Juha od mrkve

Zelena salata s mesom
Salata
1
Krastavac
18
Piletina
Kokoš
Pečeno

Nikola
Jambrešić
1200
2023-10-12
2023-12-12
FULL_TIME
0

Nikola
Jambrešić
Luka
Vincelj
1300
2023-11-12
2023-12-12
PART_TIME
100

Mateo
Spevec
1400
2023-12-12
2025-12-12
FULL_TIME
0

Julia
Janković
1500
2023-12-12
2025-12-12
PART_TIME
500

Julia
Janković
Matija
Trstenjak
1400
2022-12-12
2025-12-12
FULL_TIME
0

Matija
Trstenjak
Karlo
Puklek
1300
2024-01-01
2025-12-12
PART_TIME
0

Matija
Puklek
500
1200
2023-12-12
2025-12-12
FULL_TIME
50

Nikola
Jambrešić
Natali
Kovačić
1200
2022-12-12
2025-12-12
PART_TIME
0

Karlo
Kontrec
1300
2023-12-12
2025-12-12
FULL_TIME
0

McDonalds
Jarunska ulica
2
Zagreb
10000
4
Juha
Juha od mrkve
Salata od krastavca
Zelena salata s mesom
Špagete karbonare
2
Nikola Jambrešić
Luka Vincelj
2
Julia Janković
Matija Trstenjak
3
Matija Puklek
Natali Kovačić
Karlo Kontrec
McDonalds
McDonalds
McDonalds
Arka
Ulica Ivana Plemenitog Zajca
13
Čakovec
40000
1
Špagete bolonjez
1
Nikola Jambrešić
2
Matija Trstenjak
Karlo Puklek
2
Matija Puklek
Karlo Kontrec
Arka
Laganini
Športska ulica
8
Čakovec
40000
3
Juha od mrkve
Špagete bolonjez
Zelena salata s mesom
1
Matea Spevec
Julia Janković
Mateo Spevec
1
Karlo Puklek
1
Karlo Kontrec

Arka
1
Špagete bolonjez
Matija Puklek
2023-10-22T14:30:45
Arka
2
Špagete bolonjez
Špagete karbonare
Matija Puklek
2023-11-11T11:32:35
McDonalds
4
Špagete bolonjez
Špagete karbonare
Juha od mrkve
Zelena salata s mesom
Natali Kovačić
2023-11-17T23:32:31
Juha od mrkve
Salata od krastavca
Zelena salata s mesom
Špagete karbonare
2
Nikola Jambrešić
Luka Vincelj
2
Julia Janković
Matija Trstenjak
3
Matija Puklek
Natali Kovačić
Karlo Kontrec
Arka
Ulica Ivana Plemenitog Zajca
13
Čakovec
40000
1
Špagete bolonjez
1
Nikola Jambrešić
2
Julia Janković
Matija Trstenjak
Karlo Puklek
2
Matija Puklek
Karlo Kontrec
Laganini
Športska ulica
8
Čakovec
40000
2
Juha od mrkve
Špagete bolonjez
1
Matea Spevec
Julia Janković
Mateo Spevec
1
Karlo Puklek
1
Karlo Kontrec

Arka
1
Špagete bolonjez
Matija Puklek
2023-10-22T14:30:45
Arka
1
Špagete bolonjez
Matija Puklek
2023-11-11T11:32:35
Bolonjez
Špagete bolonjez
*/