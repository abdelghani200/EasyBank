package org.example.Implementation;

import org.example.Db.DatabaseConnection;
import org.example.Dto.Agence;
import org.example.Dto.Credit;
import org.example.Dto.EtatCredit;
import org.example.Dto.TypeCompte;
import org.example.Interface.ICredit;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ImCredit implements ICredit {

    private final Connection connection;

    public ImCredit() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }

    @Override
    public Optional<Credit> save(Credit credit) {
        String sql = "insert into gestion_bancaire.credit(num,montant,remarque,etat,date,duree,client_id) values(?,?,?,?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, credit.getNumero());
            statement.setDouble(2, credit.getMontant());
            statement.setString(3, credit.getRemarques());
            statement.setObject(4, credit.getEtat(), Types.OTHER);
            statement.setObject(5, credit.getDate());
            statement.setInt(6, credit.getDuree());
            statement.setInt(7, credit.getSimulation().getClient().getId());

            int najah = statement.executeUpdate();

            if (najah > 0){
                return Optional.of(credit);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Credit> findByEtat(EtatCredit etat) {
        List<Credit> credits = getAllCredits();

        return credits.stream()
                .filter(credit -> credit.getEtat() == etat)
                .collect(Collectors.toList());
    }

    @Override
    public List<Credit> findByDate(LocalDate date) {
        List<Credit> credits = getAllCredits();

        return credits.stream()
                .filter(credit -> credit.getDate().isEqual(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Credit> getAllCredits() {
        String sql = "select * from gestion_bancaire.credit";
        List<Credit> creditList = new ArrayList<>();
        try (PreparedStatement statement =connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()){
            while (resultSet.next()) {
                Credit credit = new Credit();
                credit.setNumero(resultSet.getInt("num"));
                String etatStr = resultSet.getString("etat");
                EtatCredit etat = EtatCredit.valueOf(etatStr);
                credit.setRemarques(resultSet.getString("remarque"));
                credit.setDuree(resultSet.getInt("duree"));
                java.sql.Date dateSql = resultSet.getDate("date");
                LocalDate date = dateSql.toLocalDate();
                Double montant = resultSet.getDouble("montant");
                credit.setDate(date);
                credit.setEtat(etat);
                credit.setMontant(montant);
                creditList.add(credit);
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return creditList;
    }

    @Override
    public boolean changeEtatCredit(int numero, EtatCredit etat) {
        String sql = "update gestion_bancaire.credit set etat = ?  where num = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            {
                statement.setObject(1, etat, Types.OTHER);
                statement.setInt(2, numero);

                int rows = statement.executeUpdate();
                if (rows > 0){
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }




}
