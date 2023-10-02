package org.example.Implementation;

import org.example.Db.DatabaseConnection;
import org.example.Dto.Client;
import org.example.Dto.Compte;
import org.example.Dto.CompteCourant;
import org.example.Dto.Employe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

public class ImCompeCourant extends ImCompte<CompteCourant> {

    private Connection connection;

    public ImCompeCourant() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }

    @Override
    public Optional<Compte> save(CompteCourant comptecourant) {
        Optional<Compte> compteOptional = super.save(comptecourant);

        if (compteOptional.isPresent()) {
            String insertClientQuery = "INSERT INTO gestion_bancaire.comptecourant (decouvert, id) VALUES (?, ?)";

            try {
                int compteId = compteOptional.get().getId();

                PreparedStatement clientStatement = connection.prepareStatement(insertClientQuery);
                clientStatement.setDouble(1, comptecourant.getDecouvert());
                clientStatement.setInt(2, compteId);

                int clientRowsAffected = clientStatement.executeUpdate();
                if (clientRowsAffected == 1) {
                    return Optional.of(comptecourant);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return Optional.empty();
    }



}
