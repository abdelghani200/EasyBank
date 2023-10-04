package org.example.Dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AffecterEmploye {
    private int transfert_id;
    private LocalDate transfert_date;
    private Integer employe_matricule;
    private String agence_code;
}
