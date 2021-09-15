package org.tim.weathertracker.core.entities.openweather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.tim.weathertracker.core.entities.CityWeather;


@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherDto {



    public CityWeather toCityWeather() {
        return CityWeather.builder().build();
    }
}
