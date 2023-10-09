package org.example;

import org.example.Db.DatabaseConnection;
import org.example.Dto.AffecterEmploye;
import org.example.Dto.Employe;
import org.example.Dto.Operation;
import org.example.Implementation.*;
import org.example.Interface.IAffecterEmploye;
import org.example.Interface.IAgence;
import org.example.Interface.ICredit;
import org.example.Interface.ITransaction;
import org.example.ManagerClasses.*;
import org.example.Services.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        ManageClient clientController = new ManageClient(scanner);
        ManagerEmploye employeManager = new ManagerEmploye(scanner);
        ManagerCompte compteManager = new ManagerCompte(scanner);
        ManagerMission managerMission = new ManagerMission(scanner);
        ManagerOperation managerOperation = new ManagerOperation(scanner);
        ImClient imClientService = new ImClient();
        ImEmploye imEmployeService = new ImEmploye();
        IAgence agenceService = new ImAgence();
        ManagerAgence managerAgence = new ManagerAgence(scanner, new ServiceAgence(agenceService));
        ICredit creditService = new ImCredit();
        ManagerCredit managerCredit = new ManagerCredit(scanner, new ServiceCredit(creditService), imClientService, new ServiceSimulation(imEmployeService));
        ImCompte imCompte = new ImCompte();
        Operation operation = new Operation();
        ImOperation imOperation = new ImOperation();
        ITransaction iTransaction = new ImTransaction();
        ManagerTransaction managerTransaction = new ManagerTransaction(scanner,new ServiceTransaction(iTransaction,imOperation),imOperation,operation,new ImEmploye(),imCompte);
        IAffecterEmploye iAffecterEmploye = new ImAffecterEmploye();
        ManagerAffectation managerAffectation = new ManagerAffectation(scanner,iAffecterEmploye);

        int choix;
        do {
            try {
                // Efface l'écran (peut ne pas fonctionner sur tous les systèmes)
                System.out.print("\033[H\033[2J");

                // Affiche le titre en bleu centré
                System.out.println("\t\t\t\t\t\u001B[34m" + "***********************");
                System.out.println("\t\t\t\t\t*   Gestion Bancaire   *");
                System.out.println("\t\t\t\t\t***********************" + "\u001B[0m");

                System.out.println("\t\t\t\t\tMenu principal:");
                System.out.println("\t\t\t\t\t1. Gérer les clients");
                System.out.println("\t\t\t\t\t2. Gérer les employés");
                System.out.println("\t\t\t\t\t3. Gérer les comptes");
                System.out.println("\t\t\t\t\t4. Gérer les missions");
                System.out.println("\t\t\t\t\t5. Gérer les opérations");
                System.out.println("\t\t\t\t\t6. Gérer les agences");
                System.out.println("\t\t\t\t\t7. Gérer les credits");
                System.out.println("\t\t\t\t\t8. Gérer les transactions");
                System.out.println("\t\t\t\t\t9. Gérer les affectations");
                System.out.println("\t\t\t\t\t0. Quitter");
                System.out.print("Entrez votre choix : ");

                choix = scanner.nextInt();
                scanner.nextLine();

                switch (choix) {
                    case 1:
                        clientController.start();
                        break;
                    case 2:
                        employeManager.startEmploye();
                        break;
                    case 3:
                        compteManager.startEmploye();
                        break;
                    case 4:
                        managerMission.startMission();
                        break;
                    case 5:
                        managerOperation.startOperation();
                        break;
                    case 6:
                        managerAgence.startAgenceMenu();
                        break;
                    case 7:
                        managerCredit.startCreditMenu();
                        break;
                    case 8:
                        managerTransaction.startTransaction();
                        break;
                    case 9:
                        managerAffectation.startAffectationMenu();
                    case 0:
                        System.out.println("Au revoir !");
                        break;
                    default:
                        System.out.println("Choix invalide, veuillez réessayer.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Erreur : Veuillez entrer un nombre valide.");
                scanner.nextLine();
                choix = -1;
            }
        } while (choix != 0);

        scanner.close();
    }
}
