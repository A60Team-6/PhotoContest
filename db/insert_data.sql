-- Вмъкване на данни в таблицата roles
INSERT INTO roles (id, name)
VALUES (UUID(), 'Junkie'),
       (UUID(), 'Organizer'),
       (UUID(), 'Jury');

-- Вмъкване на данни в таблицата rankings
INSERT INTO rankings (id, name)
VALUES (UUID(), 'Junkie'),
       (UUID(), 'Enthusiast'),
       (UUID(), 'Master'),
       (UUID(), 'WiseAndBenevolentPhotoDictator');

-- Вмъкване на данни в таблицата users
INSERT INTO users (user_id, username, first_name, last_name, email, password, profile_photo, points, role_id,
                   ranking_id, created_at, is_active, is_invited)
VALUES (UUID(), 'john_doe', 'John', 'Doe', 'john.doe@example.com',
        '$2a$10$wdyGdi7ZZSFS37C/R8mvru9iei8wjHlhSLqvrbSuLw.zGWMCx8CJ.',
        'http://res.cloudinary.com/deeelijg4/image/upload/v1726549662/l7sezn77rx37ihlmwbsg.jpg', 0,
        (SELECT id FROM roles WHERE name = 'Junkie'),
        (SELECT id FROM rankings WHERE name = 'Junkie'), NOW(), TRUE, FALSE),

       (UUID(), 'mario', 'mario', 'georgiev', 'mario@example.com',
        '$2a$10$wdyGdi7ZZSFS37C/R8mvru9iei8wjHlhSLqvrbSuLw.zGWMCx8CJ.',
        'http://res.cloudinary.com/deeelijg4/image/upload/v1726549662/l7sezn77rx37ihlmwbsg.jpg', 100,
        (SELECT id FROM roles WHERE name = 'Organizer'),
        (SELECT id FROM rankings WHERE name = 'Enthusiast'), NOW(), TRUE, FALSE),


       (UUID(), 'ivan', 'ivan', 'nachkov', 'ivan@example.com',
        '$2a$10$wdyGdi7ZZSFS37C/R8mvru9iei8wjHlhSLqvrbSuLw.zGWMCx8CJ.',
        'http://res.cloudinary.com/deeelijg4/image/upload/v1726549662/l7sezn77rx37ihlmwbsg.jpg', 100,
        (SELECT id FROM roles WHERE name = 'Organizer'),
        (SELECT id FROM rankings WHERE name = 'Enthusiast'), NOW(), TRUE, FALSE),

       (UUID(), 'jane_smith', 'Jane', 'Smith', 'jane.smith@example.com',
        '$2a$10$wdyGdi7ZZSFS37C/R8mvru9iei8wjHlhSLqvrbSuLw.zGWMCx8CJ.',
        'http://res.cloudinary.com/deeelijg4/image/upload/v1726549662/l7sezn77rx37ihlmwbsg.jpg', 200,
        (SELECT id FROM roles WHERE name = 'Organizer'),
        (SELECT id FROM rankings WHERE name = 'Master'), NOW(), TRUE, FALSE),

       (UUID(), 'mariya', 'Mariya', 'Asenova', 'asenova@example.com',
        '$2a$10$wdyGdi7ZZSFS37C/R8mvru9iei8wjHlhSLqvrbSuLw.zGWMCx8CJ.',
        'http://res.cloudinary.com/deeelijg4/image/upload/v1726549662/l7sezn77rx37ihlmwbsg.jpg', 200,
        (SELECT id FROM roles WHERE name = 'Junkie'),
        (SELECT id FROM rankings WHERE name = 'Master'), NOW(), TRUE, FALSE),

       (UUID(), 'petar', 'Petar', 'Ivanov', 'pivanov@example.com',
        '$2a$10$wdyGdi7ZZSFS37C/R8mvru9iei8wjHlhSLqvrbSuLw.zGWMCx8CJ.',
        'http://res.cloudinary.com/deeelijg4/image/upload/v1726549662/l7sezn77rx37ihlmwbsg.jpg', 200,
        (SELECT id FROM roles WHERE name = 'Junkie'),
        (SELECT id FROM rankings WHERE name = 'Master'), NOW(), TRUE, FALSE),

       (UUID(), 'todor', 'Todor', 'Dimitrov', 'tdimitrov@example.com',
        '$2a$10$wdyGdi7ZZSFS37C/R8mvru9iei8wjHlhSLqvrbSuLw.zGWMCx8CJ.',
        'http://res.cloudinary.com/deeelijg4/image/upload/v1726549662/l7sezn77rx37ihlmwbsg.jpg', 200,
        (SELECT id FROM roles WHERE name = 'Junkie'),
        (SELECT id FROM rankings WHERE name = 'Master'), NOW(), TRUE, FALSE),

       (UUID(), 'vasil', 'Vasil', 'Petrov', 'vpetrov@example.com',
        '$2a$10$wdyGdi7ZZSFS37C/R8mvru9iei8wjHlhSLqvrbSuLw.zGWMCx8CJ.',
        'http://res.cloudinary.com/deeelijg4/image/upload/v1726549662/l7sezn77rx37ihlmwbsg.jpg', 0,
        (SELECT id FROM roles WHERE name = 'Junkie'),
        (SELECT id FROM rankings WHERE name = 'Junkie'), NOW(), TRUE, FALSE),

       (UUID(), 'stefan', 'Stefan', 'Georgiev', 'sgeorgiev@example.com',
        '$2a$10$wdyGdi7ZZSFS37C/R8mvru9iei8wjHlhSLqvrbSuLw.zGWMCx8CJ.',
        'http://res.cloudinary.com/deeelijg4/image/upload/v1726549662/l7sezn77rx37ihlmwbsg.jpg', 0,
        (SELECT id FROM roles WHERE name = 'Junkie'),
        (SELECT id FROM rankings WHERE name = 'Junkie'), NOW(), TRUE, FALSE),

       (UUID(), 'ivan23', 'Ivan', 'Aleksandrov', 'ialeksandrov@example.com',
        '$2a$10$wdyGdi7ZZSFS37C/R8mvru9iei8wjHlhSLqvrbSuLw.zGWMCx8CJ.',
        'http://res.cloudinary.com/deeelijg4/image/upload/v1726549662/l7sezn77rx37ihlmwbsg.jpg', 0,
        (SELECT id FROM roles WHERE name = 'Junkie'),
        (SELECT id FROM rankings WHERE name = 'Junkie'), NOW(), TRUE, FALSE),

       (UUID(), 'nikolai', 'Nikolai', 'Stoyanov', 'nstoyanov@example.com',
        '$2a$10$wdyGdi7ZZSFS37C/R8mvru9iei8wjHlhSLqvrbSuLw.zGWMCx8CJ.',
        'http://res.cloudinary.com/deeelijg4/image/upload/v1726549662/l7sezn77rx37ihlmwbsg.jpg', 0,
        (SELECT id FROM roles WHERE name = 'Junkie'),
        (SELECT id FROM rankings WHERE name = 'Junkie'), NOW(), TRUE, FALSE),

       (UUID(), 'georgi', 'Georgi', 'Popov', 'gpopov@example.com',
        '$2a$10$wdyGdi7ZZSFS37C/R8mvru9iei8wjHlhSLqvrbSuLw.zGWMCx8CJ.',
        'http://res.cloudinary.com/deeelijg4/image/upload/v1726549662/l7sezn77rx37ihlmwbsg.jpg', 0,
        (SELECT id FROM roles WHERE name = 'Junkie'),
        (SELECT id FROM rankings WHERE name = 'Junkie'), NOW(), TRUE, FALSE),

       (UUID(), 'dimitar', 'Dimitar', 'Kolev', 'dkolev@example.com',
        '$2a$10$wdyGdi7ZZSFS37C/R8mvru9iei8wjHlhSLqvrbSuLw.zGWMCx8CJ.',
        'http://res.cloudinary.com/deeelijg4/image/upload/v1726549662/l7sezn77rx37ihlmwbsg.jpg', 0,
        (SELECT id FROM roles WHERE name = 'Junkie'),
        (SELECT id FROM rankings WHERE name = 'Junkie'), NOW(), TRUE, FALSE),

       (UUID(), 'mihail', 'Mihail', 'Kostov', 'mkostov@example.com',
        '$2a$10$wdyGdi7ZZSFS37C/R8mvru9iei8wjHlhSLqvrbSuLw.zGWMCx8CJ.',
        'http://res.cloudinary.com/deeelijg4/image/upload/v1726549662/l7sezn77rx37ihlmwbsg.jpg', 0,
        (SELECT id FROM roles WHERE name = 'Junkie'),
        (SELECT id FROM rankings WHERE name = 'Junkie'), NOW(), TRUE, FALSE),

       (UUID(), 'angel', 'Angel', 'Nikolov', 'anikolov@example.com',
        '$2a$10$wdyGdi7ZZSFS37C/R8mvru9iei8wjHlhSLqvrbSuLw.zGWMCx8CJ.',
        'http://res.cloudinary.com/deeelijg4/image/upload/v1726549662/l7sezn77rx37ihlmwbsg.jpg', 0,
        (SELECT id FROM roles WHERE name = 'Junkie'),
        (SELECT id FROM rankings WHERE name = 'Junkie'), NOW(), TRUE, FALSE);


