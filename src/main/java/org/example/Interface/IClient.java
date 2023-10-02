package org.example.Interface;

import java.sql.Connection;
import org.example.Dto.Client;
import org.example.Dto.Employe;

import java.util.List;
import java.util.Optional;

public interface IClient{

    Optional<Client> findByCode(String code);
    Optional<List<Client>> readAll();

    boolean deleteClient(String code);

    boolean update(Client client);
    Optional<Client> findByEmailOrNomOrPrenom(String searchValue);

}
