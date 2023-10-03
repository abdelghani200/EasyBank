package org.example.Dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Agence {
    private String code;
    private String nom;
    private String adresse;
    private String telephone;
    private List<Compte> comptes = new ArrayList<>();
    private List<Employe> employes = new ArrayList<>();



}
