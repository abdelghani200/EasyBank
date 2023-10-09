package org.example.Dto;


import java.time.LocalDate;
import java.util.List;


public class CompteCourant extends Compte{

    private double decouvert;


    public CompteCourant(int id, String numero, double solde, LocalDate dateCreation, TypeCompte status, double decouvert, Agence agence) {
        super(id, numero, solde, dateCreation, status,agence);
        setDecouvert(decouvert);
    }

    public CompteCourant() {

    }

    /*
    public CompteCourant(int clientId, String numero, double solde, LocalDate dateCreation, TypeCompte status) {
        super(clientId,numero,solde,dateCreation,status);
    }

     */

    public double getDecouvert() {
        return decouvert;
    }

    public void setDecouvert(double decouvert) {
        this.decouvert = decouvert;
    }
}





