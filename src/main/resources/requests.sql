INSERT INTO users (email, login, name, birthday)
VALUES ('olik@gmail.ru', 'login1', 'olik', '1996-10-20'),
       ('mickhail@ya.ru', 'login2', 'mickhail', '2004-11-29'),
       ('vadim@ya.ru', 'login3', 'vadim', '1997-12-24'),
       ('sveta@ya.ru', 'login4', 'sveta', '2000-01-01');



INSERT INTO films (NAME, DESCRIPTION, RELEASEDATE, DURATION, RATINGMPAID)
VALUES ('Ты мое сокровище', 'корейская мелодрамма с трагическим концом', '2021-01-22', 140, 1),
       ('Поразительное на каждом шагу', 'про перемещение во времени', '2011-03-02', 1680, 4),
       ('Братство клинков', 'история про заговор против династии', '2014-05-01', 147, 3);

-- UPDATE Users SET EMAIL = 'EMAIL@ya.ru', LOGIN = 'login2', NAME = 'mickhail', BIRTHDAY = '2004-11-29' WHERE ID = 2;
-- UPDATE Films SET NAME = 'Аленький цветочек', DESCRIPTION = 'мультфильм о девочке',
--                  RELEASEDATE = '2021-01-22', DURATION = 80, RAITING_ID = 1 WHERE FILM_ID = 1;
