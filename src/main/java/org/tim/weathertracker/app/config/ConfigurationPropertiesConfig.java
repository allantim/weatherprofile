package org.tim.weathertracker.app.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.tim.weathertracker.core.AppProperties;

@Configuration
@EnableConfigurationProperties(
    {
        AppProperties.class
    }
)
public class ConfigurationPropertiesConfig {
}
