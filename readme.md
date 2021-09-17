# Weather Tracker

## Prerequisites for Coding or running in Intellij

- Java 11
- Maven 3.6.0 (or above)
- IntelliJ IDEA with Lombok IDEA Plugin
- Docker installed locally

## Running in Docker containers
You will need 
- Java 11
- Maven 3.6.0 (or above)
- Docker installed locally

### 1. Clone repo.

### 2. Do a maven build
Run the following
```bash
mvn clean verify
```

### 3. Docker build the WeatherTracker
The following will create an image called tim/weathertracker with Tag "latest".

```bash
make app-build
```

### 4. Start MySql in a container.
This will have a network called tim-net
```bash
make db-up
```

### 5. Run the Dockerised WeatherTracker
This will connect to tim-net. Note please wait 30+ seconds for MySql to start.
```bash
make app-run
```

Now verify that it has started:
```bash
docker logs weathertracker
```

You will see that in the logs the server should be started with the OpenWeather calls:
```bash
2021-09-17 00:15:22.668  INFO 1 --- [           main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000490: Using JtaPlatform implementation: [org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform]
2021-09-17 00:15:22.679  INFO 1 --- [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2021-09-17 00:15:23.250  INFO 1 --- [           main] o.t.w.d.OpenWeatherClientImpl            : OpenWeatherUri https://api.openweathermap.org/data/2.5/weather?APPID=2a5fb3ca31cc264d05fa69eeadd72ef8&q=SYDNEY,AU&units=metric
2021-09-17 00:15:24.878  INFO 1 --- [           main] o.t.w.d.OpenWeatherClientImpl            : OpenWeatherUri https://api.openweathermap.org/data/2.5/weather?APPID=2a5fb3ca31cc264d05fa69eeadd72ef8&q=BRISBANE,AU&units=metric
2021-09-17 00:15:25.176  INFO 1 --- [           main] o.t.w.d.OpenWeatherClientImpl            : OpenWeatherUri https://api.openweathermap.org/data/2.5/weather?APPID=2a5fb3ca31cc264d05fa69eeadd72ef8&q=ADELAIDE,AU&units=metric
2021-09-17 00:15:25.488  INFO 1 --- [           main] o.t.w.d.OpenWeatherClientImpl            : OpenWeatherUri https://api.openweathermap.org/data/2.5/weather?APPID=2a5fb3ca31cc264d05fa69eeadd72ef8&q=CANBERRA,AU&units=metric
2021-09-17 00:15:25.792  INFO 1 --- [           main] o.t.w.d.OpenWeatherClientImpl            : OpenWeatherUri https://api.openweathermap.org/data/2.5/weather?APPID=2a5fb3ca31cc264d05fa69eeadd72ef8&q=PERTH,AU&units=metric
2021-09-17 00:15:26.098  INFO 1 --- [           main] o.t.w.d.OpenWeatherClientImpl            : OpenWeatherUri https://api.openweathermap.org/data/2.5/weather?APPID=2a5fb3ca31cc264d05fa69eeadd72ef8&q=DARWIN,AU&units=metric
2021-09-17 00:15:26.684  WARN 1 --- [           main] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning
2021-09-17 00:15:27.043  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path '/weather'
2021-09-17 00:15:27.053  INFO 1 --- [           main] s.a.ScheduledAnnotationBeanPostProcessor : No TaskScheduler/ScheduledExecutorService bean found for scheduled processing
2021-09-17 00:15:27.062  INFO 1 --- [           main] o.t.w.WeathertrackerApplication          : Started WeathertrackerApplication in 9.994 seconds (JVM running for 10.707)
```



### 6. Run Tests in src/test/http/rest-http.http
You can either run these in Intellij by hitting the play button, or copy into Postman.


### 7. Cleanup

Destroy the weathertracker container
```bash
make app-cleanup
```

Destroy MySql.

```bash
make db-down
```

## Running in Intellij
Import into Intellij

Start database.
```bash
make db-up
```
Run WeatherTrackerApplication with a Spring profile of dev.

Run the tests in : src/test/http/rest-http.http
As per instructions above.

Cleanup DB as per above.


## Design Assumptions 
1. This app is heavy on reads, light on writes
2. It is OK for data to be stale for the customer, eventual consistency works fine.

## TODO - SORRY RAN OUT OF TIME
1. Auditing. I just added created_at and updated_at to be automatically updated by MySQL.
2. Look into db transactions to ensure all committed (or rolled back) in a transaction
3. Look into caching. This app is heavy read, light on writes. So CityWeather can be cached, no need to hit the DB all the time.

## Future changes to look into
1. Reactive, although schedulers will work differently.
2. Separate thread pool for the Scheduled Weather loader. 
3. This is not horizontally scalable because each service loads weather into same DB. Maybe City weather loading can be a separate Springboot app or serverless lambda so that we can scale this.
4. Materialized views, since this data is read heavy.