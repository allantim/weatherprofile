package org.tim.weathertracker.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;

@ConfigurationProperties(prefix = "app")
@Component
@Setter
@Getter
@NoArgsConstructor
public class AppProperties {
    private Duration retrieveWaitDuration;
    private Set<String> supportedCities;
}
