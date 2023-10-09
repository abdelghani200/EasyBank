package org.example.Interface;

import org.example.Dto.Transaction;

import java.util.Optional;

public interface ITransaction {
    Optional<Transaction> save(Transaction transaction);
    Boolean updateCompteSourceMontant(Transaction transaction);
    Boolean updateCompteDestinationMontant(Transaction transaction);
    int deleteAll();
    int delete(int id);

}
