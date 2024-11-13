package hr.java.service;

public class Validation {
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

    public static boolean validLocalDate(String value) {
        return value.matches("\\b([0-9]{4})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])\\b");
    }
}
