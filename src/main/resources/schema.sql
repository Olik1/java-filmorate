CREATE TABLE IF NOT EXISTS Users
(
    USER_ID  INTEGER PRIMARY KEY AUTO_INCREMENT,
    EMAIL    VARCHAR(100) NOT NULL,
    LOGIN    VARCHAR(100) NOT NULL,
    NAME     VARCHAR(50)  NOT NULL,
    BIRTHDAY DATE         NOT NULL
);
CREATE TABLE IF NOT EXISTS Friendship
(
    USER_ID   INTEGER,
    FRIEND_ID INTEGER,
    STATUS    BOOLEAN,
    PRIMARY KEY (USER_ID, FRIEND_ID),
    FOREIGN KEY (USER_ID) REFERENCES Users (USER_ID) ON DELETE CASCADE,
    FOREIGN KEY (FRIEND_ID) REFERENCES Users (USER_ID) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS Rating_MPA
(
    RAITING_ID INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    MPA_NAME   VARCHAR(50)
);
CREATE TABLE IF NOT EXISTS Genre
(
    GENRE_ID INTEGER PRIMARY KEY AUTO_INCREMENT,
    NAME     VARCHAR(100) NOT NULL
);
CREATE TABLE IF NOT EXISTS Films
(
    FILM_ID      INTEGER PRIMARY KEY AUTO_INCREMENT,
    NAME         VARCHAR(50) NOT NULL,
    DESCRIPTION  VARCHAR(250),
    RELEASE_Date DATE        NOT NULL,
    DURATION     INTEGER     NOT NULL,
    RAITING_ID   INTEGER     NOT NULL,
    FOREIGN KEY (RAITING_ID) REFERENCES Rating_MPA (RAITING_ID) ON DELETE CASCADE

);
CREATE TABLE IF NOT EXISTS Film_Genre
(
    FILM_ID  INTEGER,
    GENRE_ID INTEGER,
    PRIMARY KEY (FILM_ID, GENRE_ID),
    FOREIGN KEY (FILM_ID) REFERENCES Films (FILM_ID) ON DELETE CASCADE,
    FOREIGN KEY (GENRE_ID) REFERENCES Genre (GENRE_ID) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS Likes
(
    FILM_ID INTEGER,
    USER_ID INTEGER,
    PRIMARY KEY (FILM_ID, USER_ID),
    FOREIGN KEY (FILM_ID) REFERENCES Films (FILM_ID) ON DELETE CASCADE,
    FOREIGN KEY (USER_ID) REFERENCES Users (USER_ID) ON DELETE CASCADE
);


