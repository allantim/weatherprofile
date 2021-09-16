package org.tim.weathertracker.dataprovider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.tim.weathertracker.core.AppProperties;
import org.tim.weathertracker.core.entities.openweather.OpenWeatherDto;
import org.tim.weathertracker.core.repository.OpenWeatherClient;

import java.net.URI;


@Repository
@Slf4j
public class OpenWeatherClientImpl implements OpenWeatherClient {

    private static final String APPID = "APPID";
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

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(appProperties.getOpenWeatherUrl())
            .queryParam(APPID, appProperties.getOpenWeatherApiKey())
            .queryParam(CITY, String.format("%s,AU", cityCode))
            .queryParam( UNITS, "metric");
        URI uri = builder.build().toUri();
        log.info("OpenWeatherUri {}", uri);
        ResponseEntity<OpenWeatherDto> response = restTemplate.exchange(
            builder.build().toUri(),
            HttpMethod.GET,
            null,
            OpenWeatherDto.class
        );
        return response.getBody();
    }
}
