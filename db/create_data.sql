create database photo_contest;
use photo_contest;

-- Таблица за ролите на потребителите
CREATE TABLE roles
(
    id   CHAR(36) DEFAULT UUID() PRIMARY KEY,
    name VARCHAR(32) UNIQUE NOT NULL
);

-- Таблица за ранкинга на потребителите
CREATE TABLE rankings
(
    id   CHAR(36) DEFAULT UUID() PRIMARY KEY,
    name VARCHAR(32) UNIQUE NOT NULL
);

-- Таблица за потребители (Users)
CREATE TABLE users
(
    user_id       CHAR(36) DEFAULT UUID() PRIMARY KEY,
    username      VARCHAR(50) UNIQUE NOT NULL,
    first_name    VARCHAR(50)        NOT NULL,
    last_name     VARCHAR(50)        NOT NULL,
    email         VARCHAR(50) UNIQUE NOT NULL,
    password      VARCHAR(255)       NOT NULL,
    profile_photo VARCHAR(256)       ,
    points        INT,
    role_id       CHAR(36)           NOT NULL,
    ranking_id    CHAR(36)           NOT NULL,
    created_at    TIMESTAMP,
    is_active     BOOLEAN,
    FOREIGN KEY (role_id) REFERENCES roles (id),
    FOREIGN KEY (ranking_id) REFERENCES rankings (id)
);

-- Таблица за фазите на конкурса
CREATE TABLE phases
(
    id   CHAR(36) DEFAULT UUID() PRIMARY KEY,
    name VARCHAR(32) UNIQUE NOT NULL
);

-- Таблица за конкурси (Contests)
CREATE TABLE contests
(
    id                CHAR(36) DEFAULT UUID() PRIMARY KEY,
    title             VARCHAR(100) UNIQUE NOT NULL,
    category          VARCHAR(100)        NOT NULL,
    phase_id          CHAR(36)            NOT NULL,
    cover_photo_url   VARCHAR(255),
    user_id      CHAR(36)            NOT NULL,
    created_at        TIMESTAMP,
    change_phase_time TIMESTAMP,
    is_active         BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (phase_id) REFERENCES phases (id)
);

-- Таблица за снимки (Photos)
CREATE TABLE photos
(
    id          CHAR(36) DEFAULT UUID() PRIMARY KEY,
    title       VARCHAR(100) NOT NULL,
    story       TEXT         NOT NULL,
    photo_url   VARCHAR(255) NOT NULL,
    contest_id  CHAR(36)     NOT NULL,
    user_id     CHAR(36)     NOT NULL,
    upload_date TIMESTAMP,
    is_active   BOOLEAN,
    FOREIGN KEY (contest_id) REFERENCES contests (id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

-- Таблица за оценки от журито (JuryRatings)
CREATE TABLE jury_photo_ratings
(
    id             CHAR(36) DEFAULT UUID() PRIMARY KEY,
    photo_id       CHAR(36) NOT NULL,
    user_id        CHAR(36) NOT NULL,
    score          INT      NOT NULL,
    comment        TEXT     NOT NULL,
    category_match BOOLEAN,
    review_date    TIMESTAMP,
    is_active      BOOLEAN,
    FOREIGN KEY (photo_id) REFERENCES photos (id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

-- Таблица за участия в конкурси (ContestParticipation)
CREATE TABLE contests_participation
(
    id                 CHAR(36) DEFAULT UUID() PRIMARY KEY,
    contest_id         CHAR(36) NOT NULL,
    user_id            CHAR(36) NOT NULL,
    participation_date TIMESTAMP,
    photo_uploaded     BOOLEAN,
    is_active           BOOLEAN,
    score              INT, -- Крайната оценка след журиране
    FOREIGN KEY (contest_id) REFERENCES contests (id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);
