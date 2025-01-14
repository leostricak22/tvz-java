package hr.java.restaurant.util;

import java.time.format.DateTimeFormatter;

public class Localization {
    public static DateTimeFormatter DateFormatter() {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    }

    public static DateTimeFormatter DateTimeFormatter() {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
    }
}
