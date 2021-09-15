package org.tim.weathertracker.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tim.weathertracker.core.entities.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

}
