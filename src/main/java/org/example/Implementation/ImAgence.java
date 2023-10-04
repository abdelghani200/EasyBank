package org.example.Implementation;

import org.example.Db.DatabaseConnection;
import org.example.Dto.Agence;
import org.example.Exception.AgenceException;
import org.example.Interface.IAgence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    @Override
    public Optional<Agence> update(Agence agence) {
        return Optional.empty();
    }

    @Override
    public Optional<Agence> findByAdresse(String adresse) {
        return Optional.empty();
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
        return Optional.empty();
    }
}
