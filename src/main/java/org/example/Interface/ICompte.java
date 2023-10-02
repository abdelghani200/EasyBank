package org.example.Interface;

import org.example.Dto.*;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ICompte<T extends Compte> {

    Optional<Compte> save(T compte);
    List<Compte> findByClient(Client client);

    Optional<Compte> deleteCompte(String numeroCompte);

    boolean changeCompteStatus(Compte compte);

    Optional<Compte> update(T compte);

    Map<TypeCompte, List<Compte>> findByStatus();
    Map<TypeCompte, List<Compte>> findCompteByOperationNumero(String operationNumero);

}
