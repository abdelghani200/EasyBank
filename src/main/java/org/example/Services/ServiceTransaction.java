package org.example.Services;

import org.example.Dto.Operation;
import org.example.Dto.Transaction;
import org.example.Exception.TransactionException;
import org.example.Implementation.ImOperation;
import org.example.Interface.ITransaction;

import java.util.Optional;

public class ServiceTransaction {

    private ITransaction transactionService;
    private ImOperation operationServe;
    public ServiceTransaction(ITransaction transactionService, ImOperation operationServe) {
        this.transactionService = transactionService;
        this.operationServe = operationServe;
    }

    public Transaction createTransaction(Transaction transaction, Operation operation) throws TransactionException {
        if (transaction == null) {
            return null;
        } else {
            Optional<Operation> optionalOperation = operationServe.saveOperation(operation);
            if (optionalOperation.isPresent()) {
                if (transaction.getMontant() > transaction.getCompteSource().getSolde()) {
                    throw new TransactionException("Montant insuffisant !!");
                }
                Optional<Transaction> optionalTransaction = transactionService.save(transaction);
                if (optionalTransaction.isPresent()) {
                    boolean sourceUpdated = transactionService.updateCompteSourceMontant(transaction);
                    boolean destinationUpdated = transactionService.updateCompteDestinationMontant(transaction);

                    if (sourceUpdated && destinationUpdated) {
                        return optionalTransaction.get();
                    } else {
                        throw new TransactionException("Erreur lors de la mise à jour des comptes.");
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    public int deleteTransaction(int id){
        if (id == 0){
            return 0;
        }else {
            return transactionService.delete(id);
        }
    }

}
