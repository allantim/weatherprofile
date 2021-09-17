package org.tim.weathertracker.core.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "city_weather")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"name"})
public class CityWeather {

    @Id
    @Column(name="name")
    private String name;
    @Column(name="temp")
    private float temp;
    @Column(name="feels_like")
    private float feelsLike;
    @Column(name="temp_min")
    private float tempMin;
    @Column(name="temp_max")
    private float tempMax;
    @Column(name="pressure")
    private int pressure;
    @Column(name="humidity")
    private int humidity;
    @Column(name="wind_speed")
    private float windSpeed;
    @Column(name="weather_main")
    private String weatherMain;

}
