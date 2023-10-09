package org.example.Dto;

import java.util.List;
import java.time.LocalDate;
import java.util.Date;


public class Operation {

    protected String numero;
    protected LocalDate dateCreation;
    protected double montant;
    protected TypeOperation status;

    protected Employe employe;

    protected Compte compte;
    protected List<Employe> employeList;

    public Operation(String numero, LocalDate dateCreation, double montant, TypeOperation status) {
       setNumero(numero);
       setMontant(montant);
       setDateCreation(dateCreation);
       setStatus(status);
    }

    public Operation(){

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

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public List<Employe> getEmployeList() {
        return employeList;
    }

    public void setEmployeList(List<Employe> employeList) {
        this.employeList = employeList;
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
