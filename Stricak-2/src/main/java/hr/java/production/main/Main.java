package hr.java.production.main;

import hr.java.restaurant.model.*;
import hr.java.service.Input;

import java.util.Scanner;

public class
Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Category[] categories = new Category[3];
        Category.inputCategory(categories, scanner);

        Ingredient[] ingredients = new Ingredient[5];
        Ingredient.inputIngredient(ingredients, categories, scanner);

        Meal[] meals = new Meal[9];
        int counter = 0;

        System.out.println("Unos veganskih jela: ");
        for (;counter < 3; counter++)
            meals[counter] = VeganMeal.inputVeganMeal(categories, ingredients, scanner);

        System.out.println("Unos vegetarijanskih jela: ");
        for (;counter < 6; counter++)
            meals[counter] = VegeterianMeal.inputVegeterianMeal(categories, ingredients, scanner);

        System.out.println("Unos mesnih jela: ");
        for (;counter < 9; counter++)
            meals[counter] = MeatMeal.inputMeatMeal(categories, ingredients, scanner);

        Person[] people = new Person[9];
        int index = 0;

        Chef[] chefs = new Chef[3];
        Chef.inputChef(chefs, scanner);
        for (Chef chef : chefs) {
            people[index++] = chef;
        }

        Waiter[] waiters = new Waiter[3];
        Waiter.inputWaiter(waiters, scanner);
        for (Waiter waiter : waiters) {
            people[index++] = waiter;
        }

        Deliverer[] deliverers = new Deliverer[3];
        Deliverer.inputDeliverer(deliverers, scanner);
        for (Deliverer deliverer : deliverers) {
            people[index++] = deliverer;
        }

        Restaurant[] restaurants = new Restaurant[3];
        Restaurant.inputRestaurant(restaurants, meals, chefs, waiters, deliverers, scanner);

        Order[] orders = new Order[3];
        Order.inputOrder(orders, restaurants, meals, scanner);

        System.out.println("Osoba s najvećom plaćom:");
        Person highestSalaryPerson = Person.findHighestSalaryPerson(people);
        highestSalaryPerson.print(1);

        System.out.println("\nOsoba s najduljim ugovorom (koji je najranije započeo): ");
        Person longestContractPerson = Person.findLongestContractPerson(people);
        longestContractPerson.print(1);

        Meal mostCaloricMeal = Meal.findMostCaloricMeal(meals);
        System.out.println("\nNajkaloričnije jelo (" + mostCaloricMeal.getTotalCalories() + " kcal): ");
        mostCaloricMeal.print(1);

        Meal leastCaloricMeal = Meal.findLeastCaloricMeal(meals);
        System.out.println("\nNajmanje kalorično jelo: (" + leastCaloricMeal.getTotalCalories() + " kcal): ");
        leastCaloricMeal.print(1);

        scanner.close();
    }
}


/*
TEST CASE:
Juha
Topla i kremasta jela od povrća ili mesa.
Salata
Svježe i zdrave miješane salate.
Tjestenina
Talijanska tjestenina s različitim umacima.
Mrkva
Voće
Juha
abcjelo
41
Kuhano
Rajčica
Salata
18.5
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
15
soja
da
ne

Špagete bolonjez
Tjestenina
2
Špageti
Rajčica
20
soja
da
da

Salata od krastavca
Salata
1
Krastavac
18
tofu
da
da


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

Špagete karbonare
Tjestenina
1
Špageti
20
soja
ne
ne

Zelena salata
Salata
1
Krastavac
18
tofu
ne
da



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

Pužići sa saftom
Tjestenina
1
Špageti
20
Govedina
Krava
kuhano

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

Matija
Trstenjak
1400
2022-12-12
2025-12-12
FULL_TIME
0

Karlo
Puklek
1300
2024-01-01
2025-12-12
PART_TIME
0

Matija
Puklek
1200
2023-12-12
2025-12-12
FULL_TIME
50

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
4Juha
Topla i kremasta jela od povrća ili mesa.
Salata
Svježe i zdrave miješane salate.
Tjestenina
Talijanska tjestenina s različitim umacima.
Mrkva
Voće
Juha
abcjelo
41
Kuhano
Rajčica
Salata
18.5
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
15
soja
da
ne

Špagete bolonjez
Tjestenina
2
Špageti
Rajčica
20
soja
da
da

Salata od krastavca
Salata
1
Krastavac
18
tofu
da
da


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

Špagete karbonare
Tjestenina
1
Špageti
20
soja
ne
ne

Zelena salata
Salata
1
Krastavac
18
tofu
ne
da



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

Pužići sa saftom
Tjestenina
1
Špageti
20
Govedina
Krava
kuhano

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

Matija
Trstenjak
1400
2022-12-12
2025-12-12
FULL_TIME
0

Karlo
Puklek
1300
2024-01-01
2025-12-12
PART_TIME
0

Matija
Puklek
1200
2023-12-12
2025-12-12
FULL_TIME
50

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
McDonalds
3
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
McDonalds
3
Špagete karbonare
Juha od mrkve
Zelena salata s mesom
Natali Kovačić
2023-11-17T23:32:31
*/