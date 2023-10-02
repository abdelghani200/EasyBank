package org.example.Interface;

import org.example.Dto.Client;
import org.example.Dto.Personne;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;


public interface IPersonne<T extends Personne> {
    Optional<T> save(T personne);


}
