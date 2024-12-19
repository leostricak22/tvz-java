package hr.java.restaurant.util;

import java.time.format.DateTimeFormatter;

public class Localization {
    public static DateTimeFormatter DateFormatter() {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    }
}
