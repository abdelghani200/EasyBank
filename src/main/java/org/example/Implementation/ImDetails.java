package org.example.Implementation;

import org.example.Db.DatabaseConnection;
import org.example.Dto.Employe;
import org.example.Dto.Mission;
import org.example.Dto.MissionDetails;
import org.example.Interface.IDetailsMission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ImDetails implements IDetailsMission {
    Connection connection;

    public ImDetails() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }

    @Override
    public Optional<MissionDetails> NewAffectation(MissionDetails missionDetails) {

        String query = "Insert into gestion_bancaire.detailsMission (matricule_emp, code_mission, nom, dateDebut, dateFin) values(?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, missionDetails.getEmploye().getId());
            preparedStatement.setInt(2, missionDetails.getMission().getId());
            preparedStatement.setString(3, missionDetails.getNom());
            preparedStatement.setObject(4, missionDetails.getDateDebut());
            preparedStatement.setObject(5, missionDetails.getDateFin());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 1) {
                return Optional.of(missionDetails);
            }

        } catch (SQLException e) {
            System.out.println("Une erreur SQL s'est produite : " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<MissionDetails> deleteAffecte(MissionDetails mission) {

        try {
            String deletemission = "delete from gestion_bancaire.detailsmission where nom = ?";

            PreparedStatement statement = connection.prepareStatement(deletemission);

            statement.setString(1, mission.getNom());

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0){
                return Optional.of(mission);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            System.out.println("Une erreur SQL s'est produite : " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Map<Employe, List<MissionDetails>> getHistoriqueAffectations() {
        Map<Employe, List<MissionDetails>> historiqueAffectationsMap = new HashMap<>();

        try {
            String query = "SELECT e.id AS employe_id, m.code AS mission_code, m.nom AS mission_nom, m.description AS mission_description, " +
                    "e.matricule, e.daterecrutement, e.adressemail, p.nom AS personne_nom, p.prenom, p.datenaissance, " +
                    "dm.nom AS affectation_nom, dm.datedebut, dm.datefin " +
                    "FROM gestion_bancaire.detailsmission dm " +
                    "INNER JOIN gestion_bancaire.employe e ON dm.matricule_emp = e.id " +
                    "INNER JOIN gestion_bancaire.personne p ON e.personne_id = p.id " +
                    "INNER JOIN gestion_bancaire.mission m ON dm.code_mission = m.id";


            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Employe employe = new Employe();
                employe.setId(resultSet.getInt("employe_id"));

                employe.setMatricule(resultSet.getString("matricule"));
                employe.setDateRecrutement(resultSet.getDate("daterecrutement").toLocalDate());
                employe.setAdresseEmail(resultSet.getString("adressemail"));
                employe.setNom(resultSet.getString("personne_nom"));
                employe.setPrenom(resultSet.getString("prenom"));
                employe.setDateNaissance(resultSet.getDate("datenaissance").toLocalDate());

                Mission mission = new Mission();

                mission.setCode(resultSet.getString("mission_code"));  // Utilisez "mission_code" au lieu de "code"
                mission.setNom(resultSet.getString("mission_nom"));
                mission.setDescription(resultSet.getString("mission_description"));


                MissionDetails missionDetails = new MissionDetails(
                        employe,
                        mission,
                        resultSet.getString("affectation_nom"),
                        resultSet.getDate("datedebut").toLocalDate(),
                        resultSet.getDate("datefin").toLocalDate()
                );

                historiqueAffectationsMap.computeIfAbsent(employe, k -> new ArrayList<>()).add(missionDetails);
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            System.out.println("Une erreur SQL s'est produite : " + e.getMessage());
        }

        return historiqueAffectationsMap;
    }



    @Override
    public int getAffectationsStatistics(){
        int totalAffectations = 0;

        String query = "SELECT COUNT(*) AS total FROM gestion_bancaire.detailsMission";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                totalAffectations = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Une erreur SQL s'est produite : " + e.getMessage());
        }

        return totalAffectations;
    }




}
