package hr.java.service;

import hr.java.restaurant.exception.InvalidValueException;
import hr.java.restaurant.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Input {
    public static Integer integer(Scanner scanner, String message) {
        while (true) {
            System.out.println(message);
            try {
                int input = scanner.nextInt();
                scanner.nextLine();

                if (input > 0) {
                    return input;
                } else {
                    System.out.println("Uneseni broj mora biti veći od 0. Pokušajte ponovno:");
                }
            } catch (InputMismatchException e) {
                System.out.println("Uneseni broj je neispravan. Pokušajte ponovno:");
                scanner.next();
            }
        }
    }

    public static BigDecimal bigDecimal(Scanner scanner, String message) {
        while (true) {
            System.out.println(message);
            try {
                BigDecimal input = scanner.nextBigDecimal();
                scanner.nextLine();

                return input;
            } catch (InputMismatchException e) {
                System.out.println("Uneseni broj je neispravan. Pokušajte ponovno:");
                scanner.nextLine();
            }
        }
    }

    public static String string(Scanner scanner, String message) {
        System.out.println(message);
        String stringInputValue = scanner.nextLine();

        if (!stringInputValue.isEmpty()) {
            return stringInputValue;
        } else {
            System.out.println("Niste ništa unijeli.");
            return string(scanner, message);
        }
    }

    public static String postalCode(Scanner scanner, String message) {
        System.out.println(message);
        String postalCodeInputValue = scanner.nextLine();

        if (Validation.validPostalCode(postalCodeInputValue)) {
            return postalCodeInputValue;
        } else {
            System.out.println("Unijeli ste pogrešan poštanski broj.");
            return postalCode(scanner, message);
        }
    }

    public static LocalDateTime localDateTime(Scanner scanner, String message) {
        System.out.println(message);
        String localDateTimeInputValue = scanner.nextLine();

        if(Validation.validLocalDateTime(localDateTimeInputValue)) {
            return LocalDateTime.parse(localDateTimeInputValue);
        } else {
            System.out.println("Unijeli ste pogrešan datum.");
            return localDateTime(scanner, message);
        }
    }

    public static LocalDate localDate(Scanner scanner, String message) {
        System.out.println(message);
        String localDateInputValue = scanner.nextLine();

        if(Validation.validLocalDate(localDateInputValue)) {
            return LocalDate.parse(localDateInputValue);
        } else {
            System.out.println("Unijeli ste pogrešan datum.");
            return localDate(scanner, message);
        }
    }

    public static Category categoryName(Scanner scanner, String message, Category[] categories) {
        String categoryNameInput = Input.string(scanner, message + Output.objectNameOptions(Category.categoryNameArray(categories)));
        Integer selectedCategoryIndex = Category.existsByName(categories, categoryNameInput);

        if (selectedCategoryIndex != -1) {
            return categories[selectedCategoryIndex];
        } else {
            System.out.println("Unesena kategorija ne postoji.");
            return categoryName(scanner, message, categories);
        }
    }

    public static Ingredient ingredientName(Scanner scanner, String message, Ingredient[] ingredients) {
        String ingredientNameInput = Input.string(scanner, message  + Output.objectNameOptions(Ingredient.ingredientNameArray(ingredients)));
        Integer selectedIngredientIndex = Ingredient.existsByName(ingredients, ingredientNameInput);

        if (selectedIngredientIndex != -1) {
            return ingredients[selectedIngredientIndex];
        } else {
            System.out.println("Uneseni sastojak ne postoji.");
            return ingredientName(scanner, message, ingredients);
        }
    }

    public static Meal mealName(Scanner scanner, String message, Meal[] meals) {
        String mealNameInput = Input.string(scanner, message + Output.objectNameOptions(Meal.mealNameArray(meals)));
        Integer selectedMealIndex = Meal.existsByName(meals, mealNameInput);

        if (selectedMealIndex != -1) {
            return meals[selectedMealIndex];
        } else {
            System.out.println("Uneseno jelo ne postoji.");
            return mealName(scanner, message, meals);
        }
    }

    public static Chef chefName(Scanner scanner, String message, Chef[] chefs) {
        String chefFirstAndLastNameInput = Input.string(scanner, message + Output.objectNameOptions(Chef.chefNameArray(chefs)));
        String[] chefNameInput = chefFirstAndLastNameInput.split(" ");

        if (chefNameInput.length > 1) {
            Integer selectedChefIndex = Chef.existsByName(chefs, chefNameInput[0], chefNameInput[1]);

            if(selectedChefIndex != -1) {
                return chefs[selectedChefIndex];
            }
        }

        System.out.println("Unesena osoba ne postoji.");
        return chefName(scanner, message, chefs);
    }

    public static Waiter waiterName(Scanner scanner, String message, Waiter[] waiters) {
        String waiterFirstAndLastNameInput = Input.string(scanner, message  + Output.objectNameOptions(Waiter.waiterNameArray(waiters)));
        String[] waiterNameInput = waiterFirstAndLastNameInput.split(" ");

        if (waiterNameInput.length > 1) {
            Integer selectedWaiterIndex = Waiter.existsByName(waiters, waiterNameInput[0], waiterNameInput[1]);

            if(selectedWaiterIndex != -1) {
                return waiters[selectedWaiterIndex];
            }
        }

        System.out.println("Unesena osoba ne postoji.");
        return waiterName(scanner, message, waiters);
    }

    public static Deliverer delivererName(Scanner scanner, String message, Deliverer[] deliverers) {
        String delivererFirstAndLastNameInput = Input.string(scanner, message  + Output.objectNameOptions(Deliverer.delivererNameArray(deliverers)));
        String[] delivererNameInput = delivererFirstAndLastNameInput.split(" ");

        if (delivererNameInput.length > 1) {
            Integer selectedDelivererIndex = Deliverer.existsByName(deliverers, delivererNameInput[0], delivererNameInput[1]);

            if(selectedDelivererIndex != -1) {
                return deliverers[selectedDelivererIndex];
            }
        }

        System.out.println("Unesena osoba ne postoji.");
        return delivererName(scanner, message, deliverers);
    }

    public static Restaurant restaurantName(Scanner scanner, String message, Restaurant[] restaurants) {
        String restaurantNameInput = Input.string(scanner, message  + Output.objectNameOptions(Restaurant.restaurantNameArray(restaurants)));
        Integer selectedRestaurantIndex = Restaurant.existsByName(restaurants, restaurantNameInput);

        if(selectedRestaurantIndex != -1) {
            return restaurants[selectedRestaurantIndex];
        }

        System.out.println("Uneseni restoran ne postoji.");
        return restaurantName(scanner, message, restaurants);
    }

    public static Contract contract(Scanner scanner, String message) {
        BigDecimal salary;

        while (true) {
            salary = Input.bigDecimal(scanner, "Unesite plaću.");

            try {
                Validation.checkSalary(salary);
                break;
            } catch (InvalidValueException e) {
                System.out.println("Unesena plaća je neispravna. Plaća mora biti veća od minimalne (" + Contract.getMinSalary() + ").");
            }
        }

        LocalDate startDate = Input.localDate(scanner, "Unesite početak ugovora.");
        LocalDate endDate = Input.localDate(scanner, "Unesite kraj ugovora.");
        String contractType = Input.string(scanner, "Unesite tip ugovora.");

        return new Contract(salary, startDate, endDate, contractType);
    }

    public static boolean booleanValue(Scanner scanner, String message) {
        System.out.println(message);
        String booleanInputValue = Input.string(scanner, "Unesite 'da' ili 'ne'.");

        if (booleanInputValue.equals("da")) {
            return true;
        } else if (booleanInputValue.equals("ne")) {
            return false;
        } else {
            return booleanValue(scanner, message);
        }
    }
}