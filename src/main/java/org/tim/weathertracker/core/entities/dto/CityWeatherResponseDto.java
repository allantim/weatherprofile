package org.tim.weathertracker.core.entities.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tim.weathertracker.core.entities.CityWeather;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityWeatherResponseDto {

    private String name;
    private float temp;
    private float feelsLike;
    private float tempMin;
    private float tempMax;
    private int pressure;
    private int humidity;
    private float windSpeed;
    private String weatherMain;

    public static CityWeatherResponseDto fromEntity(CityWeather cityWeather) {
        return CityWeatherResponseDto.builder()
            .feelsLike(cityWeather.getFeelsLike())
            .humidity(cityWeather.getHumidity())
            .name(cityWeather.getName())
            .temp(cityWeather.getTemp())
            .pressure(cityWeather.getPressure())
            .tempMax(cityWeather.getTempMax())
            .tempMin(cityWeather.getTempMin())
            .weatherMain(cityWeather.getWeatherMain())
            .windSpeed(cityWeather.getWindSpeed())
            .build();
    }
}
