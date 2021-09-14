package org.tim.weathertracker.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tim.weathertracker.core.entities.UserData;

public interface UserDataRepository extends JpaRepository<UserData, String> {
}
