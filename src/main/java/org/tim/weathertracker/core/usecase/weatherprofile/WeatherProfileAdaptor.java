package org.tim.weathertracker.core.usecase.weatherprofile;

import org.springframework.stereotype.Component;
import org.tim.weathertracker.core.entities.UserProfile;
import org.tim.weathertracker.core.entities.dto.CityWeatherResponseDto;
import org.tim.weathertracker.core.entities.dto.WeatherProfileResponseDto;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WeatherProfileAdaptor {

    public Set<WeatherProfileResponseDto> adapt(Set<UserProfile> userProfiles) {
        return userProfiles.stream()
            .map(
                userProfile ->
                    WeatherProfileResponseDto.builder()
                        .nickname(userProfile.getNickname())
                        .cities(
                            userProfile.getCityWeathers().stream()
                                .map(CityWeatherResponseDto::fromEntity)
                                .collect(Collectors.toList())
                        )
                        .build()
            )
            .collect(Collectors.toSet());
    }
}
