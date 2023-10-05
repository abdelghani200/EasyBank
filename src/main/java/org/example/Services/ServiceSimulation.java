package org.example.Services;

import org.example.Dto.Simulation;
import org.example.Implementation.ImEmploye;
import org.example.Interface.IAgence;

public class ServiceSimulation {

    private ImEmploye imEmployeService;

    public ServiceSimulation(ImEmploye imEmployeService){
        this.imEmployeService = imEmployeService;
    }

    public double createSimulation(Simulation simulation) {
        double resultat = 0;

        try {
            if (imEmployeService.findByMatricule(simulation.getEmploye().getMatricule()).isPresent()) {
                if (simulation.getPaiementMensuel() != null && simulation.getCapitalEmprunte() != null) {
                    double PaiementMensuel = simulation.getPaiementMensuel();
                    double CapitalEmprunte = simulation.getCapitalEmprunte();
                    int paiementMensuelNum = simulation.getPaiementMensuelNum();

                    resultat = (PaiementMensuel * (1.2 / 12)) / (1 - Math.pow(1 + 1.2 / 12, -paiementMensuelNum));
                } else {
                    System.out.println("Les champs montly_payment et borrowed_capital ne peuvent pas être vides.");
                }
            } else {
                System.out.println("L'employé avec le matricule spécifié n'existe pas.");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la création de la simulation : " + e.getMessage());
        }

        return resultat;
    }


}
