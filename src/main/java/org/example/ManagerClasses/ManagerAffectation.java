package org.example.ManagerClasses;

import org.example.Dto.AffecterEmploye;
import org.example.Dto.Agence;
import org.example.Dto.Employe;
import org.example.Exception.AgenceException;
import org.example.Interface.IAffecterEmploye;
import org.example.Services.ServiceAffectEmp;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ManagerAffectation {

    private final Scanner scanner;
    private final ServiceAffectEmp serviceAffectEmp;

    public ManagerAffectation(Scanner scanner, IAffecterEmploye affecterEmployeService) {
        this.scanner = scanner;
        this.serviceAffectEmp = new ServiceAffectEmp(affecterEmployeService);
    }

    public void startAffectationMenu() {
        int choix;
        do {
            System.out.println("\nMenu Agence:");
            System.out.println("1. Affecter un employe à une agence");
            System.out.println("2. Historiques des affectations");
            System.out.println("Entrez votre choix :");

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix){
                case 1:
                    createAffectation();
                    break;
                case 2:
                    viewHistoriqueAffectations();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("choix invalide");
            }
        }while (true);
    }

    public void createAffectation() {
        System.out.println("Entrer le code d'employe : ");
        int code = scanner.nextInt();
        System.out.println("Entrer le code d'agence : ");
        String code_agence = scanner.next();  // Utilisez scanner.next() pour lire le code d'agence

        Employe employe = new Employe();
        employe.setId(code);
        Agence agence = new Agence();
        agence.setCode(code_agence);

        AffecterEmploye affecterEmploye = new AffecterEmploye();
        affecterEmploye.setEmploye(employe);
        affecterEmploye.setAgence(agence);

        Optional<AffecterEmploye> createdAffectation = serviceAffectEmp.affecterEmploye(affecterEmploye);
        if (createdAffectation.isPresent()) {
            AffecterEmploye result = createdAffectation.get();
            System.out.println("Affectation créée avec succès : " + result);
        } else {
            System.out.println("Erreur : Affectation non créée");
        }
    }


    public void viewHistoriqueAffectations() {
        System.out.println("Entrer le code de l'employe : ");
        int code = scanner.nextInt();

        Employe employe = new Employe();
        employe.setId(code);

        List<AffecterEmploye> historiqueAffectations = serviceAffectEmp.getHistoriqueAffectation(employe);
        if (!historiqueAffectations.isEmpty()) {
            System.out.println("Historique des affectations pour l'employe " + code + ":");
            for (AffecterEmploye affectation : historiqueAffectations) {
                System.out.println(affectation);
            }
        } else {
            System.out.println("Aucun historique d'affectations trouvé pour l'employe " + code);
        }
    }


}
