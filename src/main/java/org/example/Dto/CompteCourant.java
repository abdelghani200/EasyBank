package org.example.Dto;


import java.time.LocalDate;
import java.util.List;


public class CompteCourant extends Compte{

    private double decouvert;


    public CompteCourant(int id, String numero, double solde, LocalDate dateCreation, TypeCompte status, double decouvert) {
        super(id, numero, solde, dateCreation, status);
        setDecouvert(decouvert);
    }

    public CompteCourant() {

    }

    public double getDecouvert() {
        return decouvert;
    }

    public void setDecouvert(double decouvert) {
        this.decouvert = decouvert;
    }
}





