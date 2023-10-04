package org.example.Interface;

import org.example.Dto.AffecterEmploye;
import org.example.Dto.Employe;

import java.util.List;
import java.util.Optional;

public interface IAffecterEmploye {
    Optional<AffecterEmploye> affecterEmploye(AffecterEmploye affecterEmploye);

}
