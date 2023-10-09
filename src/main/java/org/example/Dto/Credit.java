package org.example.Dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credit {
    private Integer numero;
    private Double montant;
    private String remarques;
    private EtatCredit etat;
    private LocalDate date;
    private Integer duree;
    private Simulation simulation;
}
