package org.example.ManagerClasses;

import org.example.Db.DatabaseConnection;
import org.example.Dto.Client;
import org.example.Dto.Employe;
import org.example.Dto.Mission;
import org.example.Implementation.ImClient;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ManageClient {
    private final Scanner scanner;
    private final ImClient imClient;

    public ManageClient(Scanner scanner) throws SQLException {
        this.scanner = scanner;
        this.imClient = new ImClient();
    }

    public void start() throws SQLException {

        int choix;
        do {
            System.out.println("Menu:");
            System.out.println("1. Ajouter un client");
            System.out.println("2. Chercher un client par code");
            System.out.println("3. Supprimer un client");
            System.out.println("4. Afficher un client");
            System.out.println("5. Modifier un client");
            System.out.println("6. Chercher un client par nom ou prenom ou email");
            System.out.println("8. retourn");
            System.out.println("0. Quitter");
            System.out.print("Entrez votre choix : ");

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    System.out.println("Entrez le nom du client : ");
                    String nom = scanner.nextLine();

                    System.out.println("Entrez le prénom du client : ");
                    String prenom = scanner.nextLine();

                    System.out.print("Veuillez entrer la date de naissance (YYYY-MM-DD) : ");
                    String datenc = scanner.nextLine();
                    //scanner.nextLine();

                    System.out.println("Entrez le code du client : ");
                    String code = scanner.nextLine();

                    System.out.println("Entrez le adresse du client : ");
                    String adresse = scanner.nextLine();

                    System.out.println("Entrez le telephone du client : ");
                    String telephone = scanner.nextLine();

                    LocalDate dateNaissance = LocalDate.parse(datenc, DateTimeFormatter.ISO_LOCAL_DATE);


                    Client client = new Client(0,nom,prenom,telephone,dateNaissance,code,adresse);

                    client.setDateNaissance(dateNaissance);

                    imClient.save(client);

                    System.out.println("Client ajouté avec succès !");
                    break;
                case 2:
                    System.out.println("Donner le code du client rechercher : ");
                    String codeRecherche = scanner.nextLine();
                    Optional<Client> clientTrouve = imClient.findByCode(codeRecherche);

                    if (clientTrouve.isPresent()) {
                        client = clientTrouve.get();
                        System.out.println("Client trouvé : ");
                        System.out.println("Nom : " + client.getNom());
                        System.out.println("Prenom : " + client.getPrenom());
                        System.out.println("Date de Naissance : " + client.getDateNaissance());
                        System.out.println("Adresse : " + client.getAdresse());
                    } else {
                        System.out.println("Aucun client trouvé avec le code : " + codeRecherche);
                    }

                    break;
                case 3:
                    String codeASupprimer;
                    do {
                        System.out.println("Entrez le code du client à supprimer : ");
                        codeASupprimer = scanner.nextLine();
                    } while (codeASupprimer.isEmpty()); // Continue à demander tant que l'entrée est vide

                    boolean deletedClient = imClient.deleteClient(codeASupprimer);
                    if (deletedClient) {
                        System.out.println("Client supprimé avec succès.");
                    } else {
                        System.out.println("Aucun client avec le code " + codeASupprimer + " n'a été trouvé.");
                    }
                    break;
                case 4:
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
                    break;
                case 5:
                    System.out.print("Entrez le code du client à mettre à jour : ");
                    String code_1 = scanner.nextLine();

                    Optional<Client> clientOptional = imClient.findByCode(code_1);

                    if (!clientOptional.isPresent()) {
                        System.out.println("Aucun client trouvé avec le code saisi.");
                        return;
                    }

                    Client clientToUpdate = clientOptional.get();
                    System.out.println("Client trouvé : " + clientToUpdate);

                    System.out.println("Saisissez les nouvelles valeurs (entrez '-' pour laisser inchangé) :");

                    System.out.print("Nouveau nom : ");
                    String newNom = scanner.nextLine();
                    if (!newNom.equals("-")) {
                        clientToUpdate.setNom(newNom);
                    }

                    System.out.print("Nouveau prénom : ");
                    String newPrenom = scanner.nextLine();
                    if (!newPrenom.equals("-")) {
                        clientToUpdate.setPrenom(newPrenom);
                    }

                    System.out.print("Nouvelle date de naissance (YYYY-MM-DD) : ");
                    String newDateNaissanceStr = scanner.nextLine();
                    if (!newDateNaissanceStr.equals("-")) {
                        LocalDate newDateNaissance = LocalDate.parse(newDateNaissanceStr, DateTimeFormatter.ISO_LOCAL_DATE);
                        clientToUpdate.setDateNaissance(newDateNaissance);
                    }

                    System.out.print("Nouvelle adresse : ");
                    String newAdresse = scanner.nextLine();
                    if (!newAdresse.equals("-")) {
                        clientToUpdate.setAdresse(newAdresse);
                    }

                    boolean updated = imClient.update(clientToUpdate);

                    if (updated) {
                        System.out.println("Mise à jour réussie.");
                    } else {
                        System.out.println("Échec de la mise à jour.");
                    }
                    break;
                case 6:
                    System.out.println("Donner le mot cle du client rechercher : ");
                    String cleRecherche = scanner.nextLine();
                    Optional<Client> clientTrouver = imClient.findByEmailOrNomOrPrenom(cleRecherche);

                    if (clientTrouver.isPresent()) {
                        client = clientTrouver.get();
                        System.out.println("Client trouvé : ");
                        System.out.println(client.toString());
                    } else {
                        System.out.println("Aucun client trouvé avec le code : " + cleRecherche);
                    }
                    break;
                case 8:
                    return;
                case 0:
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        } while (choix != 0);

    }




}

