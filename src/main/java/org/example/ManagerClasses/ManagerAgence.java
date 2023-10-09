package org.example.ManagerClasses;

import org.example.Dto.Agence;
import org.example.Exception.AgenceException;
import org.example.Implementation.ImAgence;
import org.example.Services.ServiceAgence;

import java.util.Optional;
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
            System.out.println("3. Delete une agence");
            System.out.println("4. Update une agence");
            System.out.println("5. Recherche une agence avec code");
            System.out.println("6. Recherche une agence avec adresse");
            System.out.println("Entrez votre choix :");

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix){
                case 1:
                    createAgence();
                    break;
                case 3:
                    deleteAgence();
                    break;
                case 4:
                    updateAgence();
                    break;
                case 5:
                    findAgenceByCode();
                    break;
                case 6:
                    findAgenceByAdresse();
                    break;
                case 0:
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

    public void deleteAgence() {
        System.out.println("Entrer le code de l'agence que vous souhaitez supprimer :");
        String code = scanner.nextLine();
        try {
            serviceAgence.deleteAgence(code);
            System.out.println("L'agence a été supprimée avec succès.");
        } catch (AgenceException e) {
            System.out.println("Erreur lors de la suppression de l'agence : " + e.getMessage());
        }
    }

    public void updateAgence() {
        System.out.println("Entrer le code de l'agence que vous souhaitez modifier :");
        String code = scanner.nextLine();

        Optional<Agence> optionalAgence = serviceAgence.findAgenceByCode(code);

        if (optionalAgence.isPresent()) {
            Agence agence = optionalAgence.get();

            System.out.println("Nom actuel de l'agence : " + agence.getNom());
            System.out.println("Entrez le nouveau nom : ");
            String newName = scanner.nextLine();
            System.out.println("Adresse actuel de l'agence : " + agence.getAdresse());
            System.out.println("Entrez le nouveau adresse : ");
            String newAdresse = scanner.nextLine();
            System.out.println("Telephone actuel de l'agence : " + agence.getTelephone());
            System.out.println("Entrez le nouveau telephone : ");
            String newTelephone = scanner.nextLine();

            agence.setNom(newName);
            agence.setAdresse(newAdresse);
            agence.setTelephone(newTelephone);

            try {
                serviceAgence.updateAgence(agence);
                //System.out.println("Agence mise à jour avec succès.");
            } catch (Exception e) {
                System.out.println("Erreur lors de la mise à jour de l'agence : " + e.getMessage());
            }
        } else {
            System.out.println("Agence introuvable.");
        }
    }

    public void findAgenceByCode() {
        System.out.println("Entrer le code de l'agence que vous souhaitez rechercher :");
        String code =scanner.nextLine();

        Optional<Agence> agence = serviceAgence.findAgenceByCode(code);
        if (agence.isPresent()){
            System.out.println("Agence trouve: "+ agence.get());
        } else {
            System.out.println("Aucune agence trouvee");
        }
    }

    public void findAgenceByAdresse() {
        System.out.println("Entrer l'adresse de l'agence que vous souhaitez rechercher :");
        String adresse = scanner.nextLine();
        Optional<Agence> agence = serviceAgence.findAgenceByAdresse(adresse);
        if (agence.isPresent()) {
            System.out.println("Agence trouvée : " + agence.get());
        } else {
            System.out.println("Aucune agence trouvee.");
        }
    }



}
