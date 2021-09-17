package org.tim.weathertracker.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@Slf4j
@EnableWebMvc
public class WebConfig {

    @Bean
    public RestTemplate restClient(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
            // TODO set timeouts
            .build();
    }
}
