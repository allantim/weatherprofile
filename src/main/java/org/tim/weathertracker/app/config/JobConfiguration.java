package org.tim.weathertracker.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.tim.weathertracker.core.AppProperties;
import org.tim.weathertracker.core.usecase.cityscheduler.ScheduledWeatherRetriever;

import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
@EnableScheduling
public class JobConfiguration implements SchedulingConfigurer {
    private final AppProperties appProperties;
    private final ScheduledWeatherRetriever scheduledWeatherRetriever;

    @Autowired
    public JobConfiguration(AppProperties appProperties, ScheduledWeatherRetriever scheduledWeatherRetriever) {
        this.appProperties = appProperties;
        this.scheduledWeatherRetriever = scheduledWeatherRetriever;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        PeriodicTrigger trigger = new PeriodicTrigger(appProperties.getRetrieveInterval(), SECONDS);
        trigger.setFixedRate(true);
        trigger.setInitialDelay(appProperties.getRetrieveInterval() * 1000); // ms
        TriggerTask releaseTask = new TriggerTask(scheduledWeatherRetriever, trigger);
        scheduledTaskRegistrar.addTriggerTask(releaseTask);
    }
}
