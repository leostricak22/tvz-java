package hr.java.production.main;

import hr.java.restaurant.model.*;
import hr.java.restaurant.sort.MealRestaurantComparator;
import hr.java.service.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Represents the main class.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting the application");

        Scanner scanner = new Scanner(System.in);

        List<Category> categories = Category.inputCategoryList(Constants.NUM_OF_CATEGORIES, scanner);
        Set<Ingredient> ingredients = Ingredient.inputIngredientSet(Constants.NUM_OF_INGREDIENTS, categories, scanner);
        Set<Meal> meals = Meal.inputMealSet(categories, ingredients, scanner);

        Set<Person> people = new HashSet<>();

        Set<Chef> chefs = Chef.inputChefSet(Constants.NUM_OF_CHEFS, people, scanner);
        people.addAll(chefs);

        Set<Waiter> waiters = Waiter.inputWaiterSet(Constants.NUM_OF_WAITERS, people, scanner);
        people.addAll(waiters);

        Set<Deliverer> deliverers = Deliverer.inputDeliverer(Constants.NUM_OF_DELIVERERS, people, scanner);
        people.addAll(deliverers);

        List<Restaurant> restaurants = Restaurant.inputRestaurantList(Constants.NUM_OF_RESTAURANTS, meals, chefs, waiters, deliverers, scanner);
        List<Order> orders = Order.inputOrder(Constants.NUM_OF_ORDERS, restaurants, scanner);

        Map<Meal, List<Restaurant>> mealRestaurants = Restaurant.addMealsToMealRestaurantMap(restaurants);
        Restaurant.findMealInRestaurants(mealRestaurants, meals, scanner);

/*        List<Person> peopleSortedBySalary = new ArrayList<>(people);
        peopleSortedBySalary.sort(new PersonSalaryComparator());

        for (Person person : peopleSortedBySalary) {
            person.print(0);
        }
*/

        System.out.println("RADNICI S NAJVEĆOM PLAĆOM U SVAKOM OD RESTORANA");
        for(Restaurant restaurant : restaurants) {
            System.out.println("Restoran: " + restaurant.getName());
            restaurant.personWithHighestSalary().print(0);
        }

        System.out.println("\nRADNICI S NAJDUŽIM UGOVOROM U SVAKOM OD RESTORANA");
        for(Restaurant restaurant : restaurants) {
            System.out.println("Restoran: " + restaurant.getName());
            restaurant.personWithLongestContract().print(0);
        }

        System.out.println("\nNAJVIŠE JELA PO BROJU RESTORANA");
        List<Map.Entry<Meal, List<Restaurant>>> mealRestaurantSortedEntries = new ArrayList<>(mealRestaurants.entrySet());
        mealRestaurantSortedEntries.sort(new MealRestaurantComparator());

        int i=0;
        for (Map.Entry<Meal, List<Restaurant>> mealRestaurant : mealRestaurantSortedEntries) {
            System.out.println(mealRestaurant.getKey().getName() + ": " + mealRestaurant.getValue().size());
            i++;
        }

/*        for (Restaurant restaurant : restaurants)
            restaurant.print(0);*/

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