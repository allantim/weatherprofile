package org.tim.weathertracker.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tim.weathertracker.core.entities.UserData;

import java.util.UUID;

public interface UserDataRepository extends JpaRepository<UserData, String> {
    UserData findByUserId(UUID userId);
}
