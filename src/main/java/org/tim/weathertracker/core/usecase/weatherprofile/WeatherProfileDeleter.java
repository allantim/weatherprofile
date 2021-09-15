package org.tim.weathertracker.core.usecase.weatherprofile;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.tim.weathertracker.core.AppProperties;
import org.tim.weathertracker.core.entities.UserData;
import org.tim.weathertracker.core.entities.UserProfile;
import org.tim.weathertracker.core.entities.dto.GeneralResponseDto;
import org.tim.weathertracker.core.repository.CityWeatherRepository;
import org.tim.weathertracker.core.repository.UserDataRepository;

import java.util.Optional;
import java.util.UUID;

@Component
public class WeatherProfileDeleter {

    private final UserDataRepository userDataRepository;

    public WeatherProfileDeleter(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    public GeneralResponseDto delete(UUID userId, String nickname) {

        UserData userData = userDataRepository.findByUserId(userId);
        if (userData == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with userID %s not found. Cannot delete", userId));
        }

        Optional<UserProfile> userProfileOpt = userData.getUserProfiles().stream().filter(userProfile -> userProfile.getNickname().equals(nickname)).findFirst();
        if (userProfileOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("UserProfile with nickname %s not found. Cannot delete", nickname));
        }
        userData.getUserProfiles().remove(userProfileOpt.get());

        // TODO this is not the most efficient way to save
        // UserProfileRepository saver would be better.
        // This could end up with N+1 problem but no time to check
        userDataRepository.save(userData);

        return GeneralResponseDto.builder().status("deleted").build();
    }
}
