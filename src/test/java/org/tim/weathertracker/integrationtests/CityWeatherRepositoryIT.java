package org.tim.weathertracker.integrationtests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.tim.weathertracker.core.entities.CityWeather;
import org.tim.weathertracker.core.repository.CityWeatherRepository;
import org.tim.weathertracker.core.usecase.ScheduledWeatherRetriever;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@Slf4j
class CityWeatherRepositoryIT {

	@Container
	static MySQLContainer<?> dbContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
		.withUsername("weatheruser")
		.withPassword("ABC")
		.withDatabaseName("weathertracker");

	@DynamicPropertySource
	static void propertySource(DynamicPropertyRegistry registry) {
		log.info("Container URL: {}", dbContainer.getJdbcUrl());
		registry.add("spring.datasource.url", () -> dbContainer.getJdbcUrl());
		registry.add("spring.datasource.password", () -> dbContainer.getPassword());
		registry.add("spring.datasource.username", () -> dbContainer.getUsername());

	}

	@Autowired
	private CityWeatherRepository cityWeatherRepository;

	@MockBean
	private ScheduledWeatherRetriever scheduledWeatherRetriever;

	@BeforeEach
	void setUp() {
		cityWeatherRepository.deleteAll();

		cityWeatherRepository.saveAll(
			List.of(
				CityWeather.builder()
					.name("SYDNEY")
					.temp(1.1f)
					.humidity(111)
					.feelsLike(11.1f)
					.tempMax(11.2f)
					.tempMin(11.12f)
					.pressure(1111)
					.weatherMain("Cloudy")
					.windSpeed(11.11f)
					.build(),
				CityWeather.builder()
					.name("BRISBANE")
					.temp(2.2f)
					.humidity(222)
					.feelsLike(22.1f)
					.tempMax(22.2f)
					.tempMin(22.12f)
					.pressure(2222)
					.weatherMain("Sunny")
					.windSpeed(22.22f)
					.build(),
				CityWeather.builder()
					.name("CANBERRA")
					.temp(3.3f)
					.humidity(333)
					.feelsLike(33.1f)
					.tempMax(33.2f)
					.tempMin(33.12f)
					.pressure(333)
					.weatherMain("BloodyFreezing")
					.windSpeed(33.33f)
					.build()
			)
		);
	}

	@Test
	void retrieveAll() {
		List<CityWeather> result = cityWeatherRepository.findAll();
		assertThat(result).hasSize(3);
		assertThat(result.stream().map(CityWeather::getName).collect(Collectors.toSet())).containsExactlyInAnyOrder("CANBERRA", "BRISBANE", "SYDNEY");
	}

	@Test
	void findBy() {
		List<CityWeather> result = cityWeatherRepository.findAllById(List.of("CANBERRA", "BRISBANE"));
		assertThat(result).hasSize(2);
		assertThat(result.stream().map(CityWeather::getName).collect(Collectors.toSet())).containsExactlyInAnyOrder("CANBERRA", "BRISBANE");
	}

	@Test
	void updateMany() {
		cityWeatherRepository.save(CityWeather.builder()
			.name("CANBERRA")
			.temp(4.4f)
			.humidity(444)
			.feelsLike(44.1f)
			.tempMax(44.2f)
			.tempMin(44.12f)
			.pressure(444)
			.weatherMain("BloodyFreezing")
			.windSpeed(44.44f)
			.build());
		Optional<CityWeather> result = cityWeatherRepository.findById("CANBERRA");
		assertThat(result).isNotEmpty();
		assertThat(result.get().getWindSpeed()).isEqualTo(44.44f);
	}

}
