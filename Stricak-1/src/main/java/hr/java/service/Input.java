package hr.java.service;

import hr.java.restaurant.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Input {
    public static boolean isBigDecimal(String value) {
        return value.matches("\\d+(\\.\\d+)?");
    }

    public static boolean isInteger(String value) {
        return value.matches("[1-9]\\d*");
    }

    public static boolean validPostalCode(String value) {
        return value.matches("^[1-5]\\d{4}$");
    }

    public static boolean validLocalDateTime(String value) {
        return value.matches("\\b([0-9]{4})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])T([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])\\b");
    }

    public static Integer integer(Scanner scanner, String message) {
        System.out.println(message);
        String integerInputValue = scanner.nextLine();

        if(isInteger(integerInputValue)) {
            return Integer.parseInt(integerInputValue);
        } else {
            System.out.println("Uneseni broj je neispravan.");
            return integer(scanner, message);
        }
    }

    public static BigDecimal bigDecimal(Scanner scanner, String message) {
        System.out.println(message);
        String bigDecimalInputValue = scanner.nextLine();

        if (isBigDecimal(bigDecimalInputValue)) {
            return new BigDecimal(bigDecimalInputValue);
        } else {
            System.out.println("Uneseni broj je neispravan.");
            return bigDecimal(scanner, message);
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

        if (validPostalCode(postalCodeInputValue)) {
            return postalCodeInputValue;
        } else {
            System.out.println("Unijeli ste pogrešan poštanski broj.");
            return postalCode(scanner, message);
        }
    }

    public static LocalDateTime localDateTime(Scanner scanner, String message) {
        System.out.println(message);
        String localDateTimeInputValue = scanner.nextLine();

        if(validLocalDateTime(localDateTimeInputValue)) {
            return LocalDateTime.parse(localDateTimeInputValue);
        } else {
            System.out.println("Unijeli ste pogrešan datum.");
            return localDateTime(scanner, message);
        }
    }

    public static String objectNameOptions(String[] nameOptions) {
        String messageExtend = "";
        if(nameOptions.length > 0) {
            messageExtend += " - (";
            for (int i = 0; i < nameOptions.length; i++) {
                messageExtend += nameOptions[i];

                if (i + 1 != nameOptions.length)
                    messageExtend += ", ";
                else
                    messageExtend += ")";
            }
        }

        return messageExtend;
    }

    public static Category categoryName(Scanner scanner, String message, Category[] categories) {
        String categoryNameInput = Input.string(scanner, message + objectNameOptions(Category.categoryNameArray(categories)));
        Integer selectedCategoryIndex = Category.existsByName(categories, categoryNameInput);

        if (selectedCategoryIndex != -1) {
            return categories[selectedCategoryIndex];
        } else {
            System.out.println("Unesena kategorija ne postoji.");
            return categoryName(scanner, message, categories);
        }
    }

    public static Ingredient ingredientName(Scanner scanner, String message, Ingredient[] ingredients) {
        String ingredientNameInput = Input.string(scanner, message  + objectNameOptions(Ingredient.ingredientNameArray(ingredients)));
        Integer selectedIngredientIndex = Ingredient.existsByName(ingredients, ingredientNameInput);

        if (selectedIngredientIndex != -1) {
            return ingredients[selectedIngredientIndex];
        } else {
            System.out.println("Uneseni sastojak ne postoji.");
            return ingredientName(scanner, message, ingredients);
        }
    }

    public static Meal mealName(Scanner scanner, String message, Meal[] meals) {
        String mealNameInput = Input.string(scanner, message + objectNameOptions(Meal.mealNameArray(meals)));
        Integer selectedMealIndex = Meal.existsByName(meals, mealNameInput);

        if (selectedMealIndex != -1) {
            return meals[selectedMealIndex];
        } else {
            System.out.println("Uneseno jelo ne postoji.");
            return mealName(scanner, message, meals);
        }
    }

    public static Chef chefName(Scanner scanner, String message, Chef[] chefs) {
        String chefFirstAndLastNameInput = Input.string(scanner, message + objectNameOptions(Chef.chefNameArray(chefs)));
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
        String waiterFirstAndLastNameInput = Input.string(scanner, message  + objectNameOptions(Waiter.waiterNameArray(waiters)));
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
        String delivererFirstAndLastNameInput = Input.string(scanner, message  + objectNameOptions(Deliverer.delivererNameArray(deliverers)));
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
        String restaurantNameInput = Input.string(scanner, message  + objectNameOptions(Restaurant.restaurantNameArray(restaurants)));
        Integer selectedRestaurantIndex = Restaurant.existsByName(restaurants, restaurantNameInput);

        if(selectedRestaurantIndex != -1) {
            return restaurants[selectedRestaurantIndex];
        }

        System.out.println("Uneseni restoran ne postoji.");
        return restaurantName(scanner, message, restaurants);
    }

    public static void tabulatorPrint(Integer tabulators) {
        for (int i = 0; i < tabulators; i++) {
            System.out.print("|\t");
        }
    }
}
