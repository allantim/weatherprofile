package org.tim.weathertracker.core.usecase.cityscheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tim.weathertracker.core.AppProperties;

@Component
@Slf4j
public class ScheduledWeatherRetriever implements Runnable{

    private final AppProperties appProperties;
    private final CityWeatherGetAndSave cityWeatherGetAndSave;

    public ScheduledWeatherRetriever(AppProperties appProperties,
                                     CityWeatherGetAndSave cityWeatherGetAndSave) {
        this.appProperties = appProperties;
        this.cityWeatherGetAndSave = cityWeatherGetAndSave;
        // Retrieve all City weather as part of start-up
        retrieveAndSaveNoExceptionHandling();
    }

    @Override
    public void run() {
        appProperties.getSupportedCities()
            .parallelStream()
            .forEach(
                city -> {
                    try {
                        cityWeatherGetAndSave.retrieveAndSave(city);
                    } catch (Exception e) {
                        log.error("Failed to retrieveAndSave city [{}]", city, e);
                    }
                });
    }

    private void retrieveAndSaveNoExceptionHandling() {
        appProperties.getSupportedCities()
            .parallelStream()
            .forEach(cityWeatherGetAndSave::retrieveAndSave);
    }
}
