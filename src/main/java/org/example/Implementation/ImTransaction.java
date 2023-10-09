package org.example.Implementation;

import org.example.Db.DatabaseConnection;
import org.example.Dto.Transaction;
import org.example.Exception.TransactionException;
import org.example.Interface.ITransaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class ImTransaction implements ITransaction {

    private final Connection connection;

    public ImTransaction() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }
    @Override
    public Optional<Transaction> save(Transaction transaction) {
        String sql = "insert into gestion_bancaire.transaction(comptesource,comptedestination) values(?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, transaction.getCompteSource().getId());
            statement.setInt(2, transaction.getCompteDestination().getId());
            //statement.setInt(3, 1);

            int rows = statement.executeUpdate();
            if (rows > 0){
                return Optional.of(transaction);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Boolean updateCompteSourceMontant(Transaction transaction) {
        String updateSql = "update gestion_bancaire.compte set solde = ? where id = ?";

        double newSolde = transaction.getCompteSource().getSolde() - transaction.getMontant();
        System.out.println("hhhsggssf" + " " + transaction.getMontant());
        try {
            PreparedStatement statement = connection.prepareStatement(updateSql);
            statement.setDouble(1, newSolde);
            statement.setInt(2, transaction.getCompteSource().getId());
            int rows = statement.executeUpdate();
            if (rows > 0){
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public Boolean updateCompteDestinationMontant(Transaction transaction) {
        String updateSql = "update gestion_bancaire.compte set solde = ? where id =?";

        double newSolde = transaction.getCompteDestination().getSolde() + transaction.getMontant();
        System.out.println("hhhsggssf" + " " + transaction.getMontant());
        try {
            PreparedStatement statement = connection.prepareStatement(updateSql);
            statement.setDouble(1, newSolde);
            statement.setInt(2, transaction.getCompteDestination().getId());
            int rows = statement.executeUpdate();
            if (rows > 0){
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public int deleteAll() {
        String sql = "delete from gestion_bancaire.transaction";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            int rows = statement.executeUpdate();
            if (rows > 0){
                return 1;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    @Override
    public int delete(int id) {
        String sql = "delete from gestion_bancaire.transaction where id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            int rows = statement.executeUpdate();
            if (rows > 0){
                return 1;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
}
