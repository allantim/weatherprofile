package org.tim.weathertracker.dataprovider;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.tim.weathertracker.core.AppProperties;
import org.tim.weathertracker.core.entities.openweather.OpenWeatherDto;
import org.tim.weathertracker.core.repository.OpenWeatherClient;

import java.util.Map;

@Repository
public class OpenWeatherClientImpl implements OpenWeatherClient {

    private static final String APPID = "appid";
    private static final String CITY = "q";
    private static final String UNITS = "units";

    private final RestTemplate restTemplate;
    private final AppProperties appProperties;

    public OpenWeatherClientImpl(RestTemplate restTemplate,
                                 AppProperties appProperties) {
        this.restTemplate = restTemplate;
        this.appProperties = appProperties;
    }

    @Override
    public OpenWeatherDto retrieve(String cityCode) {
        return restTemplate.getForObject(
            appProperties.getOpenWeatherUrl(),
            OpenWeatherDto.class,
            Map.of(
                APPID, appProperties.getOpenWeatherApiKey(),
                CITY, String.format("%s,AU", cityCode),
                UNITS, "metric"
            )
        );
    }
}
