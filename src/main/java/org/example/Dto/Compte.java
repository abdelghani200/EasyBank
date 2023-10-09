package org.example.Dto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


public abstract class Compte {

    protected int id;
    protected String numero;
    protected double solde;
    protected LocalDate dateCreation;
    protected TypeCompte status;

    protected Client client;
    
    protected Employe employe;
    List<Operation> operationList;

    protected Agence agence;


    public Compte(int id, String numero, double solde, LocalDate dateCreation, TypeCompte status) {
        setId(id);
        setNumero(numero);
        setSolde(solde);
        setDateCreation(dateCreation);
        setStatus(status);
    }

    public Compte() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public TypeCompte getStatus() {
        return status;
    }

    public void setStatus(TypeCompte status) {
        this.status = status;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public List<Operation> getOperationList() {
        return operationList;
    }

    public void setOperationList(List<Operation> operationList) {
        this.operationList = operationList;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Compte Information:\n");
        stringBuilder.append("+-------------------------+\n");
        stringBuilder.append(String.format("| ID: %-20s|\n", id));
        stringBuilder.append(String.format("| Numéro de compte: %-10s|\n", numero));
        stringBuilder.append(String.format("| Solde: %-18s|\n", solde));
        stringBuilder.append(String.format("| Date de création: %-8s|\n", dateCreation));
        stringBuilder.append(String.format("| Statut: %-17s|\n", status));
        stringBuilder.append(String.format("| Nom du client: %-13s|\n", client.getNom()));
        stringBuilder.append(String.format("| Prénom du client: %-10s|\n", client.getPrenom()));
        stringBuilder.append(String.format("| Téléphone du client: %-8s|\n", client.getTelephone()));
        stringBuilder.append(String.format("| Date de naissance du client: %-4s|\n", client.getDateNaissance()));
        stringBuilder.append(String.format("| Matricule de l'employé: %-7s|\n", employe.getMatricule()));
        stringBuilder.append(String.format("| Nom de l'employé: %-13s|\n", employe.getNom()));
        stringBuilder.append(String.format("| Prénom de l'employé: %-10s|\n", employe.getPrenom()));
        stringBuilder.append(String.format("| Téléphone de l'employé: %-8s|\n", employe.getTelephone()));
        stringBuilder.append(String.format("| Date de naissance de l'employé: %-4s|\n", employe.getDateNaissance()));
        stringBuilder.append("+-------------------------+\n");
        return stringBuilder.toString();
    }

}
