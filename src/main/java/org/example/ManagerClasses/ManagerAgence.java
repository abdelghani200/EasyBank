package org.example.ManagerClasses;

import org.example.Dto.Agence;
import org.example.Exception.AgenceException;
import org.example.Services.ServiceAgence;

import java.util.Scanner;

public class ManagerAgence {
    private final Scanner scanner;
    private final ServiceAgence serviceAgence;

    public ManagerAgence(Scanner scanner, ServiceAgence serviceAgence) {
        this.scanner = scanner;
        this.serviceAgence = serviceAgence;
    }

    public void startAgenceMenu() {
        int choix;
        do {
            System.out.println("\nMenu Agence:");
            System.out.println("1. Créer une agence");
            System.out.println("2.Retour au menu principal");
            System.out.println("Entrez votre choix :");

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix){
                case 1:
                    createAgence();
                    break;
                case 2:
                    return;
                default:
                    System.out.println("choix invalide");
            }
        }while (true);
    }

    public void createAgence(){
        System.out.println("Entrer le code de l'agence : ");
        String code = scanner.nextLine();
        System.out.println("Entrer le nom de l'agence : ");
        String nom = scanner.nextLine();
        System.out.println("Entrer l'adresse de l'agence : ");
        String adresse = scanner.nextLine();
        System.out.println("Entrer le telephone de l'agence : ");
        String telephone = scanner.nextLine();

        Agence agence = new Agence();
        agence.setCode(code);
        agence.setNom(nom);
        agence.setAdresse(adresse);
        agence.setTelephone(telephone);

        try {
            Agence createdAgence = serviceAgence.createAgence(agence);
            System.out.println("L'agence a été créée avec succès : " + createdAgence);
        } catch (AgenceException e) {
            System.out.println("Erreur lors de la création de l'agence : " + e.getMessage());
        }

    }



}