-- Вмъкване на данни в таблицата phases
INSERT INTO phases (id, name)
VALUES (UUID(), 'Phase 1'),
       (UUID(), 'Phase 2'),
       (UUID(), 'Finished');

-- Вмъкване на данни в таблицата contests
INSERT INTO contests (id, title, category, phase_id, cover_photo_url, user_id, created_at, change_phase_time,
                      is_active)
VALUES (UUID(), 'Nature Photography', 'Nature', (SELECT id FROM phases WHERE name = 'Phase 1'),
        'https://example.com/covers/nature.jpg', (SELECT user_id FROM users WHERE username = 'jane_smith'), NOW(),
        DATE_ADD(NOW(), INTERVAL 3 HOUR), TRUE),
       (UUID(), 'Urban Photography', 'Landscapes', (SELECT id FROM phases WHERE name = 'Phase 1'),
        'https://example.com/covers/urban.jpg', (SELECT user_id FROM users WHERE username = 'jane_smith'), NOW(),
        DATE_ADD(NOW(), INTERVAL 3 HOUR), TRUE),
       (UUID(), 'Insects', 'Macro Photography', (SELECT id FROM phases WHERE name = 'Phase 1'),
        'https://example.com/covers/street_art.jpg', (SELECT user_id FROM users WHERE username = 'mario'), NOW(),
        DATE_ADD(NOW(), INTERVAL 3 HOUR), TRUE),
       (UUID(), 'Wildlife Exploration', 'Wildlife', (SELECT id FROM phases WHERE name = 'Phase 1'),
        'https://example.com/covers/wildlife.jpg', (SELECT user_id FROM users WHERE username = 'ivan'), NOW(),
        DATE_ADD(NOW(), INTERVAL 3 HOUR), TRUE),
       (UUID(), 'Animal Kingdom', 'Aerial Photography', (SELECT id FROM phases WHERE name = 'Phase 1'),
        'https://example.com/covers/animals.jpg', (SELECT user_id FROM users WHERE username = 'ivan'), null,
        DATE_ADD(NOW(), INTERVAL 3 HOUR), TRUE),
       (UUID(), 'Spring beauty', 'Seasons', (SELECT id FROM phases WHERE name = 'Phase 1'),
        'https://example.com/covers/architecture.jpg', (SELECT user_id FROM users WHERE username = 'mario'), null,
        DATE_ADD(NOW(), INTERVAL 3 HOUR), TRUE);

