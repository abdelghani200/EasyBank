package org.example.Dto;


import java.util.List;

public class Mission {

    private int id;
    private String code;
    private String nom;
    private String description;

    List<Employe> employeList;

    public Mission(int id,String code, String nom, String description) {
        setId(id);
        setCode(code);
        setNom(nom);
        setDescription(description);
    }

    public Mission() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Mission{" +
                //"id=" + id +
                ", code='" + code + '\'' +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
