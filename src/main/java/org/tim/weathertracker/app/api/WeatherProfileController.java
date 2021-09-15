package org.tim.weathertracker.app.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tim.weathertracker.core.entities.UserProfile;
import org.tim.weathertracker.core.entities.dto.WeatherProfileRequestDto;
import org.tim.weathertracker.core.entities.dto.GeneralResponseDto;
import org.tim.weathertracker.core.usecase.weatherprofile.WeatherProfileDeleter;
import org.tim.weathertracker.core.usecase.weatherprofile.WeatherProfileRetriever;
import org.tim.weathertracker.core.usecase.weatherprofile.WeatherProfileUpserter;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1")
@Slf4j
public class WeatherProfileController {

    private final WeatherProfileUpserter weatherProfileUpserter;
    private final WeatherProfileDeleter weatherProfileDeleter;
    private final WeatherProfileRetriever weatherProfileRetriever;

    public WeatherProfileController(WeatherProfileUpserter weatherProfileUpserter,
                                    WeatherProfileDeleter weatherProfileDeleter,
                                    WeatherProfileRetriever weatherProfileRetriever) {
        this.weatherProfileUpserter = weatherProfileUpserter;
        this.weatherProfileDeleter = weatherProfileDeleter;
        this.weatherProfileRetriever = weatherProfileRetriever;
    }

    @GetMapping("/user/{userId}/profile")
    public Set<UserProfile> retrieveUserWeatherProfile(@PathVariable("userId") UUID userId) {
        // TODO maybe I should be returning a DTO here, not the entity
        return weatherProfileRetriever.retrieve(userId);
    }

    @PostMapping("/user/{userId}/profile")
    public GeneralResponseDto createOrUpdateWeatherProfile(@PathVariable("userId") UUID userId,
                                                           @RequestBody WeatherProfileRequestDto requestDto) {
        return weatherProfileUpserter.upsert(userId, requestDto);
    }

    @DeleteMapping("/user/{userId}/profile/{nickname}")
    public GeneralResponseDto deleteWeatherProfileWithNickName(@PathVariable("userId") UUID userId,
                                                               @PathVariable("nickname") String nickname) {
        return weatherProfileDeleter.delete(userId, nickname);
    }
}
