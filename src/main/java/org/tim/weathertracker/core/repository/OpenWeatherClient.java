package org.tim.weathertracker.core.repository;

import org.tim.weathertracker.core.entities.openweather.OpenWeatherDto;

public interface OpenWeatherClient {

    OpenWeatherDto retrieve(String cityCode);
}
