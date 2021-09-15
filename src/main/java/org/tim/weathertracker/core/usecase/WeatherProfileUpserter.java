package org.tim.weathertracker.core.usecase;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.tim.weathertracker.core.AppProperties;
import org.tim.weathertracker.core.entities.CityWeather;
import org.tim.weathertracker.core.entities.UserData;
import org.tim.weathertracker.core.entities.UserProfile;
import org.tim.weathertracker.core.entities.dto.WeatherProfileRequestDto;
import org.tim.weathertracker.core.entities.dto.GeneralResponseDto;
import org.tim.weathertracker.core.repository.CityWeatherRepository;
import org.tim.weathertracker.core.repository.UserDataRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class WeatherProfileUpserter {

    private final UserDataRepository userDataRepository;
    private final CityWeatherRepository cityWeatherRepository;
    private final AppProperties appProperties;

    public WeatherProfileUpserter(UserDataRepository userDataRepository,
                                  CityWeatherRepository cityWeatherRepository,
                                  AppProperties appProperties) {
        this.userDataRepository = userDataRepository;
        this.cityWeatherRepository = cityWeatherRepository;
        this.appProperties = appProperties;
    }

    public GeneralResponseDto upsert(UUID userId, WeatherProfileRequestDto requestDto) {
        if (StringUtils.isBlank(requestDto.getNickname())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nickname is not populated");
        }
        if (CollectionUtils.isEmpty(requestDto.getCities())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cities are not populated");
        }
        Set<String> unknownCities = requestDto.getCities().stream()
            .filter(city -> !appProperties.getSupportedCities().contains(city))
            .collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(unknownCities)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The following cities are not supported %s", unknownCities));
        }

        UserData userData = userDataRepository.findByUserId(userId);
        if (userData == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with userID %s not found", userId));
        }


        if (userData.getUserProfiles() == null) {
            userData.setUserProfiles(new HashSet<>());
        }
        // Find cities
        List<CityWeather> cityWeatherList = cityWeatherRepository.findAllById(new ArrayList<>(requestDto.getCities()));
        userData.getUserProfiles().add(
            UserProfile.builder()
                .cityWeathers(new HashSet<>(cityWeatherList))
                .nickname(requestDto.getNickname())
                .build()
        );
        // TODO this is not the most efficient way to save
        // UserProfileRepository saver would be better.
        // This could end up with N+1 problem but no time to check
        userDataRepository.save(userData);

        return GeneralResponseDto.builder().status("saved").build();
    }
}
