package org.example.ManagerClasses;

import org.example.Dto.Agence;
import org.example.Dto.Employe;
import org.example.Implementation.ImAgence;
import org.example.Implementation.ImEmploye;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ManagerEmploye {
    private final Scanner scanner;
    private final ImEmploye imEmploye;

    public ManagerEmploye(Scanner scanner) throws SQLException {
        this.scanner = scanner;
        this.imEmploye = new ImEmploye();
    }

    public void startEmploye() throws SQLException {

        ImAgence imAgence = new ImAgence();

        int choix;
        do {
            System.out.println("Menu:");
            System.out.println("1. Ajouter un employe");
            System.out.println("2. Chercher un employe par matricule");
            System.out.println("3. Supprimer un employe");
            System.out.println("4. Afficher la liste employes");
            System.out.println("5. Muter un employe");
            System.out.println("6. Muter un employe");
            System.out.println("7. Muter un employe");
            System.out.println("0. Quitter");
            System.out.print("Entrez votre choix : ");

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    System.out.println("Entrez le nom du employe : ");
                    String nom = scanner.nextLine();

                    System.out.println("Entrez le prénom du employe : ");
                    String prenom = scanner.nextLine();

                    System.out.print("Veuillez entrer la date de Naissance (YYYY-MM-DD) : ");
                    String DateNaissance = scanner.nextLine();

                    System.out.print("Veuillez entrer la date de Recrutement (YYYY-MM-DD) : ");
                    String DateRecrutement = scanner.nextLine();

                    LocalDate dateRecrutement = LocalDate.parse(DateRecrutement, DateTimeFormatter.ISO_LOCAL_DATE);
                    LocalDate dateNaissance = LocalDate.parse(DateNaissance, DateTimeFormatter.ISO_LOCAL_DATE);

                    System.out.println("Entrez la matricule du employe : ");
                    String matricule = scanner.nextLine();

                    System.out.println("Entrez l'adresse email du employe : ");
                    String adresseEmail = scanner.nextLine();

                    System.out.println("Entrez telephone du employe : ");
                    String telephone = scanner.nextLine();

                    System.out.println("Entrez code d' agence : ");
                    String code_agence = scanner.nextLine();


                    Employe employe = new Employe(nom,prenom,dateNaissance,telephone,matricule,adresseEmail,dateRecrutement);

                    employe.setNom(nom);
                    employe.setPrenom(prenom);
                    employe.setAdresseEmail(adresseEmail);
                    employe.setMatricule(matricule);
                    employe.setTelephone(telephone);
                    employe.setDateNaissance(dateNaissance);
                    employe.setDateRecrutement(dateRecrutement);
                    Agence agence = new Agence();
                    agence.setCode(code_agence);
                    employe.setAgence(agence);

                    imEmploye.save(employe);

                    System.out.println("Employe ajouté avec succès !");
                    break;
                case 2:
                    System.out.println("Donner matricule du employe rechercher : ");
                    String matriculeRecherche = scanner.nextLine();
                    Optional<Employe> employeTrouve = imEmploye.findByMatricule(matriculeRecherche);

                    if (employeTrouve.isPresent()) {
                        employe = employeTrouve.get();
                        System.out.println("Client trouvé : ");
                        System.out.println("Nom : " + employe.getNom());
                        System.out.println("Prenom : " + employe.getPrenom());
                        System.out.println("Date de Naissance : " + employe.getDateNaissance());
                        System.out.println("Adresse : " + employe.getAdresseEmail());
                        System.out.println("Date Recrutement : " + employe.getDateRecrutement());
                    } else {
                        System.out.println("Aucun client trouvé avec le code : " + matriculeRecherche);
                    }
                    break;
                case 3:
                    String matriculeASupprimer;
                    do {
                        System.out.println("Entrez le matricule de l'employé à supprimer : ");
                        matriculeASupprimer = scanner.nextLine();
                    } while (matriculeASupprimer.isEmpty()); // Continue à demander tant que l'entrée est vide

                    boolean deletedEmploye = imEmploye.deleteEmploye(matriculeASupprimer);
                    if (deletedEmploye) {
                        System.out.println("Employé supprimé avec succès.");
                    } else {
                        System.out.println("Aucun employé avec le matricule " + matriculeASupprimer + " n'a été trouvé.");
                    }
                    break;
                case 4:

                    Optional<List<Employe>> clientsOptional = imEmploye.readAllEmployes();

                    if (clientsOptional.isPresent()) {
                        List<Employe> employes = clientsOptional.get();
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

                    break;
                case 5:
                    System.out.print("Entrez le matricule du employe à mettre à jour : ");
                    String matricule_1 = scanner.nextLine();

                    Optional<Employe> employeOptional = imEmploye.findByMatricule(matricule_1);
                     System.out.println("employeOptional" + employeOptional);
                    if (!employeOptional.isPresent()) {
                        System.out.println("Aucun employe trouvé avec le matricule saisi.");
                        return;
                    }

                    Employe employeToUpdate = employeOptional.get();
                    System.out.println("Employe trouvé : " + employeToUpdate);

                    System.out.println("Saisissez les nouvelles valeurs (entrez '-' pour laisser inchangé) :");

                    System.out.print("Nouveau nom : ");
                    String newNom = scanner.nextLine();
                    if (!newNom.equals("-")) {
                        employeToUpdate.setNom(newNom);
                    }

                    System.out.print("Nouveau prénom : ");
                    String newPrenom = scanner.nextLine();
                    if (!newPrenom.equals("-")) {
                        employeToUpdate.setPrenom(newPrenom);
                    }

                    System.out.print("Nouvelle date de naissance (YYYY-MM-DD) : ");
                    String newDateNaissanceStr = scanner.nextLine();
                    if (!newDateNaissanceStr.equals("-")) {
                        LocalDate newDateNaissance = LocalDate.parse(newDateNaissanceStr, DateTimeFormatter.ISO_LOCAL_DATE);
                        employeToUpdate.setDateNaissance(newDateNaissance);
                    }

                    System.out.print("Nouvelle date de recrutement (YYYY-MM-DD) : ");
                    String newDateRecrutementStr = scanner.nextLine();
                    if (!newDateNaissanceStr.equals("-")) {
                        LocalDate newDateRecrutement = LocalDate.parse(newDateRecrutementStr, DateTimeFormatter.ISO_LOCAL_DATE);
                        employeToUpdate.setDateRecrutement(newDateRecrutement);
                    }


                    System.out.print("Nouvelle adresse email : ");
                    String newAdresse = scanner.nextLine();
                    if (!newAdresse.equals("-")) {
                        employeToUpdate.setAdresseEmail(newAdresse);
                    }

                    boolean updated = imEmploye.update(employeToUpdate);

                    System.out.println("updateu" + updated);
                    System.out.println("getDateRecrutement" + employeToUpdate.getDateRecrutement());

                    if (updated) {
                        System.out.println("Mise à jour réussie.");
                    } else {
                        System.out.println("Échec de la mise à jour.");
                    }
                    break;
                case 6:
                    System.out.println("Donner le mot cle du employe rechercher : ");
                    String cleRecherche = scanner.nextLine();
                    Optional<Employe> employeTrouver = imEmploye.findByEmailOrNomOrPrenom(cleRecherche);

                    if (employeTrouver.isPresent()) {
                        employe = employeTrouver.get();
                        System.out.println("Emloye trouvé : ");
                        System.out.println(employe.toString());
                    } else {
                        System.out.println("Aucun client trouvé avec le code : " + cleRecherche);
                    }
                    break;
                case 7:
                    System.out.println("Matricule de l'employe qui va muter : ");
                    String matriculeMuter = scanner.nextLine();

                    System.out.println("Code de la nouvelle agence : ");
                    String codeAgence = scanner.nextLine();

                    Optional<Employe> employeOptional1 = imEmploye.findByMatricule(matriculeMuter);

                    if (employeOptional1.isPresent()) {
                        Employe employe1 = employeOptional1.get();

                        Optional<Agence> agenceOptional = imAgence.findByCode(codeAgence);

                        if (agenceOptional.isPresent()) {
                            Agence nouvelleAgence = agenceOptional.get();

                            employe1.setAgence(nouvelleAgence);

                            Optional<Employe> employeModifie = imEmploye.changeAgence(employe1, codeAgence);

                            if (employeModifie.isPresent()) {
                                System.out.println("L'employé a été affecté à la nouvelle agence avec succès.");
                            } else {
                                System.out.println("Une erreur s'est produite lors de la modification de l'affectation de l'employé.");
                            }
                        } else {
                            System.out.println("La nouvelle agence avec le code '" + codeAgence + "' n'a pas été trouvée.");
                        }
                    } else {
                        System.out.println("L'employé avec le matricule '" + matriculeMuter + "' n'a pas été trouvé.");
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
