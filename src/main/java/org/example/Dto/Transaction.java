package org.example.Dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private Double montant;
    private LocalDate dateTransaction;
    private Compte compte;
}