package org.tim.weathertracker.integrationtests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.tim.weathertracker.core.entities.CityWeather;
import org.tim.weathertracker.core.entities.UserData;
import org.tim.weathertracker.core.repository.CityWeatherRepository;
import org.tim.weathertracker.core.repository.UserDataRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@Slf4j
class UserDataRepositoryIT {

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
	private UserDataRepository userDataRepository;

	@BeforeEach
	void setUp() {
		userDataRepository.deleteAll();

		userDataRepository.saveAll(
			List.of(
				UserData.builder()
					.name("dave")
					.userId(UUID.fromString("0970eba6-77be-4288-85b3-7db29f61897e"))
					.email("d@d.com")
					.build(),
				UserData.builder()
					.name("rick")
					.userId(UUID.fromString("784a84c6-d4ba-4ae1-a014-f6d1d4825af7"))
					.email("rick@d.com")
					.build()
			)
		);
	}

	@Test
	void retrieveAll() {
		List<UserData> result = userDataRepository.findAll();
		assertThat(result).hasSize(2);
		assertThat(result.stream().map(UserData::getName).collect(Collectors.toSet())).containsExactlyInAnyOrder("dave", "rick");
		assertThat(result.stream().map(UserData::getUserId).collect(Collectors.toSet())).containsExactlyInAnyOrder(UUID.fromString("0970eba6-77be-4288-85b3-7db29f61897e"), UUID.fromString("784a84c6-d4ba-4ae1-a014-f6d1d4825af7"));
	}

}
