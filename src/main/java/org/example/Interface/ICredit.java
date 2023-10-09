package org.example.Interface;

import org.example.Dto.Credit;
import org.example.Dto.EtatCredit;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ICredit {

    Optional<Credit> save(Credit credit);
    List<Credit> getAllCredits();

    boolean changeEtatCredit(int numero, EtatCredit etat);

    List<Credit> findByEtat(EtatCredit etat);

    List<Credit> findByDate(LocalDate date);
}
