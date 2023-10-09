package org.example.Services;

import org.example.Dto.Credit;
import org.example.Dto.EtatCredit;
import org.example.Interface.ICredit;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ServiceCredit {
    private ICredit creditService;

    public ServiceCredit(ICredit creditService) {
        this.creditService = creditService;
    }

    public Credit createCredit(Credit credit) throws Exception {
        Optional<Credit> savedCredit = creditService.save(credit);
        if (savedCredit.isPresent()){
            return savedCredit.get();
        } else {
            throw new Exception("Erreur lors de la création du crédit.");
        }
    }

    public boolean changeEtat(int numero, EtatCredit newEtat)
    {
        if (newEtat == EtatCredit.Active || newEtat == EtatCredit.Inactive) {
            return creditService.changeEtatCredit(numero, newEtat);
        } else {
            return false;
        }
    }


    public List<Credit> getAll(){
        return creditService.getAllCredits();
    }

    public List<Credit> findByEtat(EtatCredit etatCredit){
        return creditService.findByEtat(etatCredit);
    }

    public List<Credit> findByDate(LocalDate date){
        return creditService.findByDate(date);
    }

}
