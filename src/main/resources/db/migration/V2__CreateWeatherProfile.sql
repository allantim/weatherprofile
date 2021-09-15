CREATE TABLE IF NOT EXISTS weathertracker.city_weather (
    `name` VARCHAR(20) NOT NULL PRIMARY KEY,
    `temp` DECIMAL(6,2) NOT NULL,
    `feels_like` DECIMAL(6,2) NOT NULL,
    `temp_min` DECIMAL(6,2) NOT NULL,
    `temp_max` DECIMAL(6,2) NOT NULL,
    `pressure` INTEGER NOT NULL,
    `humidity` INTEGER NOT NULL,
    `wind_speed` DECIMAL(6,2) NOT NULL,
    `weather_main` VARCHAR(80),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)  ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS weathertracker.profile_city (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_profile_id BIGINT NOT NULL,
    city_name VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_user_city UNIQUE (user_profile_id, city_name),
    CONSTRAINT fk_user_profile FOREIGN KEY (user_profile_id) REFERENCES user_profile(id),
    CONSTRAINT fk_city_weather FOREIGN KEY (city_name) REFERENCES city_weather(`name`)
)  ENGINE=INNODB;

