package org.example.Dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AffecterEmploye {
    private int transfert_id;
    private LocalDate transfert_date;
    private Employe employe;
    private Agence agence;
}
