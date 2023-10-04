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

    public void deleteAgence(String code) throws AgenceException {
        try {
            if (code == null) {
                throw new AgenceException("Votre code est Null");
            } else if (agenceService.delete(code) == 1) {
                System.out.println("Agence deleted avec binajah.");
            }
        } catch (AgenceException e) {
            System.err.println("Une exception AgenceException s'est produite : " + e.getMessage());
        }
    }





}
