package org.example.ManagerClasses;

import org.example.Dto.Operation;
import org.example.Implementation.ImOperation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

public class ManagerOperation {
    private final Scanner scanner;

    public ManagerOperation(Scanner scanner) {
        this.scanner = scanner;
    }

    public void startOperation() throws SQLException {
        ImOperation imOperation = new ImOperation();

        int choix;

        do {
            System.out.println("Menu des opérations");
            System.out.println("1. Ajouter une opération");
            System.out.println("2. Rechercher une opération par numéro");
            System.out.println("3. Supprimer une opération");
            System.out.println("0. Quitter");
            System.out.print("Entrez votre choix : ");

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    System.out.println("Entrer le numéro du compte :");
                    String numeroInput = scanner.nextLine();

                    System.out.println("Quel type d'opération voulez-vous effectuer (Retrait ou Versement) :");
                    String typeOperation = scanner.nextLine();

                    if ("Retrait".equalsIgnoreCase(typeOperation) || "Versement".equalsIgnoreCase(typeOperation)) {
                        System.out.println("Entrez le montant : ");
                        double montant = scanner.nextDouble();
                        scanner.nextLine();

                        Optional<Operation> operationResult = performOperation(typeOperation, numeroInput, montant);
                        if (operationResult.isPresent()) {
                            System.out.println(typeOperation + " effectué avec succès !");
                        } else {
                            System.out.println("L'opération a échoué. Veuillez vérifier les données entrées.");
                        }
                    } else {
                        System.out.println("Type d'opération non valide. Veuillez entrer 'Retrait' ou 'Versement'.");
                    }
                    break;
                case 2:
                    System.out.print("Veuillez entrer le numéro de l'opération : ");
                    String operationNumber = scanner.nextLine();
                    searchAndPrintOperation(operationNumber);
                    break;
                case 3:
                    System.out.print("Veuillez entrer le numéro de l'opération à supprimer : ");
                    String operationToDelete = scanner.nextLine();
                    boolean isOperationDeleted = imOperation.delete(operationToDelete);
                    if (isOperationDeleted) {
                        System.out.println("Opération supprimée avec succès.");
                    } else {
                        System.out.println("Aucune opération correspondante n'a été trouvée ou supprimée.");
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        } while (choix != 0);

    }

    private Optional<Operation> performOperation(String operationType, String accountNumber, double amount) throws SQLException {
        ImOperation imOperation = new ImOperation();
        if ("Retrait".equalsIgnoreCase(operationType)) {
            return imOperation.Retrait(accountNumber, amount);
        } else if ("Versement".equalsIgnoreCase(operationType)) {
            return imOperation.Versement(accountNumber, amount);
        }
        return Optional.empty();
    }

    private void searchAndPrintOperation(String operationNumber) throws SQLException {
        ImOperation imOperation = new ImOperation();
        Optional<Operation> operationOpt = imOperation.findOperationByNumero(operationNumber);
        if (operationOpt.isPresent()) {
            Operation operationFound = operationOpt.get();
            System.out.println("Opération trouvée : " + operationFound.toString());
        } else {
            System.out.println("Aucune opération trouvée avec le numéro : " + operationNumber);
        }
    }
}
