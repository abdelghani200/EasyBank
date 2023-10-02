package org.example.Interface;

import org.example.Dto.Client;
import org.example.Dto.Employe;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface IEmploye {
    Optional<Employe> findByMatricule(String matricule);
    Optional<List<Employe>> readAllEmployes();

    boolean update(Employe employe);

    Optional<Employe> findByEmailOrNomOrPrenom(String searchValue);
}
