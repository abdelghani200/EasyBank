package org.example.Implementation;

import org.example.Db.DatabaseConnection;
import org.example.Dto.Client;
import org.example.Dto.Personne;
import org.example.Interface.IClient;

import java.sql.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImClient extends ImPersonne<Client> implements IClient {

    private Connection connection;

    public ImClient() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }

    @Override
    public Optional<Client> findByCode(String code) {
        String sqlFind = "SELECT c.code, p.nom, p.prenom, p.datenaissance, p.telephone, c.adresse " +
                "FROM gestion_bancaire.Client c " +
                "INNER JOIN gestion_bancaire.Personne p ON c.personne_id = p.id " +
                "WHERE c.code = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlFind)) {
            preparedStatement.setString(1, code);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    LocalDate dateNaissance = resultSet.getDate("datenaissance").toLocalDate();
                    String adresse = resultSet.getString("adresse");
                    String telephone = resultSet.getString("telephone");

                    Client client = new Client(code, adresse);
                    client.setCode(code);
                    client.setNom(nom);
                    client.setPrenom(prenom);
                    client.setDateNaissance(dateNaissance);
                    client.setAdresse(adresse);
                    client.setTelephone(telephone);


                    return Optional.of(client);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    public boolean deleteClient(String code) {
        try {
            connection.setAutoCommit(false); // Désactiver la validation automatique

            String deleteClientQuery = "DELETE FROM gestion_bancaire.Client WHERE code = ?";
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
                    "WHERE id NOT IN (SELECT DISTINCT personne_id FROM gestion_bancaire.Client)";
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
    public Optional<Client> save(Client client) {

        Optional<Client> personneOptional = super.save(client);

        if (personneOptional.isPresent()) {
            Personne personne = personneOptional.get();
            String insertClientQuery = "INSERT INTO gestion_bancaire.Client (code, adresse, personne_id) " +
                    "VALUES (?, ?, ?)";

            try {
                // Insertion dans la table Client
                PreparedStatement clientStatement = connection.prepareStatement(insertClientQuery);
                clientStatement.setString(1, client.getCode());
                clientStatement.setString(2, client.getAdresse());
                clientStatement.setInt(3, personne.getId());

                int clientRowsAffected = clientStatement.executeUpdate();
                if (clientRowsAffected == 1) {
                    return Optional.of(client);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return Optional.empty();
    }


    @Override
    public Optional<List<Client>> readAll() {
        List<Client> clients = new ArrayList<>();

        String selectAllClientsQuery = "SELECT id, code, adresse, personne_id FROM gestion_bancaire.Client";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectAllClientsQuery)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String code = resultSet.getString("code");
                    String adresse = resultSet.getString("adresse");
                    int personneId = resultSet.getInt("personne_id");

                    // Récupérez les informations de base du client depuis la table Client
                    Client client = new Client(code, adresse);
                    client.setId(id);
                    client.setCode(code);
                    client.setAdresse(adresse);

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
                                client.setNom(nom);
                                client.setPrenom(prenom);
                                client.setTelephone(telephone);
                                client.setDateNaissance(dateNaissance);
                            }
                        }
                    }

                    clients.add(client);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (!clients.isEmpty()) {
            return Optional.of(clients);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean update(Client client) {
        String updateClientQuery = "UPDATE gestion_bancaire.Client SET adresse = ? WHERE code = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateClientQuery)) {
            preparedStatement.setString(1, client.getAdresse());
            preparedStatement.setString(2, client.getCode());

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                return false;
            }

            // Maintenant, mettez à jour les informations de la personne dans la table "Personne"
            String updatePersonneQuery = "UPDATE gestion_bancaire.Personne SET nom = ?, prenom = ?, datenaissance = ? " +
                    "WHERE id = (SELECT personne_id FROM gestion_bancaire.Client WHERE code = ?)";

            try (PreparedStatement personneStatement = connection.prepareStatement(updatePersonneQuery)) {
                personneStatement.setString(1, client.getNom());
                personneStatement.setString(2, client.getPrenom());
                personneStatement.setDate(3, Date.valueOf(client.getDateNaissance()));
                personneStatement.setString(4, client.getCode());

                int personneRowsUpdated = personneStatement.executeUpdate();

                // Si au moins une ligne a été mise à jour dans chaque table, retournez true
                return personneRowsUpdated > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du client.", e);
        }
    }

    @Override
    public Optional<Client> findByEmailOrNomOrPrenom(String searchValue) {
        String sqlFind = "SELECT c.code, p.nom, p.prenom, p.datenaissance, p.telephone, c.adresse " +
                "FROM gestion_bancaire.Client c " +
                "INNER JOIN gestion_bancaire.Personne p ON c.personne_id = p.id " +
                "WHERE c.adresse = ? OR p.nom = ? OR p.prenom = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlFind)) {
            preparedStatement.setString(1, searchValue);
            preparedStatement.setString(2, searchValue);
            preparedStatement.setString(3, searchValue);


            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String code = resultSet.getString("code");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    LocalDate dateNaissance = resultSet.getDate("datenaissance").toLocalDate();
                    String adresse = resultSet.getString("adresse");
                    String telephone = resultSet.getString("telephone");

                    Client client = new Client(code, adresse);
                    client.setCode(code);
                    client.setNom(nom);
                    client.setPrenom(prenom);
                    client.setDateNaissance(dateNaissance);
                    client.setAdresse(adresse);
                    client.setTelephone(telephone);

                    return Optional.of(client);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }




}

