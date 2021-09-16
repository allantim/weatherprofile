package org.tim.weathertracker.integrationtests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.tim.weathertracker.core.entities.CityWeather;
import org.tim.weathertracker.core.entities.UserData;
import org.tim.weathertracker.core.entities.dto.CreateUserDataResponseDto;
import org.tim.weathertracker.core.entities.dto.GeneralResponseDto;
import org.tim.weathertracker.core.entities.dto.WeatherProfileCreateRequestDto;
import org.tim.weathertracker.core.entities.dto.WeatherProfileRetrieveResponseDto;
import org.tim.weathertracker.core.repository.CityWeatherRepository;
import org.tim.weathertracker.core.usecase.cityscheduler.ScheduledWeatherRetriever;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Example test
 */
@SpringBootTest (
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Testcontainers
@Slf4j
public class CreateUserAndProfilesIT {

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
    private TestRestTemplate restTemplate;

    @Autowired
    private CityWeatherRepository cityWeatherRepository;

    // Mock this out so that we do not attempt retrieves from OpenWeather during test
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
    public void createUser_addProfile_modify_delete(){
        // Create User
        ResponseEntity<CreateUserDataResponseDto> createUserDataResponseDto = restTemplate.postForEntity(
            "/api/v1/user",
            UserData.builder()
                .name("tim")
                .email("tim@t2.com")
                .build(),
            CreateUserDataResponseDto.class
        );
        UUID userId = createUserDataResponseDto.getBody().getUserId();
        assertThat(userId).isNotNull();

        // Add Profile
        ResponseEntity<GeneralResponseDto> profileResponse = restTemplate.postForEntity(
            String.format("/api/v1/user/%s/profile", userId),
            WeatherProfileCreateRequestDto.builder()
                .nickname("HOLIDAY")
                .cities(Set.of("SYDNEY", "CANBERRA"))
                .build(),
            GeneralResponseDto.class
        );
        assertThat(profileResponse.getBody().getStatus()).isEqualTo("saved");

        ResponseEntity<WeatherProfileRetrieveResponseDto[]> result =  restTemplate.getForEntity(String.format("/api/v1/user/%s/profile", userId), WeatherProfileRetrieveResponseDto[].class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(result.getBody()[0].getNickname()).isEqualTo("HOLIDAY");
        assertThat(result.getBody()[0].getCities()).hasSize(2);
    }
}
