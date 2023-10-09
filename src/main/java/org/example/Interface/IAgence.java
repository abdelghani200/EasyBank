package org.example.Interface;

import org.example.Dto.Agence;
import org.example.Exception.AgenceException;

import java.util.Optional;

public interface IAgence {

    Optional<Agence> save(Agence agence) throws AgenceException;
    Optional<Agence> update(Agence agence);
    Optional<Agence> findByAdresse(String adresse);
    int delete(String code);
    Optional<Agence> findByCode(String code);


}
