package org.example.Implementation;

import org.example.Db.DatabaseConnection;
import org.example.Dto.Agence;
import org.example.Exception.AgenceException;
import org.example.Interface.IAgence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImAgence implements IAgence {

    Connection connection;
    public ImAgence() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }
    @Override
    public Optional<Agence> save(Agence agence) throws AgenceException {

        String sqlAgence = "insert into gestion_bancaire.agence(code,nom,adresse,telephone) values(?,?,?,?)";

        try{
            PreparedStatement statement = connection.prepareStatement(sqlAgence);
            statement.setString(1, agence.getCode());
            statement.setString(2, agence.getNom());
            statement.setString(3, agence.getAdresse());
            statement.setString(4, agence.getTelephone());

            int rowsagence = statement.executeUpdate();
            if (rowsagence == 0){
                throw  new AgenceException("Échec de l'insertion de l'agence.");
            } else {
                return Optional.of(agence);
            }

        }catch (Exception e){
            throw new AgenceException("Erreur lors de l'insertion de l'agence.", e);
        }

    }

    public Optional<Agence> update(Agence agence) {
        String agenceSql = "UPDATE gestion_bancaire.agence " +
                "SET nom = ?, adresse = ?, telephone = ? " +
                "WHERE code = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(agenceSql);
            statement.setString(1, agence.getNom());
            statement.setString(2, agence.getAdresse());
            statement.setString(3, agence.getTelephone());
            statement.setString(4, agence.getCode());

            int rowrUpdate = statement.executeUpdate();

            if (rowrUpdate == 1) {
                return Optional.of(agence);
            } else {
                return Optional.empty();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Agence> findByAdresse(String adresse) {
        List<Agence> agences = getAllAgences();
        return agences.stream()
                .filter(agence -> agence.getAdresse().equalsIgnoreCase(adresse))
                .findFirst();
    }

    @Override
    public int delete(String code) {
        String ageceSql = "delete from gestion_bancaire.agence whre code = ?";
        try{
            PreparedStatement statement = connection.prepareStatement(ageceSql);
            statement.setString(1, code);
            int delete = statement.executeUpdate();
            if (delete == 0){
                throw new AgenceException("Error de supprission.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    @Override
    public Optional<Agence> findByCode(String code) {
        List<Agence> agences = getAllAgences();

        return agences.stream()
                .filter(agence -> agence.getCode().equalsIgnoreCase(code))
                .findFirst();
    }

    private List<Agence> getAllAgences() {
        String sql = "select * from gestion_bancaire.agence";
        List<Agence> agences = new ArrayList<>();
        try (PreparedStatement statement =connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()){
            while (resultSet.next()) {
                Agence agence = new Agence();
                agence.setCode(resultSet.getString("code"));
                agence.setNom(resultSet.getString("nom"));
                agence.setAdresse(resultSet.getString("adresse"));
                agence.setTelephone(resultSet.getString("telephone"));
                agences.add(agence);
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return agences;
    }

}
