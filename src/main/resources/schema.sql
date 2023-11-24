CREATE TABLE IF NOT EXISTS Users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     uuid CHAR(36) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP,
    token VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS Phones (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      number VARCHAR(50) NOT NULL,
    citycode VARCHAR(10),
    countrycode VARCHAR(10),
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES Users(id)
    );
