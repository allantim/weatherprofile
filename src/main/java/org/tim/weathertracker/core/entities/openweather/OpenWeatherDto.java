package org.tim.weathertracker.core.entities.openweather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tim.weathertracker.core.entities.CityWeather;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherDto {

    private String name;
    private List<Weather> weather;
    private Main main;
    private Wind wind;


    public CityWeather toCityWeather() {
        return CityWeather.builder()
            .name(name.toUpperCase())
            .temp(main.getTemp())
            .pressure(main.getPressure())
            .tempMin(main.getTemp_min())
            .tempMax(main.getTemp_max())
            .feelsLike(main.getFeels_like())
            .humidity(main.getHumidity())
            .windSpeed(wind.getSpeed())
            .weatherMain(weather.get(0).getMain())
            .build();
    }
}
