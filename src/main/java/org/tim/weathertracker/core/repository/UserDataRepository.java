package org.tim.weathertracker.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tim.weathertracker.core.entities.UserData;

import java.util.UUID;

public interface UserDataRepository extends JpaRepository<UserData, String> {
    // This is an outer join because user profiles might be empty
    @Query("select u from user_data u LEFT JOIN FETCH u.userProfiles where u.userId=:userId")
    UserData findByUserId(UUID userId);
}
