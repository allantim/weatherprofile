package org.tim.weathertracker.core.usecase.cityscheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tim.weathertracker.core.entities.openweather.OpenWeatherDto;
import org.tim.weathertracker.core.repository.CityWeatherRepository;
import org.tim.weathertracker.core.repository.OpenWeatherClient;

@Component
@Slf4j
public class CityWeatherGetAndSave {

    private final OpenWeatherClient openWeatherClient;
    private final CityWeatherRepository cityWeatherRepository;

    public CityWeatherGetAndSave(OpenWeatherClient openWeatherClient,
                                 CityWeatherRepository cityWeatherRepository) {
        this.openWeatherClient = openWeatherClient;
        this.cityWeatherRepository = cityWeatherRepository;
    }

    public void retrieveAndSave(String cityCode) {
        OpenWeatherDto openWeatherDto = openWeatherClient.retrieve(cityCode);
        cityWeatherRepository.save(openWeatherDto.toCityWeather());
    }

}
