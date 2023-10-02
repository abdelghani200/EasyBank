package org.example.ManagerClasses;

import org.example.Dto.Employe;
import org.example.Dto.Mission;
import org.example.Dto.MissionDetails;
import org.example.Implementation.ImDetails;
import org.example.Implementation.ImMission;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ManagerMission {
    private final Scanner scanner;

    public ManagerMission(Scanner scanner) {
        this.scanner = scanner;
    }

    public void startMission() throws SQLException {
        ImMission imMission = new ImMission();
        ImDetails imDetails = new ImDetails();

        int choix;
        do {
            System.out.println("Menu :");
            System.out.println("1. Ajouter une mission");
            System.out.println("2. Chercher une mission par code");
            System.out.println("3. Supprimer une mission");
            System.out.println("4. Afficher les missions");
            System.out.println("5. Ajouter une affectation");
            System.out.println("6. Supprimer une affectation");
            System.out.println("7. Afficher l'historique des affectations");
            System.out.println("0. Quitter");
            System.out.print("Entrez votre choix : ");

            choix = scanner.nextInt();
            scanner.nextLine(); // Pour consommer la nouvelle ligne laissée par nextInt

            switch (choix) {
                case 1:
                    addMission(imMission);
                    break;
                case 2:
                    searchAndPrintMission(imMission);
                    break;
                case 3:
                    deleteMission(imMission);
                    break;
                case 4:
                    printMissions(imMission);
                    break;
                case 5:
                    addAffectation(imDetails);
                    break;
                case 6:
                    deleteAffectation(imDetails);
                    break;
                case 7:
                    showAffectationHistory(imDetails);
                    break;
                case 0:
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        } while (choix != 0);

    }

    private void addMission(ImMission imMission) {
        System.out.println("Entrez le code de la mission : ");
        String code = scanner.nextLine();

        System.out.println("Entrez le nom de la mission : ");
        String nom = scanner.nextLine();

        System.out.println("Entrez la description de la mission : ");
        String description = scanner.nextLine();

        Mission mission = new Mission(0, code, nom, description);

        imMission.save(mission);

        System.out.println("Mission ajoutée avec succès !");
    }

    private void searchAndPrintMission(ImMission imMission) {
        System.out.print("Veuillez entrer le code de la mission : ");
        String missionCode = scanner.nextLine();

        Optional<Mission> missionOpt = imMission.findMissionByCode(missionCode);

        if (missionOpt.isPresent()) {
            Mission missionFound = missionOpt.get();
            System.out.println("Mission trouvée : " + missionFound.toString());
        } else {
            System.out.println("Aucune mission trouvée avec ce code : " + missionCode);
        }
    }

    private void deleteMission(ImMission imMission) {
        System.out.print("Entrez le code de la mission à supprimer : ");
        String missionCodeToDelete = scanner.nextLine();
        Mission missionToDelete = new Mission(0, missionCodeToDelete, "", "");

        Optional<Mission> deletedMission = imMission.delete(missionToDelete);

        if (deletedMission.isPresent()) {
            System.out.println("Mission supprimée avec succès !");
        } else {
            System.out.println("La mission n'a pas été trouvée ou une erreur s'est produite lors de la suppression.");
        }
    }

    private void printMissions(ImMission imMission) {
        List<Mission> missions = imMission.readMission();

        if (!missions.isEmpty()) {
            System.out.println("Liste des missions :");
            for (Mission mission : missions) {
                System.out.println(mission.toString());
            }
        } else {
            System.out.println("Aucune mission n'a été trouvée.");
        }
    }

    private void addAffectation(ImDetails imDetails) {
        System.out.print("Veuillez entrer le matricule de l'employé : ");
        int id_emp = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Veuillez entrer le code de la mission : ");
        int id_Mission = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Veuillez entrer le nom d'affectation : ");
        String nomAffectation = scanner.nextLine();

        System.out.print("Veuillez entrer la date de début (YYYY-MM-DD) : ");
        String datedebut = scanner.nextLine();

        System.out.print("Veuillez entrer la date de fin (YYYY-MM-DD) : ");
        String datefin = scanner.nextLine();

        LocalDate datedebut1 = LocalDate.parse(datedebut, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate datefin1 = LocalDate.parse(datefin, DateTimeFormatter.ISO_LOCAL_DATE);

        MissionDetails missionDetails = new MissionDetails(null, null, nomAffectation, datedebut1, datefin1);
        missionDetails.setEmploye(new Employe(id_emp, "", "", null, null, "", null, null));
        missionDetails.setMission(new Mission(id_Mission, "", "", ""));

        Optional<MissionDetails> result = imDetails.NewAffectation(missionDetails);

        if (result.isPresent()) {
            System.out.println("Affectation ajoutée avec succès !");
        } else {
            System.out.println("Échec de l'insertion.");
        }
    }

    private void deleteAffectation(ImDetails imDetails) {
        System.out.println("Entrez le nom de l'affectation à supprimer : ");
        String nomAffectationToDelete = scanner.nextLine();
        MissionDetails missionDetailsToDelete = new MissionDetails(null, null, nomAffectationToDelete, null, null);

        Optional<MissionDetails> deleteMissionDetails = imDetails.deleteAffecte(missionDetailsToDelete);

        if (deleteMissionDetails.isPresent()) {
            System.out.println("Affectation supprimée avec succès !");
        } else {
            System.out.println("L'affectation n'a pas été trouvée.");
        }
    }

    private void showAffectationHistory(ImDetails imDetails) {
        Map<Employe, List<MissionDetails>> historiqueAffectationsMap = imDetails.getHistoriqueAffectations();
        Map<Employe, Integer> nombreAffectationsMap = new HashMap<>();

        // Calculer le nombre total d'affectations pour chaque employé
        for (Map.Entry<Employe, List<MissionDetails>> entry : historiqueAffectationsMap.entrySet()) {
            Employe employe = entry.getKey();
            List<MissionDetails> affectations = entry.getValue();
            int nombreAffectations = affectations.size();
            nombreAffectationsMap.put(employe, nombreAffectations);
        }

        for (Map.Entry<Employe, List<MissionDetails>> entry : historiqueAffectationsMap.entrySet()) {
            Employe employe = entry.getKey();
            List<MissionDetails> affectations = entry.getValue();

            // Afficher les affectations de cet employé
            for (MissionDetails missionDetails : affectations) {
                System.out.println("MissionDetails: " + missionDetails);
            }

            // Afficher le nombre total d'affectations pour cet employé
            int nombreTotalAffectations = nombreAffectationsMap.get(employe);
            System.out.println("Nombre d'affectations pour cet employe : " + nombreTotalAffectations);

            // Ajouter une séparation entre les employés
            System.out.println("----------------------");
        }
    }




}
