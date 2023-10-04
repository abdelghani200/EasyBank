package org.example.Dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credit {
    private String numero;
    private String remarques;
    private EtatCredit etat;
    private LocalDate date;
    private String duree;
    private Simulation simulation;
}