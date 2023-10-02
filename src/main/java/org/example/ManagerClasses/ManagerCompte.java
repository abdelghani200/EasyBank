package org.example.ManagerClasses;

import org.example.Dto.*;
import org.example.Implementation.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ManagerCompte {
    private final Scanner scanner;
    private final ImCompte<Compte> imCompte;


    public ManagerCompte(Scanner scanner) throws SQLException {
        this.scanner = scanner;
        this.imCompte = new ImCompte<>();
    }

    private static final Map<String, TypeCompte> typeCompteMap = new HashMap<>();
    static {
        typeCompteMap.put("Active", TypeCompte.Active);
        typeCompteMap.put("Closed", TypeCompte.Closed);
        typeCompteMap.put("Blocked", TypeCompte.Blocked);
    }

    public void startEmploye() throws SQLException {

        ImCompeCourant imCompeCourant = new ImCompeCourant();
        ImCompteEpargne imCompteEpargne = new ImCompteEpargne();
        ImEmploye imEmploye = new ImEmploye();
        ImClient imClient = new ImClient();


        int choix;
        do {
            System.out.println("Menu:");
            System.out.println("1. Ajouter un compte");
            System.out.println("2. Chercher un compte par client");
            System.out.println("3. Supprimer un compte");
            System.out.println("4. Changer status un compte");
            System.out.println("5. Modifier un compte");
            System.out.println("6. Affiche la liste des comptes par status");
            System.out.println("7. Affiche la liste des comptes par date de creation");
            System.out.println("0. Quitter");
            System.out.print("Entrez votre choix : ");

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    System.out.println("Entrez le statut du compte (Active, Closed, Blocked) : ");
                    String statusInput = scanner.nextLine().trim();
                    TypeCompte status = typeCompteMap.getOrDefault(statusInput, TypeCompte.Active);

                    System.out.println("Entrez le numéro du compte : ");
                    String numero = scanner.nextLine();

                    System.out.println("Entrez le solde du compte : ");
                    double solde = scanner.nextDouble();

                    System.out.println("Entrez le jour de création du compte : ");
                    int jour = scanner.nextInt();

                    System.out.println("Entrez le mois de création du compte : ");
                    int mois = scanner.nextInt();

                    System.out.println("Entrez l'année de création du compte : ");
                    int annee = scanner.nextInt();

                    System.out.println("Voici le liste des employes : ");
                    Optional<List<Employe>> clientsOptional1 = imEmploye.readAllEmployes();

                    if (clientsOptional1.isPresent()) {
                        List<Employe> employes = clientsOptional1.get();
                        if (employes.isEmpty()) {
                            System.out.println("Aucun employe trouvé dans la base de données.");
                        } else {
                            System.out.println("Liste des employes : ");
                            for (Employe employelist : employes) {
                                System.out.println(employelist.toString());
                                System.out.println("------------------------");
                            }
                        }
                    } else {
                        System.out.println("Aucun employe trouvé dans la base de données.");
                    }
                    System.out.println("Entrez le id du l'employé : ");
                    int client_id = scanner.nextInt();

                    Optional<List<Client>> clientsOptional = imClient.readAll();

                    if (clientsOptional.isPresent()) {
                        List<Client> clients = clientsOptional.get();
                        if (clients.isEmpty()) {
                            System.out.println("Aucun client trouvé dans la base de données.");
                        } else {
                            System.out.println("Liste des clients : ");
                            for (Client client1 : clients) {
                                System.out.println(client1.toString());
                                System.out.println("------------------------");
                            }
                        }
                    } else {
                        System.out.println("Aucun client trouvé dans la base de données.");
                    }
                    System.out.println("Entrez le id de client : ");
                    int employe_id = scanner.nextInt();

                    LocalDate dateCreation = LocalDate.of(annee, mois, jour);

                    System.out.println("Quel type de compte voulez-vous saisir (Courant ou Epargne) :");
                    scanner.nextLine();
                    String typecompte = scanner.nextLine();

                    Client client = new Client(client_id, "", "", "", null, "", "");
                    Employe employe = new Employe(employe_id, "", "", "", null, "", null, "");


                    if ("Courant".equalsIgnoreCase(typecompte)) {
                        System.out.println("Entrez le découvert du compte : ");
                        double decouvert = scanner.nextDouble();


                        CompteCourant compteCourant = new CompteCourant(0,numero, solde,dateCreation, status, decouvert );
                        compteCourant.setClient(client);
                        compteCourant.setEmploye(employe);
                        imCompeCourant.save(compteCourant);

                        System.out.println("Compte Courant ajouté avec succès !");
                    } else if ("Epargne".equalsIgnoreCase(typecompte)) {
                        System.out.println("Entrez le taux d'intérêt du compte : ");
                        double tauxInteret = scanner.nextDouble();

                        CompteEpargne compteEpargne = new CompteEpargne(0,numero, solde, dateCreation, status, tauxInteret);
                        compteEpargne.setClient(client);
                        compteEpargne.setEmploye(employe);
                        imCompteEpargne.save(compteEpargne);

                        System.out.println("Compte Epargne ajouté avec succès !");
                    } else {
                        System.out.println("Type de compte invalide. Veuillez saisir 'Courant' ou 'Epargne'.");
                    }
                    break;
                case 2:
                    System.out.println("Entrez le nom du client pour lequel vous souhaitez chercher les comptes : ");
                    String nomClientToSearch = scanner.nextLine().trim();
                    Client clientToSearch = new Client();
                    clientToSearch.setNom(nomClientToSearch);

                    List<Compte> comptes = imCompte.findByClient(clientToSearch);

                    if (comptes.isEmpty()) {
                        System.out.println("Aucun compte trouvé pour le client avec le nom : " + nomClientToSearch);
                    } else {
                        System.out.println("Comptes du client avec le nom '" + nomClientToSearch + "' :");
                        for (Compte compte : comptes) {
                            System.out.println(compte);
                        }
                    }
                    break;
                case 3:
                    String numeroASupprimer;
                    System.out.println("Entrez le numero du compte à supprimer : ");
                    numeroASupprimer = scanner.nextLine();
                    Optional<Compte> deletedClient = imCompte.deleteCompte(numeroASupprimer);

                        System.out.println("Compte supprimé avec succès");

                    break;
                case 4:


                    System.out.println("Entrez le numéro de compte : ");
                    String numeroCompte = scanner.nextLine();
                    scanner.nextLine();

                    System.out.println("Choisissez le nouveau statut (Active, Blocked, Closed) : ");
                    TypeCompte nouveauStatut = TypeCompte.valueOf(scanner.nextLine());

                    Compte compte = new CompteCourant();
                    compte.setNumero(numeroCompte);
                    compte.setStatus(nouveauStatut);

                    boolean statusChanged = imCompte.changeCompteStatus(compte);

                    if (statusChanged) {
                        System.out.println("Le statut du compte a été mis à jour avec succès.");
                    } else {
                        System.out.println("Erreur lors de la mise à jour du statut du compte.");
                    }
                    break;
                case 5:
                    System.out.println("Entrer le numéro de compte à modifier :");
                    String num = scanner.nextLine();
                    scanner.nextLine();

                    System.out.println("Entrer le nouveau solde :");
                    Double soldeToUpdate = scanner.nextDouble();
                    scanner.nextLine();

                    System.out.println("Entrer la nouvelle Date (AAAA-MM-JJ) :");
                    String dateStr = scanner.nextLine();
                    LocalDate dateToUpdate = LocalDate.parse(dateStr);

                    //Compte compteToUpdate = new Compte(0,num, soldeToUpdate, dateToUpdate,null);
                    Compte compteToUpdate = new CompteCourant();
                    compteToUpdate.setNumero(num);
                    compteToUpdate.setSolde(soldeToUpdate);
                    compteToUpdate.setDateCreation(dateToUpdate);

                    Optional<Compte> updatedCompte = imCompte.update(compteToUpdate);
                     System.out.println(compteToUpdate.getNumero());
                    System.out.println(compteToUpdate.getSolde());
                    if (updatedCompte.isPresent()) {
                        System.out.println("Le compte a été mis à jour avec succès.");
                    } else {
                        System.out.println("La mise à jour du compte a échoué.");
                    }

                    break;
                case 6:

                    Map<TypeCompte, List<Compte>> compteInfoMap = imCompte.findByStatus();

                    for (Map.Entry<TypeCompte, List<Compte>> entry : compteInfoMap.entrySet()) {
                        TypeCompte statusTofind = entry.getKey();
                        List<Compte> comptesTofind = entry.getValue();

                        System.out.println("Statut : " + statusTofind);

                        for (Compte compteTofind : comptesTofind) {
                            System.out.println(compteTofind.toString());
                        }
                    }
                    break;

                case 7:
                    Map<LocalDate, List<Compte>> compteInfoMapCreation = imCompte.findByDateCreation();

                    for (Map.Entry<LocalDate, List<Compte>> entry : compteInfoMapCreation.entrySet()) {
                        LocalDate dateCreationfind = entry.getKey();
                        List<Compte> comptesfind = entry.getValue();

                        System.out.println("Date de création : " + dateCreationfind);

                        for (Compte compte1 : comptesfind) {
                            System.out.println(compte1.toString());
                        }
                    }
                    break;
                case 8:
                    System.out.println("Recherche d'un compte par numéro d'opération");
                    System.out.print("Veuillez entrer le numéro de l'opération : ");
                    String operationNumero = scanner.next();

                    Map<TypeCompte, List<Compte>> compteInfoMap1 = imCompte.findCompteByOperationNumero(operationNumero);

                    if (compteInfoMap1.isEmpty()) {
                        System.out.println("Aucun compte trouvé pour le numéro d'opération : " + operationNumero);
                    } else {
                        for (Map.Entry<TypeCompte, List<Compte>> entry : compteInfoMap1.entrySet()) {
                            List<Compte> comptes12 = entry.getValue();

                            System.out.println("Comptes pour le numero d'operation : " + operationNumero);
                            for (Compte compte12 : comptes12) {
                                System.out.println("Numéro de compte : " + compte12.toString());
                            }
                        }
                    }
                    break;
                case 0:
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        } while (choix != 0);

    }
}
