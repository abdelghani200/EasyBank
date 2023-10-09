package org.example.Implementation;

import org.example.Db.DatabaseConnection;
import org.example.Dto.AffecterEmploye;
import org.example.Dto.Agence;
import org.example.Dto.Employe;
import org.example.Interface.IAffecterEmploye;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImAffecterEmploye implements IAffecterEmploye {

    private Connection connection;

    public ImAffecterEmploye() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }
    @Override
    public Optional<AffecterEmploye> affecterEmploye(AffecterEmploye affecterEmploye) {
        String sql = "INSERT INTO gestion_bancaire.transferts (transfert_date, employe_matricule, agence_code) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            LocalDate transfertDate = affecterEmploye.getTransfert_date();
            int employeMatricule = affecterEmploye.getEmploye().getId();
            String agenceCode = affecterEmploye.getAgence().getCode();

            statement.setObject(1, transfertDate);
            statement.setInt(2, employeMatricule);
            statement.setString(3, agenceCode);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                return Optional.of(affecterEmploye);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<AffecterEmploye> getHistoriqueAffectation(Employe employe) {
        List<AffecterEmploye> historiqueAffs = new ArrayList<>();
        String sql = "SELECT t.*, a.nom AS agence_nom, a.adresse AS agence_adresse, a.telephone AS agence_telephone " +
                "FROM gestion_bancaire.transferts t " +
                "LEFT JOIN gestion_bancaire.agence a ON t.agence_code = a.code " +
                "WHERE t.employe_matricule = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, employe.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    AffecterEmploye affecterEmploye = new AffecterEmploye();
                    affecterEmploye.setTransfert_id(resultSet.getInt("transfert_id"));
                    affecterEmploye.setTransfert_date(resultSet.getDate("transfert_date").toLocalDate());

                    // Set employe information
                    Employe employeAssocie = new Employe();
                    employeAssocie.setMatricule(resultSet.getString("employe_matricule"));
                    affecterEmploye.setEmploye(employeAssocie);

                    // Set agence information
                    Agence agenceAssocie = new Agence();
                    agenceAssocie.setCode(resultSet.getString("agence_code"));
                    agenceAssocie.setNom(resultSet.getString("agence_nom"));
                    agenceAssocie.setAdresse(resultSet.getString("agence_adresse"));
                    agenceAssocie.setTelephone(resultSet.getString("agence_telephone"));
                    affecterEmploye.setAgence(agenceAssocie);

                    historiqueAffs.add(affecterEmploye);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return historiqueAffs;
    }

    @Override
    public boolean deleteAffectation() {
        boolean delete = false;
        String sql = "delete from gestion_bancaire.transferts";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            int rows = statement.executeUpdate();
            if (rows > 0){
                delete = true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return delete;
    }





}
