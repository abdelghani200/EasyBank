package org.example.Services;

import org.example.Dto.Agence;
import org.example.Exception.AgenceException;
import org.example.Interface.IAgence;

import java.util.Optional;

public class ServiceAgence {
    private IAgence agenceService;

    public ServiceAgence(IAgence agenceService){
        this.agenceService = agenceService;
    }

    public Agence createAgence(Agence agence) throws AgenceException {
        try {
            Optional<Agence> optionalAgence = agenceService.save(agence);
            System.out.println(optionalAgence);
            if (optionalAgence.isPresent()) {
                return optionalAgence.get();
            } else {
                throw new AgenceException("Erreur lors de la création de l'agence : l'opération de sauvegarde a échoué.");
            }
        } catch (AgenceException e) {
            throw new AgenceException("Erreur lors de la création de l'agence.", e);
        }
    }


}
