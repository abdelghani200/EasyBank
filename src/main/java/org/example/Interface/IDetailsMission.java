package org.example.Interface;

import org.example.Dto.Employe;
import org.example.Dto.MissionDetails;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IDetailsMission {

    Optional<MissionDetails> NewAffectation(MissionDetails missionDetails);
    Optional<MissionDetails> deleteAffecte(MissionDetails mission);

    int getAffectationsStatistics();

    Map<Employe, List<MissionDetails>> getHistoriqueAffectations();
}
