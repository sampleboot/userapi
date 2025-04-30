-- Table: user
CREATE TABLE "user" (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        user_id VARCHAR(30) NOT NULL,
                        last_name VARCHAR(100) NOT NULL,
                        first_name VARCHAR(100) NOT NULL,
                        email_address VARCHAR(255) NOT NULL,
                        supervisor_user_id VARCHAR(30),
                        title_text VARCHAR(100),
                        create_user_id INT NOT NULL,
                        create_dttm TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        update_user_id INT NOT NULL,
                        update_dttm TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table: address
CREATE TABLE address (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         mobile_phone VARCHAR(50),
                         other_phone VARCHAR(50),
                         location VARCHAR(100),
                         street_address VARCHAR(50),
                         street_address_2 VARCHAR(50),
                         city VARCHAR(50),
                         state VARCHAR(20),
                         postal_code VARCHAR(9),
                         country_code VARCHAR(2) DEFAULT 'US',
                         user_id INT,
                         CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE
);
