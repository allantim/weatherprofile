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
import org.tim.weathertracker.core.entities.UserProfile;
import org.tim.weathertracker.core.repository.CityWeatherRepository;
import org.tim.weathertracker.core.repository.UserDataRepository;

import java.util.List;
import java.util.Set;
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

    @Autowired
    private CityWeatherRepository cityWeatherRepository;

    @BeforeEach
    void setUp() {
        userDataRepository.deleteAll();

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

        userData1.setUserProfiles(
            Set.of(
                UserProfile.builder()
                    .nickname("HolidayList")
                    .userData(userData1)
                    .cityWeathers(Set.of(cityWeather1, cityWeather2))
                    .build(),
				UserProfile.builder()
					.nickname("OtherList")
					.userData(userData1)
                    .cityWeathers(Set.of(cityWeather2, cityWeather3))
					.build()
            )
        );

        userDataRepository.saveAll(
            List.of(
				userData1,
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

        UserData daveData = result.stream().filter(x->x.getName().equals("dave")).findFirst().get();
        assertThat(daveData.getUserProfiles()).hasSize(2);

        UserProfile daveProfile1 = daveData.getUserProfiles().stream().filter(x->x.getNickname().equals("HolidayList")).findFirst().get();
        assertThat(daveProfile1.getCityWeathers().stream().map(x->x.getName()).collect(Collectors.toSet()))
            .containsExactlyInAnyOrder("SYDNEY", "BRISBANE");

        UserProfile daveProfile2 = daveData.getUserProfiles().stream().filter(x->x.getNickname().equals("OtherList")).findFirst().get();
        assertThat(daveProfile2.getCityWeathers().stream().map(x->x.getName()).collect(Collectors.toSet()))
            .containsExactlyInAnyOrder("CANBERRA", "BRISBANE");


        UserData rickData = result.stream().filter(x->x.getName().equals("rick")).findFirst().get();
        assertThat(rickData.getUserProfiles()).isEmpty();

        assertThat(result.stream().map(UserData::getUserId).collect(Collectors.toSet())).containsExactlyInAnyOrder(UUID.fromString("0970eba6-77be-4288-85b3-7db29f61897e"), UUID.fromString("784a84c6-d4ba-4ae1-a014-f6d1d4825af7"));
    }

}