-- Вмъкване на данни в таблицата photos
INSERT INTO photos (id, title, story, photo_url, hash, contest_id, user_id, total_score, upload_date, is_active)
VALUES (UUID(), 'Sunset in the mountains', 'A beautiful sunset in the mountains.',
        'https://example.com/photos/sunset.jpg', null
           , (SELECT id FROM contests WHERE title = 'Nature Photography'),
        (SELECT user_id FROM users WHERE username = 'john_doe'), null, NOW(), TRUE),
       (UUID(), 'City lights', 'The city comes alive at night.', 'https://example.com/photos/city_lights.jpg', null,
        (SELECT id FROM contests WHERE title = 'Urban Photography'),
        (SELECT user_id FROM users WHERE username = 'john_doe'), null, NOW(), TRUE);

-- Вмъкване на данни в таблицата jury_photo_ratings
INSERT INTO jury_photo_ratings (id, photo_id, user_id, score, comment, category_match, review_date, is_active)
VALUES (UUID(), (SELECT id FROM photos WHERE title = 'Sunset in the mountains'),
        (SELECT user_id FROM users WHERE username = 'todor'), 9, 'Great composition and lighting!', TRUE, NOW(),
        TRUE),
       (UUID(), (SELECT id FROM photos WHERE title = 'City lights'),
        (SELECT user_id FROM users WHERE username = 'mariya'), 8, 'Nice use of colors.', TRUE, NOW(), TRUE);

-- Вмъкване на данни в таблицата contests_participation
INSERT INTO contests_participation (id, contest_id, user_id, participation_date, photo_uploaded, is_active, score)
VALUES (UUID(), (SELECT id FROM contests WHERE title = 'Nature Photography'),
        (SELECT user_id FROM users WHERE username = 'john_doe'), NOW(), TRUE, TRUE, 9),
       (UUID(), (SELECT id FROM contests WHERE title = 'Urban Photography'),
        (SELECT user_id FROM users WHERE username = 'john_doe'), NOW(), TRUE, TRUE, 8);
