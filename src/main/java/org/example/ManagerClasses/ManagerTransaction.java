package org.example.ManagerClasses;

import org.example.Dto.Compte;
import org.example.Dto.Employe;
import org.example.Dto.Operation;
import org.example.Dto.Transaction;
import org.example.Exception.TransactionException;
import org.example.Implementation.ImCompte;
import org.example.Implementation.ImEmploye;
import org.example.Implementation.ImOperation;
import org.example.Services.ServiceTransaction;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

public class ManagerTransaction {

    private final Scanner scanner;
    private final ServiceTransaction serviceTransaction;
    private final ImOperation imOperation;
    private final Operation operation;
    private final ImEmploye imEmploye;
    private final ImCompte imCompte;

    public ManagerTransaction(Scanner scanner, ServiceTransaction serviceTransaction, ImOperation imOperation, Operation operation, ImEmploye imEmploye, ImCompte imCompte) {
        this.scanner = scanner;
        this.serviceTransaction = serviceTransaction;
        this.imOperation = imOperation;
        this.operation = operation;
        this.imEmploye = imEmploye;
        this.imCompte = imCompte;
    }

    public void startTransaction(){

        int choix;
        do {
            System.out.println("\nMenu Transaction:");
            System.out.println("1. Créer une Transaction");
            System.out.println("2. Supprimer une Transaction");
            System.out.println("Entrez votre choix :");

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix){
                case 1:
                    createTransaction();
                    break;
                case 2:
                    deleteTransaction();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("choix invalide");
            }
        }while (true);
    }

    public void createTransaction(){
        System.out.println("Entrer le matricule :");
        int matricule = scanner.nextInt();
        System.out.println("matricule: " + matricule);

        Optional<Employe> optionalEmploye = imEmploye.findById(matricule);
        System.out.println("Donne le compte source :");
        int source_id = scanner.nextInt();
        System.out.println("source_id: " + source_id);

        Optional<Compte> compteOptionalSource = imCompte.findById(source_id);

        System.out.println("Entrer le montant a transfer :");
        double montant = scanner.nextDouble();

        System.out.println("Donne le compte destinataire :");
        int destinataire_id = scanner.nextInt();
        System.out.println("destinataire_id: " + destinataire_id);

        Optional<Compte> compteOptionalDestinataire = imCompte.findById(destinataire_id);

        operation.setMontant(montant);
        operation.setEmploye(optionalEmploye.get());

        imOperation.saveOperation(operation);

        Transaction transaction = new Transaction("", LocalDate.now(), montant,null);
        transaction.setCompteSource(compteOptionalSource.get());
        transaction.setCompteDestination(compteOptionalDestinataire.get());

        try {
            Transaction createdTransaction = serviceTransaction.createTransaction(transaction, operation);
            if (createdTransaction != null) {
                System.out.println("Transaction réussie.");
            }
        } catch (TransactionException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteTransaction(){
        System.out.println("Entrer l'Id :");
        int id = scanner.nextInt();
        if (serviceTransaction.deleteTransaction(id) > 0){
            System.out.println("Transaction supprimer avec succes.");
        }else{
            System.out.println("Error!!");
        }
    }


}
