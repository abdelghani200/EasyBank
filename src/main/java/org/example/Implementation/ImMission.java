package org.example.Implementation;

import org.example.Db.DatabaseConnection;
import org.example.Dto.Mission;
import org.example.Dto.Operation;
import org.example.Dto.TypeOperation;
import org.example.Interface.IMission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImMission implements IMission {

    Connection connection;

    public ImMission() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }

    @Override
    public Optional<Mission> save(Mission mission) {

        String MissionQuery = "INSERT INTO gestion_bancaire.mission (code, nom, description) VALUES (?, ?, ?)";

        try {
            PreparedStatement MissionPreparedStatement = connection.prepareStatement(MissionQuery);

            MissionPreparedStatement.setString(1, mission.getCode());
            MissionPreparedStatement.setString(2, mission.getNom());
            MissionPreparedStatement.setString(3, mission.getDescription());

            int rowsMissionInserted = MissionPreparedStatement.executeUpdate();

            if (rowsMissionInserted == 1){
                return Optional.of(mission);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Mission> delete(Mission mission) {

        try {
            String deletemission = "delete from gestion_bancaire.mission where code = ?";

            PreparedStatement statement = connection.prepareStatement(deletemission);

            statement.setString(1, mission.getCode());

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0){
                return Optional.of(mission);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Mission> readMission() {
        List<Mission> missions = new ArrayList<>();

        try {
            String selectMission = "SELECT * FROM gestion_bancaire.mission ";
            PreparedStatement statement = connection.prepareStatement(selectMission);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String code = resultSet.getString("code");
                String nom = resultSet.getString("nom");
                String description = resultSet.getString("description");

                Mission mission = new Mission(0,code, nom, description);
                missions.add(mission);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return missions;
    }

    @Override
    public Optional<Mission> findMissionByCode(String code) {

        String checkCompteQuery = "SELECT * FROM gestion_bancaire.mission WHERE code = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(checkCompteQuery)) {
            // Définir le paramètre de la requête avec le numéro de compte
            preparedStatement.setString(1, code);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Vérifier si un compte correspondant a été trouvé
                if (resultSet.next()) {

                    String description = resultSet.getString("description");
                    String nom = resultSet.getString("nom");

                    Mission mission = new Mission(0,code,description,nom);
                    mission.setCode(code);
                    mission.setNom(nom);
                    mission.setDescription(description);

                    return Optional.of(mission);
                } else {

                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du compte par numéro : " + e.getMessage(), e);
        }
    }
    @Override
    public Optional<Mission> update(Mission mission) {
        String updateMissionQuery = "UPDATE gestion_bancaire.mission SET nom = ?, description = ? WHERE code = ?";

        try {
            PreparedStatement updateMissionStatement = connection.prepareStatement(updateMissionQuery);

            updateMissionStatement.setString(1, mission.getNom());
            updateMissionStatement.setString(2, mission.getDescription());
            updateMissionStatement.setString(3, mission.getCode());

            int rowsUpdated = updateMissionStatement.executeUpdate();

            if (rowsUpdated == 1) {
                return Optional.of(mission);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la mission : " + e.getMessage(), e);
        }

        return Optional.empty();
    }




}
