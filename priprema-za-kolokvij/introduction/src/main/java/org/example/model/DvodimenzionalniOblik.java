package org.example.model;

public class DvodimenzionalniOblik {
    protected double visina;
    protected double sirina;

    public double izracunajPovrsinu() {
        return visina * sirina;
    }
}