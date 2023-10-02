package org.example.Implementation;

import org.example.Db.DatabaseConnection;
import org.example.Dto.Client;
import org.example.Dto.Compte;
import org.example.Dto.CompteEpargne;
import org.example.Dto.Employe;
import org.example.Interface.ICompte;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class ImCompteEpargne extends ImCompte<CompteEpargne> {

    private Connection connection;

    public ImCompteEpargne() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }


    @Override
    public Optional<Compte> save(CompteEpargne compteepargne) {

        Optional<Compte> compteOptional = super.save(compteepargne);

        if (compteOptional.isPresent()) {
            String insertClientQuery = "INSERT INTO gestion_bancaire.compteepargne (tauxinteret, id) " +
                    "VALUES (?, ?)";

            try {

                int compteId = compteOptional.get().getId();
                PreparedStatement clientStatement = connection.prepareStatement(insertClientQuery);
                clientStatement.setDouble(1, compteepargne.getTauxInteret());
                clientStatement.setInt(2, compteId);

                int clientRowsAffected = clientStatement.executeUpdate();
                if (clientRowsAffected == 1) {
                    return Optional.of(compteepargne);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return Optional.empty();
    }
}
