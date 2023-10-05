package org.example.Implementation;


import org.example.Db.DatabaseConnection;
import org.example.Dto.Client;
import org.example.Dto.Employe;
import org.example.Dto.Personne;
import org.example.Interface.IEmploye;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImEmploye extends ImPersonne<Employe> implements IEmploye {

    private Connection connection;

    public ImEmploye() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }


    @Override
    public Optional<Employe> findByMatricule(String matricule) {
        String sqlFind = "SELECT e.matricule, p.nom, p.prenom, p.telephone, p.datenaissance, e.adressemail, e.daterecrutement " +
                "FROM gestion_bancaire.Employe e " +
                "INNER JOIN gestion_bancaire.Personne p ON e.personne_id = p.id " +
                "WHERE e.matricule = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlFind)) {
            preparedStatement.setString(1, matricule);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    String telephone = resultSet.getString("telephone");
                    LocalDate dateNaissance = resultSet.getDate("datenaissance").toLocalDate();
                    LocalDate dateRecrutement = resultSet.getDate("dateRecrutement").toLocalDate();
                    String adressemail = resultSet.getString("adressemail");

                    Employe employe = new Employe(matricule, adressemail,dateRecrutement);
                    employe.setMatricule(matricule);
                    employe.setNom(nom);
                    employe.setPrenom(prenom);
                    employe.setTelephone(telephone);
                    employe.setDateNaissance(dateNaissance);
                    employe.setAdresseEmail(adressemail);
                    employe.setDateRecrutement(dateRecrutement);


                    return Optional.of(employe);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();

    }

    @Override
    public Optional<Employe> save(Employe employe) {

        Optional<Employe> employeOptinal = super.save(employe);

        if (employeOptinal.isPresent()) {
            Personne personne = employeOptinal.get();
            String insertEmployeQuery = "INSERT INTO gestion_bancaire.Employe (matricule, daterecrutement, adressemail, personne_id,agence_code) " +
                    "VALUES (?, ?, ?, ?, ?)";

            try {
                PreparedStatement employeStatement = connection.prepareStatement(insertEmployeQuery);

                employeStatement.setString(1, employe.getMatricule());
                employeStatement.setObject(2, employe.getDateRecrutement());
                employeStatement.setString(3, employe.getAdresseEmail());
                employeStatement.setInt(4, employe.getId());
                employeStatement.setString(5, employe.getAgence().getCode());


                int employeInserted = employeStatement.executeUpdate();

                if (employeInserted == 1) {
                    return Optional.of(employe);
                }

            } catch (SQLException e) {
                System.out.println(e.getErrorCode());
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<List<Employe>> readAllEmployes() {
        List<Employe> employes = new ArrayList<>();

        String selectAllClientsQuery = "SELECT id, matricule, daterecrutement,adressemail, personne_id FROM gestion_bancaire.Employe";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectAllClientsQuery)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String matricule = resultSet.getString("matricule");
                    String adressemail = resultSet.getString("adressemail");
                    LocalDate daterecrutement = resultSet.getDate("daterecrutement").toLocalDate();
                    int personneId = resultSet.getInt("personne_id");

                    // Récupérez les informations de base du client depuis la table Client
                    Employe employe = new Employe(matricule, adressemail,daterecrutement);
                    employe.setId(id);
                    employe.setMatricule(matricule);
                    employe.setAdresseEmail(adressemail);
                    employe.setDateRecrutement(daterecrutement);

                    // Maintenant, récupérez les informations de la personne associée depuis la table Personne
                    String selectPersonneQuery = "SELECT nom, prenom, telephone, dateNaissance FROM gestion_bancaire.Personne WHERE id = ?";
                    try (PreparedStatement personneStatement = connection.prepareStatement(selectPersonneQuery)) {
                        personneStatement.setInt(1, personneId);
                        try (ResultSet personneResultSet = personneStatement.executeQuery()) {
                            if (personneResultSet.next()) {
                                String nom = personneResultSet.getString("nom");
                                String prenom = personneResultSet.getString("prenom");
                                String telephone = personneResultSet.getString("telephone");
                                LocalDate dateNaissance = personneResultSet.getDate("dateNaissance").toLocalDate();

                                // Mettez à jour le client avec les informations de la personne
                                employe.setNom(nom);
                                employe.setPrenom(prenom);
                                employe.setTelephone(telephone);
                                employe.setDateNaissance(dateNaissance);
                            }
                        }
                    }

                    employes.add(employe);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (!employes.isEmpty()) {
            return Optional.of(employes);
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteEmploye(String code) {
        try {
            connection.setAutoCommit(false); // Désactiver la validation automatique

            String deleteClientQuery = "DELETE FROM gestion_bancaire.Employe WHERE matricule = ?";
            try (PreparedStatement preparedStatementClient = connection.prepareStatement(deleteClientQuery)) {
                preparedStatementClient.setString(1, code);
                int rowsDeleted = preparedStatementClient.executeUpdate();
                if (rowsDeleted == 0) {
                    // Aucun client trouvé, annuler la transaction
                    connection.rollback();
                    return false;
                }
            }

            // Étape 2 : Supprimer les entrées de la table Personne non associées à un client
            String deleteOrphanedPersonsQuery = "DELETE FROM gestion_bancaire.Personne " +
                    "WHERE id NOT IN (SELECT DISTINCT personne_id FROM gestion_bancaire.Employe)";
            try (PreparedStatement preparedStatementOrphanedPersons = connection.prepareStatement(deleteOrphanedPersonsQuery)) {
                preparedStatementOrphanedPersons.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                // En cas d'erreur, annuler la transaction
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Erreur lors de l'annulation de la transaction.", ex);
            }
            throw new RuntimeException("Erreur lors de la suppression du client et de la personne.", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de la réinitialisation de la validation automatique.", e);
            }
        }
    }


    @Override
    public boolean update(Employe employe) {
        String updateClientQuery = "UPDATE gestion_bancaire.Employe SET adressemail = ?, daterecrutement = ? WHERE matricule = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateClientQuery)) {
            preparedStatement.setString(1, employe.getAdresseEmail());
            preparedStatement.setDate(2, Date.valueOf(employe.getDateRecrutement()));
            preparedStatement.setString(3, employe.getMatricule());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                return false;
            }
            // Maintenant, mettez à jour les informations de la personne dans la table "Personne"
            String updatePersonneQuery = "UPDATE gestion_bancaire.Personne SET nom = ?, prenom = ?, datenaissance = ? " +
                    "WHERE id = (SELECT personne_id FROM gestion_bancaire.Employe WHERE matricule = ?)";

            try (PreparedStatement personneStatement = connection.prepareStatement(updatePersonneQuery)) {
                personneStatement.setString(1, employe.getNom());
                personneStatement.setString(2, employe.getPrenom());
                personneStatement.setDate(3, Date.valueOf(employe.getDateNaissance()));
                personneStatement.setString(4, employe.getMatricule());

                int personneRowsUpdated = personneStatement.executeUpdate();

                // Si au moins une ligne a été mise à jour dans chaque table, retournez true
                return personneRowsUpdated > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du employe.", e);
        }
    }

    @Override
    public Optional<Employe> findByEmailOrNomOrPrenom(String searchValue) {
        String sqlFind = "SELECT e.matricule, p.nom, p.prenom, p.datenaissance, p.telephone, e.adressemail, e.daterecrutement " +
                "FROM gestion_bancaire.Employe e " +
                "INNER JOIN gestion_bancaire.Personne p ON e.personne_id = p.id " +
                "WHERE e.adressemail = ? OR p.nom = ? OR p.prenom = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlFind)) {
            preparedStatement.setString(1, searchValue);
            preparedStatement.setString(2, searchValue);
            preparedStatement.setString(3, searchValue);
            //preparedStatement.setDate(4, Date.valueOf(searchValue));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String matricule = resultSet.getString("matricule");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    LocalDate dateNaissance = resultSet.getDate("datenaissance").toLocalDate();
                    String telephone = resultSet.getString("telephone");
                    String adresseEmail = resultSet.getString("adressemail");
                    LocalDate daterecrutement = resultSet.getDate("daterecrutement").toLocalDate();

                    Employe employe = new Employe(matricule, adresseEmail,daterecrutement);
                    employe.setMatricule(matricule);
                    employe.setNom(nom);
                    employe.setPrenom(prenom);
                    employe.setDateNaissance(dateNaissance);
                    employe.setTelephone(telephone);
                    employe.setAdresseEmail(adresseEmail);
                    employe.setDateRecrutement(daterecrutement);


                    return Optional.of(employe);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Employe> changeAgence(Employe employe, String code_agence) {
        String sql = "update gestion_bancaire.employe set agence_code = ? where matricule = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, code_agence);
            statement.setString(2, employe.getMatricule());

            int rowsChanged = statement.executeUpdate();

            if (rowsChanged == 0){
                throw new Exception("Error!!");
            }
            return Optional.of(employe);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


}

