CREATE TABLE IF NOT EXISTS weathertracker.user_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(40) NOT NULL,
    `name` VARCHAR(200) NOT NULL,
    email VARCHAR(320) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp default now() on update now(),
    INDEX(user_id)
)  ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS weathertracker.user_profile (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    nickname VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp default now() on update now(),
    CONSTRAINT unique_nickname_user UNIQUE (user_id, nickname),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user_data(id),
    INDEX(user_id)
)  ENGINE=INNODB;