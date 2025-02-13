package hr.javafx.coffe.caffee.javafxcaffee.main;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class Student {
    private String ime, prezime, jmbag;

    public Student(String ime, String prezime, String jmbag) {
        this.ime = ime;
        this.prezime = prezime;
        this.jmbag = jmbag;
    }
}

public class Test {

    public static void main(String[] args) {
        List<Student> listaStudenata = new ArrayList<>();
        listaStudenata.add(new Student("Ivo", "Ivić", "1234567890"));
        listaStudenata.add(new Student("Marko", "Markić", "0987654321"));
    }
}
