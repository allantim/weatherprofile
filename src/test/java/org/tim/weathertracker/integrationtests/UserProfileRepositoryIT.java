package org.tim.weathertracker.integrationtests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import org.tim.weathertracker.core.entities.UserData;
import org.tim.weathertracker.core.entities.UserProfile;
import org.tim.weathertracker.core.repository.CityWeatherRepository;
import org.tim.weathertracker.core.repository.UserDataRepository;
import org.tim.weathertracker.core.repository.UserProfileRepository;
import org.tim.weathertracker.core.usecase.cityscheduler.ScheduledWeatherRetriever;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@Slf4j
class UserProfileRepositoryIT {

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
    private UserProfileRepository testee;

    @Autowired
    private CityWeatherRepository cityWeatherRepository;

    @Autowired
    private UserDataRepository userDataRepository;

    @MockBean
    private ScheduledWeatherRetriever scheduledWeatherRetriever;

    @BeforeEach
    void setUp() {
        testee.deleteAll();
        cityWeatherRepository.deleteAll();

        CityWeather cityWeather1 = CityWeather.builder()
            .name("SYDNEY")
            .temp(1.1f)
            .humidity(111)
            .feelsLike(11.1f)
            .tempMax(11.2f)
            .tempMin(11.12f)
            .pressure(1111)
            .weatherMain("Cloudy")
            .windSpeed(11.11f)
            .build();

        CityWeather cityWeather2 = CityWeather.builder()
            .name("BRISBANE")
            .temp(2.2f)
            .humidity(222)
            .feelsLike(22.1f)
            .tempMax(22.2f)
            .tempMin(22.12f)
            .pressure(2222)
            .weatherMain("Sunny")
            .windSpeed(22.22f)
            .build();

        CityWeather cityWeather3 = CityWeather.builder()
            .name("CANBERRA")
            .temp(3.3f)
            .humidity(333)
            .feelsLike(33.1f)
            .tempMax(33.2f)
            .tempMin(33.12f)
            .pressure(333)
            .weatherMain("BloodyFreezing")
            .windSpeed(33.33f)
            .build();


        cityWeatherRepository.saveAll(
            List.of(
                cityWeather1, cityWeather2, cityWeather3
            )
        );

        UserData userData1 = UserData.builder()
            .name("dave")
            .userId(UUID.fromString("0970eba6-77be-4288-85b3-7db29f61897e"))
            .email("d@d.com")
            .build();
        userData1.setUserProfiles(new HashSet<>());
        userData1.addUserProfile(
                UserProfile.builder()
                .nickname("HolidayList")
                .userData(userData1)
                .cityWeathers(Set.of(cityWeather1, cityWeather2))
                .build());

        userData1.addUserProfile(
            UserProfile.builder()
                .nickname("OtherList")
                .userData(userData1)
                .cityWeathers(Set.of(cityWeather2, cityWeather3))
                .build());

        userDataRepository.save(userData1);
    }

    @Test
    void retrieveAllThenDelete() {
        List<UserProfile> result = testee.findAll();
        assertThat(result).hasSize(2);

        UserProfile aProfile = result.get(0);
        Long anId = aProfile.getId();

        aProfile.dismissUserData();

        testee.deleteById(anId);

        result = testee.findAll();
        assertThat(result).hasSize(1);

    }

}
