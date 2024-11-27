package hr.java.service;

import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.exception.InvalidValueException;
import hr.java.restaurant.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Represents an input from the user.
 */
public class Input {
    private static final Logger logger = LoggerFactory.getLogger(Input.class);

    /**
     * Reads an integer value from the user.
     * @param scanner the scanner object used for input
     * @param message the message to be displayed to the user
     * @throws InputMismatchException if the input value is not an integer
     * @return the integer value
     */
    public static Integer integer(Scanner scanner, String message) {
        logger.info("Integer input.");

        while (true) {
            System.out.println(message);
            try {
                int input = scanner.nextInt();
                scanner.nextLine();
                if (input <= 0)
                    throw new InputMismatchException();

                return input;
            } catch (InputMismatchException e) {
                logger.error("Entered invalid Integer value.");
                System.out.println("Uneseni broj je neispravan. Pokušajte ponovno:");
                scanner.next();
            }
        }
    }

    /**
     * Reads a BigDecimal value from the user.
     * @param scanner the scanner object used for input
     * @param message the message to be displayed to the user
     * @throws InputMismatchException if the input value is not a BigDecimal
     * @return the BigDecimal value
     */
    public static BigDecimal bigDecimal(Scanner scanner, String message) {
        logger.info("BigDecimal input.");
        while (true) {
            System.out.println(message);
            try {
                BigDecimal input = scanner.nextBigDecimal();
                scanner.nextLine();

                return input;
            } catch (InputMismatchException e) {
                logger.error("Entered invalid BigDecimal value.");
                System.out.println("Uneseni broj je neispravan. Pokušajte ponovno:");
                scanner.nextLine();
            }
        }
    }

    /**
     * Reads a string value from the user.
     * @param scanner the scanner object used for input
     * @param message the message to be displayed to the user
     * @return the string value
     */
    public static String string(Scanner scanner, String message) {
        logger.info("String input.");
        System.out.println(message);
        String stringInputValue = scanner.nextLine();

        if (!stringInputValue.isEmpty()) {
            return stringInputValue;
        } else {
            logger.warn("Entered empty string.");
            System.out.println("Niste ništa unijeli.");
            return string(scanner, message);
        }
    }

    /**
     * Reads a postal code from the user.
     * @param scanner the scanner object used for input
     * @param message the message to be displayed to the user
     * @return the postal code
     */
    public static String postalCode(Scanner scanner, String message) {
        logger.info("Postal code input.");
        System.out.println(message);
        String postalCodeInputValue = scanner.nextLine();

        if (Validation.validPostalCode(postalCodeInputValue)) {
            return postalCodeInputValue;
        } else {
            logger.warn("Entered invalid postal code.");
            System.out.println("Unijeli ste pogrešan poštanski broj.");
            return postalCode(scanner, message);
        }
    }

    /**
     * Reads a LocalDateTime value from the user.
     * @param scanner the scanner object used for input
     * @param message the message to be displayed to the user
     * @return the LocalDateTime value
     */
    public static LocalDateTime localDateTime(Scanner scanner, String message) {
        logger.info("LocalDateTime input.");
        System.out.println(message);
        String localDateTimeInputValue = scanner.nextLine();

        if(Validation.validLocalDateTime(localDateTimeInputValue)) {
            return LocalDateTime.parse(localDateTimeInputValue);
        } else {
            logger.warn("Entered invalid LocalDateTime value.");
            System.out.println("Unijeli ste pogrešan datum.");
            return localDateTime(scanner, message);
        }
    }

    /**
     * Reads a LocalDate value from the user.
     * @param scanner the scanner object used for input
     * @param message the message to be displayed to the user
     * @return the LocalDate value
     */
    public static LocalDate localDate(Scanner scanner, String message) {
        logger.info("LocalDate input.");
        System.out.println(message);
        String localDateInputValue = scanner.nextLine();

        if(Validation.validLocalDate(localDateInputValue)) {
            return LocalDate.parse(localDateInputValue);
        } else {
            logger.warn("Entered invalid LocalDate value.");
            System.out.println("Unijeli ste pogrešan datum.");
            return localDate(scanner, message);
        }
    }

    /**
     * Reads a person from the user.
     * @param scanner the scanner object used for input
     * @param message the message to be displayed to the user
     * @return the person
     */
    public static Contract contract(Scanner scanner, String message) {
        logger.info("Contract input.");
        BigDecimal salary;

        while (true) {
            salary = Input.bigDecimal(scanner, "Unesite plaću.");

            try {
                Validation.checkSalary(salary);
                break;
            } catch (InvalidValueException e) {
                logger.error("Entered invalid salary value.");
                System.out.println("Unesena plaća je neispravna. Plaća mora biti veća od minimalne (" + Contract.getMinSalary() + ").");
            }
        }

        LocalDate startDate = Input.localDate(scanner, "Unesite početak ugovora.");
        LocalDate endDate = Input.localDate(scanner, "Unesite kraj ugovora.");
        ContractType contractType = ContractType.valueOf(Input.string(scanner, "Unesite tip ugovora."));

        return new Contract(salary, startDate, endDate, contractType);
    }

    /**
     * Reads a boolean value from the user.
     * @param scanner the scanner object used for input
     * @param message the message to be displayed to the user
     * @return the boolean value
     */
    public static boolean booleanValue(Scanner scanner, String message) {
        logger.info("Boolean input.");
        System.out.println(message);
        String booleanInputValue = Input.string(scanner, "Unesite 'da' ili 'ne'.");

        if (booleanInputValue.equals("da")) {
            return true;
        } else if (booleanInputValue.equals("ne")) {
            return false;
        } else {
            logger.warn("Entered invalid boolean value.");
            return booleanValue(scanner, message);
        }
    }
}