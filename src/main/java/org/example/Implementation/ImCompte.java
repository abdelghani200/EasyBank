package org.example.Implementation;

import org.example.Db.DatabaseConnection;
import org.example.Dto.*;
import org.example.Interface.ICompte;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class ImCompte<T extends Compte> implements ICompte<T> {

    private Connection connection;

    public ImCompte() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }


    @Override
    public Optional<Compte> save(T compte) {

        String insertCompteQuery = "INSERT INTO gestion_bancaire.Compte (numero, solde, datecreation, status, client_id, employe_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try {

            PreparedStatement compteStatement = connection.prepareStatement(insertCompteQuery, Statement.RETURN_GENERATED_KEYS);
            compteStatement.setString(1, compte.getNumero());
            compteStatement.setDouble(2, compte.getSolde());
            LocalDate localDate = compte.getDateCreation();
            java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
            compteStatement.setDate(3, sqlDate);
            compteStatement.setObject(4, compte.getStatus(), Types.OTHER);

            compteStatement.setInt(5,compte.getEmploye().getId());
            compteStatement.setInt(6,compte.getClient().getId());

            int rowsAffected = compteStatement.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet generatedKeys = compteStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int compteId = generatedKeys.getInt(1);
                    compte.setId(compteId);

                    return Optional.of(compte);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }


    @Override
    public List<Compte> findByClient(Client client) {
        List<Compte> compteList = new ArrayList<>();
        String selectComptesQuery = "SELECT c.* FROM gestion_bancaire.Compte c " +
                "JOIN gestion_bancaire.Client cl ON c.client_id = cl.id " +
                "JOIN gestion_bancaire.Personne p ON cl.personne_id = p.id " +
                "WHERE p.nom = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectComptesQuery);
            preparedStatement.setString(1, client.getNom());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String numero = resultSet.getString("numero");
                double solde = resultSet.getDouble("solde");
                LocalDate dateCreation = resultSet.getDate("datecreation").toLocalDate();
                String statusStr = resultSet.getString("status");
                TypeCompte status = TypeCompte.valueOf(statusStr);

                //Compte compte = new Compte(id, numero, solde, dateCreation, status);
                Compte compte = new CompteCourant();
                compte.setId(id);
                compte.setNumero(numero);
                compte.setSolde(solde);
                compte.setStatus(status);
                compte.setDateCreation(dateCreation);

                compteList.add(compte);

                return compteList;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return compteList;
    }

    @Override
    public Optional<Compte> deleteCompte(String numeroCompte) {

        String deleteCompte = "delete from gestion_bancaire.Compte where numero = ?";

        try {
            PreparedStatement deleteStatement = connection.prepareStatement(deleteCompte);
            deleteStatement.setString(1, numeroCompte);

            int rowsdeleted = deleteStatement.executeUpdate();
            if (rowsdeleted == 1) {

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public boolean changeCompteStatus(Compte compte) {
        String updateStatusQuery = "UPDATE gestion_bancaire.Compte SET status = ? WHERE numero = ?";

        try {
            PreparedStatement updateStatement = connection.prepareStatement(updateStatusQuery);
            updateStatement.setObject(1, compte.getStatus(), Types.OTHER);
            updateStatement.setString(2, compte.getNumero()); // Ajouter cette ligne pour spécifier le numéro du compte à mettre à jour

            int rowsUpdated = updateStatement.executeUpdate();
            return rowsUpdated == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating account status", e);
        }
    }
    @Override
    public Optional<Compte> update(Compte compte) {
        String updateCompteQuery = "UPDATE gestion_bancaire.compte " +
                "SET solde = CAST(? AS real), datecreation = ? " +
                "WHERE numero = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateCompteQuery)) {
            preparedStatement.setDouble(1, compte.getSolde());
            preparedStatement.setObject(2, compte.getDateCreation());
            preparedStatement.setString(3, compte.getNumero());

            int rowsUpdated = preparedStatement.executeUpdate();
             System.out.println(compte.getSolde());
            System.out.println(compte.getNumero());
            if (rowsUpdated > 0) {
                return Optional.of(compte);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


    private Compte extractCompteFromResultSet(ResultSet resultSet) throws SQLException {
        int compteId = resultSet.getInt("id");
        String numero = resultSet.getString("numero");
        double solde = resultSet.getDouble("solde");
        LocalDate dateCreation = resultSet.getDate("datecreation").toLocalDate();
        String statusStr = resultSet.getString("status");
        TypeCompte status = TypeCompte.valueOf(statusStr);

        int clientId = resultSet.getInt("id");
        String clientCode = resultSet.getString("code");
        String clientAdresse = resultSet.getString("adresse");

        int employeId = resultSet.getInt("id");
        String employeMatricule = resultSet.getString("matricule");
        String adressemail = resultSet.getString("adressemail");
        LocalDate daterecrutement = resultSet.getDate("daterecrutement").toLocalDate();

        String nom = resultSet.getString("nom");
        String prenom = resultSet.getString("prenom");
        LocalDate datenaissance = resultSet.getDate("datenaissance").toLocalDate();
        String telephone = resultSet.getString("telephone");

        //Compte compte = new Compte(compteId, numero, solde, dateCreation, status);
        Compte compte = new CompteCourant();
        Client client = new Client(clientId, nom, prenom, telephone, datenaissance, clientCode, clientAdresse);
        Employe employe = new Employe(employeId, nom, prenom, telephone, datenaissance, employeMatricule, daterecrutement, adressemail);

        compte.setClient(client);
        compte.setEmploye(employe);

        return compte;
    }

    public Map<TypeCompte, List<Compte>> findByStatus() {
        Map<TypeCompte, List<Compte>> compteInfoMap = new HashMap<>();
        String selectComptesQuery = "SELECT c.*, p.*, e.*, cl.* " +
                "FROM gestion_bancaire.Compte c " +
                "JOIN gestion_bancaire.Client cl ON c.client_id = cl.id " +
                "JOIN gestion_bancaire.personne p ON cl.personne_id = p.id " +
                "LEFT JOIN gestion_bancaire.employe e ON c.employe_id = e.id";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectComptesQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Compte compte = extractCompteFromResultSet(resultSet);
                TypeCompte status = compte.getStatus();
                compteInfoMap.computeIfAbsent(status, k -> new ArrayList<>()).add(compte);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return compteInfoMap;
    }

    public Map<LocalDate, List<Compte>> findByDateCreation() {
        Map<LocalDate, List<Compte>> compteInfoMap = new HashMap<>();
        String selectComptesQuery = "SELECT c.*, p.*, e.*, cl.* " +
                "FROM gestion_bancaire.Compte c " +
                "JOIN gestion_bancaire.Client cl ON c.client_id = cl.id " +
                "JOIN gestion_bancaire.personne p ON cl.personne_id = p.id " +
                "LEFT JOIN gestion_bancaire.employe e ON c.employe_id = e.id";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectComptesQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Compte compte = extractCompteFromResultSet(resultSet);
                LocalDate dateCreation = compte.getDateCreation();
                compteInfoMap.computeIfAbsent(dateCreation, k -> new ArrayList<>()).add(compte);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return compteInfoMap;
    }

    @Override
    public Map<TypeCompte, List<Compte>> findCompteByOperationNumero(String operationNumero) {
        Map<TypeCompte, List<Compte>> compteInfoMap = new HashMap<>();
        String selectComptesQuery = "SELECT c.*, p.*, e.*, cl.* " +
                "FROM gestion_bancaire.operation o " +
                "JOIN gestion_bancaire.compte c ON o.numero = c.numero " +
                "JOIN gestion_bancaire.Client cl ON c.client_id = cl.id " +
                "JOIN gestion_bancaire.personne p ON cl.personne_id = p.id " +
                "LEFT JOIN gestion_bancaire.employe e ON c.employe_id = e.id " +
                "WHERE o.numero = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectComptesQuery);
            preparedStatement.setString(1, operationNumero);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Compte compte = extractCompteFromResultSet(resultSet);
                TypeCompte status = compte.getStatus();
                compteInfoMap.computeIfAbsent(status, k -> new ArrayList<>()).add(compte);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return compteInfoMap;
    }



}
