package org.example.Implementation;

import org.example.Db.DatabaseConnection;
import org.example.Dto.Mission;
import org.example.Dto.Operation;
import org.example.Dto.TypeCompte;
import org.example.Dto.TypeOperation;
import org.example.Interface.IOperation;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImOperation implements IOperation {

    Connection connection;
    public ImOperation() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }

    @Override
    public Optional<Operation> saveOperation(Operation operation) {

        String OperationQuery = "INSERT INTO gestion_bancaire.operation (numero, datecreation, montant, status) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement OperationPreparedStatement = connection.prepareStatement(OperationQuery);

            OperationPreparedStatement.setString(1, operation.getNumero());
            LocalDate localDate = operation.getDateCreation();
            java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
            OperationPreparedStatement.setDate(2, sqlDate);
            OperationPreparedStatement.setDouble(3, operation.getMontant());
            OperationPreparedStatement.setObject(4, operation.getStatus(), Types.OTHER);

            int rowsMissionInserted = OperationPreparedStatement.executeUpdate();

            if (rowsMissionInserted == 1){
                return Optional.of(operation);
            }

        } catch (SQLException e) {
            System.out.println("Une erreur SQL s'est produite : " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public boolean delete(String numero) {
        try {
            String deleteQuery = "DELETE FROM gestion_bancaire.operation WHERE numero = ?";

            try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
                statement.setString(1, numero);

                int rowsDeleted = statement.executeUpdate();

                return rowsDeleted > 0;
            }
        } catch (SQLException e) {
            // Imprimer un message d'erreur en cas d'erreur SQL
            System.out.println("Une erreur SQL s'est produite lors de la suppression : " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Operation> readOperation(Operation operationParam) {
        List<Operation> operations = new ArrayList<>();

        try {
            String selectOperation = "SELECT * FROM gestion_bancaire.operation ";
            PreparedStatement statement = connection.prepareStatement(selectOperation);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String numero = resultSet.getString("numero");
                LocalDate dateCreation = resultSet.getDate("dateCreation").toLocalDate();
                double montant = resultSet.getDouble("montant");
                String statusStr = resultSet.getString("status");
                TypeOperation status = TypeOperation.valueOf(statusStr);

                Operation operation = new Operation(numero, dateCreation, montant, status);
                operations.add(operation);
            }

        } catch (SQLException e) {
            System.out.println("Une erreur SQL s'est produite : " + e.getMessage());
        }

        return operations;
    }
    @Override
    public Optional<Operation> Retrait(String numero, double montant) {
        try {

            if (!compteExists(numero)) {
                return Optional.empty();
            }

            // Vérifier si le montant est valide (positif et inférieur ou égal au solde)
            double solde = getAccountBalance(numero);
            if (montant <= 0 || montant > solde) {
                return Optional.empty(); // Montant invalide pour le retrait.
            }

            // Effectuer le retrait en mettant à jour le solde
            double nouveauSolde = solde - montant;
            updateAccountBalance(numero, nouveauSolde);

            // Créer une nouvelle opération pour le retrait
            LocalDate dateCreation = LocalDate.now();
            TypeOperation typeOperation = TypeOperation.Retrait;
            Operation operation = new Operation(numero, dateCreation, montant, typeOperation);

            // Enregistrer le retrait dans la base de données
            return saveOperation(operation);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Operation> Versement(String numero, double montant) {
        try {

            if (!compteExists(numero)) {
                return Optional.empty();
            }

            if (montant <= 0) {
                return Optional.empty();
            }

            double solde = getAccountBalance(numero);
            double nouveauSolde = solde + montant;
            updateAccountBalance(numero, nouveauSolde);

            LocalDate dateCreation = LocalDate.now();
            TypeOperation typeOperation = TypeOperation.Versement;
            Operation operation = new Operation(numero, dateCreation, montant, typeOperation);

            return saveOperation(operation);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }



    // Méthode pour obtenir le solde du compte (opération)
    @Override
    public double getAccountBalance(String numero) throws SQLException {
        String selectBalanceQuery = "SELECT solde FROM gestion_bancaire.compte WHERE numero = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectBalanceQuery)) {
            preparedStatement.setString(1, numero);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("solde");
                } else {
                    throw new SQLException("Le compte n'existe pas.");
                }
            }
        }
    }

    // Méthode pour mettre à jour le solde du compte (opération)
    @Override
    public void updateAccountBalance(String numero, double nouveauSolde) throws SQLException {
        String updateBalanceQuery = "UPDATE gestion_bancaire.compte SET solde = ? WHERE numero = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateBalanceQuery)) {
            preparedStatement.setDouble(1, nouveauSolde);
            preparedStatement.setString(2, numero);
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour vérifier si une opération existe
    @Override
    public boolean compteExists(String numero) throws SQLException {
        String checkCompteQuery = "SELECT COUNT(*) FROM gestion_bancaire.Compte WHERE numero = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(checkCompteQuery)) {
            preparedStatement.setString(1, numero);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }

    }

    @Override
    public Optional<Operation> findOperationByNumero(String numero) {

        String checkCompteQuery = "SELECT * FROM gestion_bancaire.operation WHERE numero = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(checkCompteQuery)) {
            preparedStatement.setString(1, numero);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Vérifier si un compte correspondant a été trouvé
                if (resultSet.next()) {
                   double montant = resultSet.getDouble("montant");
                   LocalDate dateCreation = resultSet.getDate("datecreation").toLocalDate();
                    String statusStr = resultSet.getString("status");
                    TypeOperation status = TypeOperation.valueOf(statusStr);

                    Operation operation = new Operation(numero,dateCreation,montant,status);
                    operation.setNumero(numero);
                    operation.setMontant(montant);
                    operation.setDateCreation(dateCreation);
                    operation.setStatus(status);

                    return Optional.of(operation);
                } else {

                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            System.out.println("Une erreur SQL s'est produite : " + e.getMessage());
            return Optional.empty();
        }
    }


}
