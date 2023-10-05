package org.example.Services;

import org.example.Dto.AffecterEmploye;
import org.example.Dto.Employe;
import org.example.Interface.IAffecterEmploye;
import org.example.Interface.IAgence;

import java.util.List;
import java.util.Optional;

public class ServiceAffectEmp {
    private IAffecterEmploye affecterEmployeService;

    public ServiceAffectEmp(IAffecterEmploye affecterEmployeService){
        this.affecterEmployeService = affecterEmployeService;
    }
    public Optional<AffecterEmploye> affecterEmploye(AffecterEmploye affecterEmploye){
        return affecterEmployeService.affecterEmploye(affecterEmploye);
    }

    public List<AffecterEmploye> getHistoriqueAffectation(Employe employe){
        return affecterEmployeService.getHistoriqueAffectation(employe);
    }

}
