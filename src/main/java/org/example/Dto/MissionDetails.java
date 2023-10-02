package org.example.Dto;

import java.time.LocalDate;

public class MissionDetails {

    private Employe employe;
    private Mission mission;
    private String nom;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    public MissionDetails(Employe employe, Mission mission, String nom, LocalDate dateDebut, LocalDate dateFin) {
        setEmploye(employe);
        setMission(mission);
        setNom(nom);
        setDateDebut(dateDebut);
        setDateFin(dateFin);
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public String toString() {
        return
                "Matricule du employe=" + employe.getMatricule() +
                "Nom du employe=" + employe.getNom() +
                "Prenom du employe=" + employe.getPrenom() +
                "Date de naissance du employe=" + employe.getDateNaissance() +
                ",Code mission=" + mission.getCode() +
                ",Nom mission=" + mission.getNom() +
                ",Description mission=" + mission.getDescription() +
                ", nom='" + nom + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin
                ;
    }
}
