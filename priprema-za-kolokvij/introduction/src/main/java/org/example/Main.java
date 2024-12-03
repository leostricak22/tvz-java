package org.example;

import org.example.model.Beverage;
import org.example.model.CoffeeShop;
import org.example.model.DvodimenzionalniOblik;
import org.example.model.Krug;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /*
        Scanner sc = new Scanner(System.in); // kreiranje Scanner objekta

        System.out.println("Unesite naziv kafića: ");
        String coffeeShopName = sc.nextLine(); // unos stringa

        System.out.println("Unesite adresu kafića: ");
        String address = sc.nextLine();

        System.out.println("Unesite broj telefona kafića: ");
        String phone = sc.nextLine();

        System.out.println("Unesite email kafića: ");
        String email = sc.nextLine();

        System.out.println("Unesite vlansika kafića: ");
        String owner = sc.nextLine();

        System.out.println("Unesite broj različitih pića: ");
        Integer numberOfBeverages = sc.nextInt();
        sc.nextLine(); // Nakon unosa broja ostaje enter u bufferu i treba se očistiti

        Beverage[] beverages = new Beverage[numberOfBeverages];

        for (int i = 0; i < numberOfBeverages; i++) {
            System.out.println("Unesite naziv pića: ");
            String beverageName = sc.nextLine();
            System.out.println("Unesite cijenu pića: ");
            BigDecimal beveragePrice = sc.nextBigDecimal();
            sc.nextLine();

            beverages[i] = new Beverage((long) (i+1), beverageName, beveragePrice);
        }
        
        CoffeeShop cheersCoffeeShop = new CoffeeShop(coffeeShopName, address, phone, email, owner, beverages);

        Beverage theMostExpensiveBeverage = beverages[0];
        for (int i = 1; i < cheersCoffeeShop.getPriceList().length; i++) {
            if(cheersCoffeeShop.getPriceList()[i].getPrice().compareTo(theMostExpensiveBeverage.getPrice()) > 0)
                theMostExpensiveBeverage = cheersCoffeeShop.getPriceList()[i];
        }

        System.out.println(
                "Najskuplje piće u kafiću " + cheersCoffeeShop.getName() +
                " je " + theMostExpensiveBeverage.getName() +
                " i košta " + theMostExpensiveBeverage.getPrice() + "€."
        );

        sc.close(); // zatvaranje scanner objekta
        // zatvara se zbog oslobodenja resura i zbog pravilnog upravljanja memorijom
         */

        Scanner sc = new Scanner(System.in);

        boolean ispravno;
        Integer nekiBroj = null;
        do {
            ispravno = true;

            try {
                System.out.println("unesi broj");
                nekiBroj = sc.nextInt();

                if(nekiBroj < 0) {
                    ispravno = false;
                    System.out.println("Krivo.");
                }
            } catch (Exception e) {
                System.out.println("Krivo.");
                ispravno = false;
            }
            sc.nextLine();

        } while (!ispravno);

        System.out.println("Unesi nesto drugo: ");
        String hmhm = sc.nextLine();

        System.out.println("Neki broj: " + nekiBroj);
        System.out.println("Nesto drugo: " + hmhm);

        sc.close();
    }
}