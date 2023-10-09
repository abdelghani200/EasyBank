package org.example.Dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Simulation {
    private Double capitalEmprunte;
    private Integer paiementMensuelNum;
    private Double resultatSimulatin;
    private Double paiementMensuel;
    private Employe employe;
    private Client client;

}
