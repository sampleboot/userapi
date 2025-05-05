INSERT INTO "user" (user_id, last_name, first_name, email_address,
                    supervisor_user_id, title_text, create_user_id, create_dttm,
                    update_user_id, update_dttm)
VALUES ('super123', 'Smith', 'Anna', 'anna.smith@example.com', null, 'Director', 1, now(), 1, now()),
       ('john123', 'Doe', 'John', 'john.doe@example.com', 'super123', 'Developer', 1, now(), 1, now()),
       ('jane456', 'Roe', 'Jane', 'jane.roe@example.com', 'super123', 'Analyst', 1, now(), 1, now()),
       ('pdutt234', 'Dutt', 'pras', 'pras.dutt@example.com', 'super123', 'Director2', 1, now(), 1, now()),
       ('testuser', 'test', 'user', 'pras.dutt@example.com', 'super123', 'Director2', 1, now(), 1, now());
;
-- Insert sample addresses
INSERT INTO address (
     mobile_phone, other_phone, location, street_address, street_address_2,
    city, state, postal_code, country_code, user_id
) VALUES
      ('123-456-7890', null, 'HQ', '123 Main St', null, 'Washington', 'DC', '20001', 'US', 	2),
       ('234-567-8901', null, 'Remote', '456 Elm St', 'Apt 2', 'Arlington', 'VA', '22201', 'US', 3),
      ( '234-596-8001', null, 'HQ', '123 Main St', 'Apt 2', 'Arlington', 'VA', '22201', 'US', 1);
;
