create database photo_contest;
use photo_contest;
-- Таблица за потребители (Users)
CREATE TABLE Users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       role ENUM('Organizer', 'Junkie', 'Jury') NOT NULL,
                       points INT DEFAULT 0, -- Събиране на точки за класиране
                       ranking ENUM('Junkie', 'Enthusiast', 'Master', 'Wise and Benevolent Photo Dictator') DEFAULT 'Junkie',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица за конкурси (Contests)
CREATE TABLE Contests (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          title VARCHAR(100) UNIQUE NOT NULL,
                          category VARCHAR(100) NOT NULL,
                          type ENUM('Open', 'Invitational') NOT NULL,
                          phase ENUM('Phase I', 'Phase II', 'Finished') DEFAULT 'Phase I',
                          phase1_time_limit INT NOT NULL, -- В дни
                          phase2_time_limit INT NOT NULL, -- В часове
                          cover_photo_url VARCHAR(255),
                          organizer_id INT NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (organizer_id) REFERENCES Users(id) ON DELETE CASCADE
);

-- Таблица за снимки (Photos)
CREATE TABLE Photos (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        title VARCHAR(100) NOT NULL,
                        story TEXT NOT NULL,
                        photo_url VARCHAR(255) NOT NULL,
                        contest_id INT NOT NULL,
                        user_id INT NOT NULL,
                        upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (contest_id) REFERENCES Contests(id) ON DELETE CASCADE,
                        FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

-- Таблица за оценки от журито (JuryRatings)
CREATE TABLE JuryRatings (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             photo_id INT NOT NULL,
                             jury_id INT NOT NULL,
                             score INT CHECK (score BETWEEN 1 AND 10),
                             comment TEXT NOT NULL,
                             category_match BOOLEAN DEFAULT TRUE,
                             review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (photo_id) REFERENCES Photos(id) ON DELETE CASCADE,
                             FOREIGN KEY (jury_id) REFERENCES Users(id) ON DELETE CASCADE
);

-- Таблица за участия в конкурси (ContestParticipation)
CREATE TABLE ContestParticipation (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      contest_id INT NOT NULL,
                                      user_id INT NOT NULL,
                                      participation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      photo_uploaded BOOLEAN DEFAULT FALSE,
                                      score INT DEFAULT 0, -- Крайната оценка след журиране
                                      FOREIGN KEY (contest_id) REFERENCES Contests(id) ON DELETE CASCADE,
                                      FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

-- Таблица за покани за участие в конкурс (Invitations)
CREATE TABLE Invitations (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             contest_id INT NOT NULL,
                             user_id INT NOT NULL,
                             invitation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             accepted BOOLEAN DEFAULT FALSE,
                             FOREIGN KEY (contest_id) REFERENCES Contests(id) ON DELETE CASCADE,
                             FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);