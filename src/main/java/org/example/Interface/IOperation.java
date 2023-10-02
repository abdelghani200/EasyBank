package org.example.Interface;

import org.example.Dto.Operation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IOperation {

    Optional<Operation> saveOperation(Operation operation);
    Optional<Operation> Retrait(String numero, double montant);

    Optional<Operation> Versement(String numero, double montant);

    double getAccountBalance(String numero) throws SQLException;

    void updateAccountBalance(String numero, double nouveauSolde) throws SQLException;

    boolean compteExists(String numero) throws SQLException;

    Optional<Operation>  findOperationByNumero(String numero);

    boolean delete(String numero);

    List<Operation> readOperation(Operation operationParam);
}
