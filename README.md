# java-filmorate
Приложение Filmorate по оценке фильмов.
## **ТЗ 11 промежуточное
В приложение Filmorate добавлены возможности лайкать фильмы, получать список лучших фильмов, добавлять друг друга в друзья.
### * * Описание ER-диаграммы:
Схема БД в виде ER диаграммы представлена ссылка

![диаграмма] (https://github.com/Olik1/java-filmorate/blob/develop/schema.png)

* `Таблица Users` - модель пользователя
* `Таблица Friendship` - инфо о дружбе между двумя пользователями.
Если статус не подвержден: столбец status - false, подвержден - true
* `Таблица Films` - модель фильма
* `Таблица Likes` - инфо о пользователях поставивших лайк фильму
* `Таблица Film_Genre` - инфо о жанрах фильма
* `Таблица Genre` - список жанров
* `Таблица Rating_MPA` - инфо о  возрастном ограничении для фильма

####  * * Проверка в песочнице связей таблицы
```
CREATE TABLE Users  (
USER_ID INT,
EMAIL varchar,
LOGIN varchar,
NAME varchar,
BIRTHDAY date
);
INSERT INTO Users (USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (1, 'olik@ya.ru', 'Olik13', 'Olga', '1999-04-24');

INSERT INTO Users (USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (2, 'Danya@ya.ru', 'Daniil', 'Danya', '2003-04-22');

CREATE TABLE Films   (
FILM_ID INT,
NAME varchar,
DESCRIPTION varchar,
RELEASE_Date date,
DURATION INT,
RAITING_ID INT
);
INSERT INTO Films (FILM_ID, NAME, DESCRIPTION, RELEASE_Date, DURATION, RAITING_ID) VALUES (1, 'Терминатор', 'Фильм о судном дне', '1999-04-24', 2, 9);
INSERT INTO Films (FILM_ID, NAME, DESCRIPTION, RELEASE_Date, DURATION, RAITING_ID) VALUES (2, 'Терминатор 2', 'Фильм о судном дне 2', '2000-04-24', 2, 9);

CREATE TABLE Likes (
FILM_ID INT,
USER_ID INT
);
INSERT INTO Likes (FILM_ID, USER_ID) VALUES (1, 1);
INSERT INTO Likes (FILM_ID, USER_ID) VALUES (1, 2);
```
Примеры запросов для основных операций приложения:
1. Получение информации о пользователе по ID:
```
   SELECT *
   FROM Users
   WHERE USER_ID = 1
```
2. Получение списка 5 пользователей по убыванию в лексикографическом порядк
```
   SELECT NAME
   FROM Users
   ORDER BY NAME DESC
   LIMIT 5;
```
3. Получение информации о фильме по ID:
```
   SELECT *
   FROM Films
   WHERE FILM_ID = 1
```
4. Выборка 10 новых фильмов по году выпуска:
```
   SELECT NAME,
   EXTRACT(YEAR FROM CAST(RELEASE_Date AS date))
   FROM Films
   ORDER BY RELEASE_Date DESC
   LIMIT 10;
```

