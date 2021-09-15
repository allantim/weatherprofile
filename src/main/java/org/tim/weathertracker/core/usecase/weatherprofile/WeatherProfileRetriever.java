package org.tim.weathertracker.core.usecase.weatherprofile;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.tim.weathertracker.core.entities.UserData;
import org.tim.weathertracker.core.entities.UserProfile;
import org.tim.weathertracker.core.repository.UserDataRepository;

import java.util.Set;
import java.util.UUID;

@Component
public class WeatherProfileRetriever {

    private final UserDataRepository userDataRepository;

    public WeatherProfileRetriever(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    public Set<UserProfile> retrieve(UUID userId) {

        UserData userData = userDataRepository.findByUserId(userId);
        if (userData == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with userID %s not found. Cannot delete", userId));
        }

        return userData.getUserProfiles();
    }
}
