package org.example.Dto;



import lombok.Data;

import java.time.LocalDate;


public abstract class Personne {

    protected int id;

    protected String nom;
    protected String prenom;
    protected String telephone;
    protected LocalDate dateNaissance;

    public Personne(int id, String nom, String prenom, String telephone, LocalDate dateNaissance) {
        setId(id);
        setNom(nom);
        setPrenom(prenom);
        setTelephone(telephone);
        setDateNaissance(dateNaissance);
    }

    public Personne(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
}
