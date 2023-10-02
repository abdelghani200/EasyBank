package org.example.Interface;

import org.example.Dto.Mission;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface IMission {

    Optional<Mission> save(Mission mission);

    Optional<Mission> delete(Mission mission);

    List<Mission> readMission();

    Optional<Mission> findMissionByCode(String code);

    Optional<Mission> update(Mission mission);

}
