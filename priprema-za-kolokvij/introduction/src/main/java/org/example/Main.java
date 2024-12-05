package org.example;

import java.util.Optional;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        Optional<String> a = Optional.ofNullable("a");

        a.ifPresent(x -> System.out.println(x));
    }
}