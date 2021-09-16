package org.tim.weathertracker.core.usecase.weatherprofile;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.tim.weathertracker.core.entities.UserData;
import org.tim.weathertracker.core.entities.dto.WeatherProfileRetrieveResponseDto;
import org.tim.weathertracker.core.repository.UserDataRepository;

import java.util.Set;
import java.util.UUID;

@Component
public class WeatherProfileRetriever {

    private final UserDataRepository userDataRepository;
    private final WeatherProfileAdaptor weatherProfileAdaptor;

    public WeatherProfileRetriever(UserDataRepository userDataRepository,
                                   WeatherProfileAdaptor weatherProfileAdaptor) {
        this.userDataRepository = userDataRepository;
        this.weatherProfileAdaptor = weatherProfileAdaptor;
    }

    public Set<WeatherProfileRetrieveResponseDto> retrieve(UUID userId) {

        UserData userData = userDataRepository.findByUserId(userId);
        if (userData == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with userID %s not found. Cannot delete", userId));
        }

        return weatherProfileAdaptor.adapt(userData.getUserProfiles());
    }
}
