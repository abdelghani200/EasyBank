package org.example.Implementation;

import org.example.Db.DatabaseConnection;
import org.example.Dto.AffecterEmploye;
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
        String sql = "INSERT INTO transferts (transfert_date, employe_matricule, agence_code) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            LocalDate transfertDate = affecterEmploye.getTransfert_date();
            int employeMatricule = affecterEmploye.getEmploye_matricule();
            String agenceCode = affecterEmploye.getAgence_code();

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





}
