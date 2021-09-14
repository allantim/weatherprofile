package org.tim.weathertracker.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tim.weathertracker.core.entities.CityWeather;

public interface CityWeatherRepository extends JpaRepository<CityWeather, String> {
}
