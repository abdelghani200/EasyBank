package org.example.Dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends Operation{
    private int id;
    private Compte compteSource;
    private Compte compteDestination;

    public Transaction(String numero, LocalDate dateCreation, double montant, TypeOperation status) {
        super(numero, dateCreation, montant, status);
        setCompteSource(compteSource);
        setCompteDestination(compteDestination);

    }
}
