package org.example.Dto;


import java.time.LocalDate;
import java.util.List;

public class Client  extends Personne{

    private int id;
    private String code;
    private String adresse;
    List<Compte> compteList;
    private Employe employe;

    public Client(int id, String nom, String prenom, String telephone, LocalDate dateNaissance, String code, String adresse) {
        super(id, nom, prenom, telephone, dateNaissance);
        setId(id);
        setCode(code);
        setAdresse(adresse);
    }

    public Client(){

    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public Client(String code, String adresse) {
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Client Information:\n");
        stringBuilder.append("+-------------------------+\n");
        stringBuilder.append(String.format("| ID: %-20s|\n", id));
        stringBuilder.append(String.format("| Code: %-18s|\n", code));
        stringBuilder.append(String.format("| Adresse: %-15s|\n", adresse));
        stringBuilder.append(String.format("| Nom: %-19s|\n", getNom()));
        stringBuilder.append(String.format("| Prénom: %-16s|\n", getPrenom()));
        stringBuilder.append(String.format("| Date de naissance: %-6s|\n", getDateNaissance()));
        stringBuilder.append(String.format("| Téléphone: %-16s|\n", getTelephone()));
        stringBuilder.append("+-------------------------+\n");
        return stringBuilder.toString();
    }



}
