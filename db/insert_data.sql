-- Вмъкване на данни в таблицата roles
INSERT INTO roles (id, name)
VALUES (UUID(), 'User'),
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
                   ranking_id, created_at, is_active)
VALUES (UUID(), 'john_doe', 'John', 'Doe', 'john.doe@example.com', 'hashed_password',
        'https://example.com/photos/john.jpg', 100, (SELECT id FROM roles WHERE name = 'User'),
        (SELECT id FROM rankings WHERE name = 'Junkie'), NOW(), TRUE),
       (UUID(), 'jane_smith', 'Jane', 'Smith', 'jane.smith@example.com', 'hashed_password',
        'https://example.com/photos/jane.jpg', 200, (SELECT id FROM roles WHERE name = 'Organizer'),
        (SELECT id FROM rankings WHERE name = 'Enthusiast'), NOW(), TRUE),
       (UUID(), 'admin_user', 'Admin', 'User', 'admin@example.com', 'hashed_password',
        'https://example.com/photos/admin.jpg', 0, (SELECT id FROM roles WHERE name = 'Jury'),
        (SELECT id FROM rankings WHERE name = 'Master'), NOW(), TRUE);

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
        NOW() + INTERVAL 1 MONTH, TRUE),
       (UUID(), 'Urban Photography', 'Urban', (SELECT id FROM phases WHERE name = 'Phase 1'),
        'https://example.com/covers/urban.jpg', (SELECT user_id FROM users WHERE username = 'jane_smith'), NOW(),
        NOW() + INTERVAL 1 MONTH, TRUE);

-- Вмъкване на данни в таблицата photos
INSERT INTO photos (id, title, story, photo_url, contest_id, user_id, upload_date, is_active)
VALUES (UUID(), 'Sunset in the mountains', 'A beautiful sunset in the mountains.',
        'https://example.com/photos/sunset.jpg', (SELECT id FROM contests WHERE title = 'Nature Photography'),
        (SELECT user_id FROM users WHERE username = 'john_doe'), NOW(), TRUE),
       (UUID(), 'City lights', 'The city comes alive at night.', 'https://example.com/photos/city_lights.jpg',
        (SELECT id FROM contests WHERE title = 'Urban Photography'),
        (SELECT user_id FROM users WHERE username = 'john_doe'), NOW(), TRUE);

-- Вмъкване на данни в таблицата jury_photo_ratings
INSERT INTO jury_photo_ratings (id, photo_id, user_id, score, comment, category_match, review_date, is_active)
VALUES (UUID(), (SELECT id FROM photos WHERE title = 'Sunset in the mountains'),
        (SELECT user_id FROM users WHERE username = 'admin_user'), 9, 'Great composition and lighting!', TRUE, NOW(),
        TRUE),
       (UUID(), (SELECT id FROM photos WHERE title = 'City lights'),
        (SELECT user_id FROM users WHERE username = 'admin_user'), 8, 'Nice use of colors.', TRUE, NOW(), TRUE);

-- Вмъкване на данни в таблицата contests_participation
INSERT INTO contests_participation (id, contest_id, user_id, participation_date, photo_uploaded, is_active, score)
VALUES (UUID(), (SELECT id FROM contests WHERE title = 'Nature Photography'),
        (SELECT user_id FROM users WHERE username = 'john_doe'), NOW(), TRUE, TRUE, 9),
       (UUID(), (SELECT id FROM contests WHERE title = 'Urban Photography'),
        (SELECT user_id FROM users WHERE username = 'john_doe'), NOW(), TRUE, TRUE, 8);
