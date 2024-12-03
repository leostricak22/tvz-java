package org.example.model;

public class Krug extends DvodimenzionalniOblik {
    private double polumjer=15;

    @Override
    public double izracunajPovrsinu() {
        super.visina = 15;
        super.sirina = 10;

        System.out.println(super.izracunajPovrsinu());
        return Math.PI * Math.pow(polumjer, 2);
    }
}