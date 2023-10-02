package org.example.Dto;

import java.util.List;
import java.time.LocalDate;
import java.util.Date;


public class Operation {

    private String numero;
    private LocalDate dateCreation;
    private double montant;
    private TypeOperation status;
    private Compte compte;
    List<Employe> employeList;

    public Operation(String numero, LocalDate dateCreation, double montant, TypeOperation status) {
       setNumero(numero);
       setMontant(montant);
       setDateCreation(dateCreation);
       setStatus(status);
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public TypeOperation getStatus() {
        return status;
    }

    public void setStatus(TypeOperation status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "numero='" + numero + '\'' +
                ", dateCreation=" + dateCreation +
                ", montant=" + montant +
                ", status=" + status +
                '}';
    }
}
