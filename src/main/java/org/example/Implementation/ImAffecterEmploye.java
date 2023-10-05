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
        String sql = "INSERT INTO transferts (transfert_date, employe_matricule, agence_code) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            LocalDate transfertDate = affecterEmploye.getTransfert_date();
            String employeMatricule = affecterEmploye.getEmploye().getMatricule();
            String agenceCode = affecterEmploye.getAgence().getCode();

            statement.setObject(1, transfertDate);
            statement.setString(2, employeMatricule);
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
        String sql = "select * from gestion_bancaire.transfers where employe_matricule = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, employe.getMatricule());
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    AffecterEmploye affecterEmploye = new AffecterEmploye();
                    affecterEmploye.setTransfert_id(resultSet.getInt("transfert_id"));
                    affecterEmploye.setTransfert_date(resultSet.getDate("transfert_date").toLocalDate());
                    Employe employeAssocie = new Employe();
                    employeAssocie.setMatricule(resultSet.getString("employe_matricule"));
                    affecterEmploye.setEmploye(employeAssocie);
                    //affecterEmploye.setAgence(new ImAgence(resultSet.getString("agence_code")));
                    Agence agenceAssocie = new Agence();
                    agenceAssocie.setCode(resultSet.getString("agence_code"));
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
        String sql = "delete from gestion_bancaire";
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
