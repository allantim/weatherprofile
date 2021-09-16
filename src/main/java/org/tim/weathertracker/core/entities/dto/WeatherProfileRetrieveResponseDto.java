package org.tim.weathertracker.core.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherProfileRetrieveResponseDto {
    private String nickname;
    private List<CityWeatherResponseDto> cities;
}
