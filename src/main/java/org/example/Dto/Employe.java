package org.example.Dto;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Employe extends Personne{
    private String matricule;
    private LocalDate dateRecrutement;
    private String adresseEmail;
    private List<Compte> compteArrayList = new ArrayList<>();
    private List<Client> clientArrayList = new ArrayList<>();
    private List<Operation> operationArrayList = new ArrayList<>();
    private List<MissionDetails> missionDetailsList;

    private Agence agence;


    public Employe(int id,String nom, String prenom, String telephone, LocalDate dateNaissance, String matricule, LocalDate dateRecrutement, String adresseEmail) {
        super(id,nom, prenom, telephone, dateNaissance);
        setMatricule(matricule);
        setDateRecrutement(dateRecrutement);
        setAdresseEmail(adresseEmail);
    }


    public Employe(){

    }

    public Employe(String nom, String prenom, LocalDate dateNaissance, String telephone, String matricule, String adresseEmail, LocalDate dateRecrutement) {
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public LocalDate getDateRecrutement() {
        return dateRecrutement;
    }

    public void setDateRecrutement(LocalDate dateRecrutement) {
        this.dateRecrutement = dateRecrutement;
    }

    public String getAdresseEmail() {
        return adresseEmail;
    }

    public void setAdresseEmail(String adresseEmail) {
        this.adresseEmail = adresseEmail;
    }


    public List<MissionDetails> getMissionDetailsList() {
        return missionDetailsList;
    }

    public void setMissionDetailsList(List<MissionDetails> missionDetailsList) {
        this.missionDetailsList = missionDetailsList;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Employe Information:\n");
        stringBuilder.append("+-------------------------+\n");
        stringBuilder.append(String.format("| ID: %-20s|\n", id));
        stringBuilder.append(String.format("| Matricule: %-18s|\n", matricule));
        stringBuilder.append(String.format("| AdresseEmail: %-15s|\n", adresseEmail));
        stringBuilder.append(String.format("| Nom: %-19s|\n", nom));
        stringBuilder.append(String.format("| Prénom: %-16s|\n",prenom));
        stringBuilder.append(String.format("| Date de naissance: %-6s|\n", dateNaissance));
        stringBuilder.append(String.format("| Date de recrutement: %-6s|\n", dateRecrutement));
        stringBuilder.append(String.format("| Téléphone: %-16s|\n", telephone));
        stringBuilder.append("+-------------------------+\n");
        return stringBuilder.toString();
    }

    public Employe(String matricule, String adresseemail, LocalDate daterecrutement) {
    }
}
