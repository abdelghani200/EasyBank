package org.example.ManagerClasses;

import org.example.Dto.*;
import org.example.Implementation.ImClient;
import org.example.Services.ServiceCredit;
import org.example.Services.ServiceSimulation;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ManagerCredit {
    private final Scanner scanner;
    private final ServiceCredit serviceCredit;
    private final ImClient imClient;
    private final ServiceSimulation serviceSimulation;

    public ManagerCredit(Scanner scanner, ServiceCredit serviceCredit, ImClient imClient, ServiceSimulation serviceSimulation) {
        this.scanner = scanner;
        this.serviceCredit = serviceCredit;
        this.imClient = imClient;
        this.serviceSimulation = serviceSimulation;
    }

    public void startCreditMenu() throws SQLException {


        int choix;
        do {
            System.out.println("\nMenu Credit:");
            System.out.println("1. Créer un credit");
            System.out.println("2. Changer etat d'un credit");
            System.out.println("3. find By Etat");
            System.out.println("4. find By Date");
            System.out.println("Entrez votre choix :");

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix){
                case 1:
                    createCredit();
                    break;
                case 2:
                    ChangeEtatCredit();
                    break;
                case 3:
                    findByEtat();
                    break;
                case 4:
                    findByDate();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("choix invalide");
            }
        }while (true);
    }

    public void createCredit() {
        System.out.println("Donnez le code du client : ");
        int idClient = scanner.nextInt();

        // Recherche du client par son code
        Optional<Client> clientOptional = imClient.findById(idClient);

        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();

            System.out.println("Donnez le montant :");
            double montant = scanner.nextDouble();
            scanner.nextLine(); // Pour consommer la nouvelle ligne
            System.out.println("Donnez la date de crédit (format : YYYY-MM-DD) :");
            String dateCreditStr = scanner.nextLine();

            LocalDate dateCredit = LocalDate.parse(dateCreditStr, DateTimeFormatter.ISO_LOCAL_DATE);

            System.out.println("Donnez les remarques :");
            String remarques = scanner.nextLine();

            System.out.println("Entrez le nombre de mois pour les paiements mensuels :");
            int paiementMensuelNum = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Donnez le matricule :");
            String matricule = scanner.nextLine();
            System.out.println("Donnez le paiement capital :");
            Double paiementCapital = scanner.nextDouble();
            System.out.println("Donnez le paiement Mensuel :");
            Double paiementMensuel = scanner.nextDouble();

            System.out.println("Donnez le num :");
            Integer num = scanner.nextInt();


            // Créez un employé avec le matricule (si nécessaire)
            Employe employe = new Employe();
            employe.setMatricule(matricule);

            client.setId(idClient);

            // Créez une simulation avec les détails
            Simulation simulation = new Simulation();
            simulation.setCapitalEmprunte(paiementCapital);
            simulation.setPaiementMensuelNum(paiementMensuelNum);
            simulation.setPaiementMensuel(paiementMensuel);
            simulation.setCapitalEmprunte(montant);
            simulation.setClient(client);

            // Créez un crédit avec les détails
            Credit credit = new Credit();
            credit.setNumero(num);
            credit.setRemarques(remarques);
            credit.setDate(dateCredit);
            credit.setEtat(EtatCredit.Inactive);
            credit.setMontant(paiementCapital);
            credit.setDuree(paiementMensuelNum);
            credit.setSimulation(simulation);

            try {
                Credit createdCredit = serviceCredit.createCredit(credit);
                System.out.println("Le crédit a été créé avec succès : " + createdCredit);
            } catch (Exception e) {
                System.out.println("Erreur lors de la création du crédit : " + e.getMessage());
            }
        } else {
            System.out.println("Aucun client trouvé avec le code : " + idClient);
        }
    }
    public void ChangeEtatCredit() {
        System.out.println("Entrez le numéro de crédit :");
        int numeroCredit = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Entrez le nouvel état du crédit (Active ou Inactive) :");
        String nouvelEtat = scanner.nextLine();

        EtatCredit etatCredit = null;
        if (nouvelEtat.equalsIgnoreCase("Active")) {
            etatCredit = EtatCredit.Active;
        } else if (nouvelEtat.equalsIgnoreCase("Inactive")) {
            etatCredit = EtatCredit.Inactive;
        }

        boolean changementEtatReussi = serviceCredit.changeEtat(numeroCredit, etatCredit);

        if (changementEtatReussi) {
            System.out.println("L'état du crédit a été modifié avec succès.");
        } else {
            System.out.println("Échec de la modification de l'état du crédit.");
        }
    }

    public void findByEtat() {
        System.out.println("Entrez l'état de crédit à rechercher (Active ou Inactive) :");
        String etatStr = scanner.nextLine();

        EtatCredit etatCredit = null;
        if (etatStr.equalsIgnoreCase("Active")) {
            etatCredit = EtatCredit.Active;
        } else if (etatStr.equalsIgnoreCase("Inactive")) {
            etatCredit = EtatCredit.Inactive;
        }

        List<Credit> credits = serviceCredit.findByEtat(etatCredit);

        if (!credits.isEmpty()) {
            System.out.println("Crédits avec l'état " + etatStr + " :");
            for (Credit credit : credits) {
                System.out.println(credit);
            }
        } else {
            System.out.println("Aucun crédit trouvé avec l'état " + etatStr);
        }
    }

    public void findByDate() {
        System.out.println("Entrez la date de crédit à rechercher (format : YYYY-MM-DD) :");
        String dateStr = scanner.nextLine();

        LocalDate dateCredit = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);

        List<Credit> credits = serviceCredit.findByDate(dateCredit);

        if (!credits.isEmpty()){
            System.out.println("Crédits avec la date " + dateStr + " :");
            for (Credit credit : credits) {
                System.out.println(credit);
            }
        } else {
            System.out.println("Aucun crédit trouvé avec la date " + dateStr);
        }
    }



}
