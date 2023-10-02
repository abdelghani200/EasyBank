package org.example.Dto;


import java.time.LocalDate;

public class CompteEpargne extends Compte{

    private double tauxInteret;

    public CompteEpargne(int id, String numero, double solde, LocalDate dateCreation, TypeCompte status, double tauxInteret) {
        super(id, numero, solde, dateCreation, status);
        setTauxInteret(tauxInteret);
    }

    public double getTauxInteret() {
        return tauxInteret;
    }

    public void setTauxInteret(double tauxInteret) {
        this.tauxInteret = tauxInteret;
    }
}
