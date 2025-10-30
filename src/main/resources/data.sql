CREATE TABLE IF NOT EXISTS category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS book (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES category(id)
);


INSERT INTO category (category) VALUES ('Фантастика');
INSERT INTO category (category) VALUES ('Романтика');
INSERT INTO category (category) VALUES ('Детектив');

INSERT INTO book (title, author, category_id) VALUES ('1984', 'Джордж Оруэлл', 1);
INSERT INTO book (title, author, category_id) VALUES ('Гордость и предубеждение', 'Джейн Остин', 2);
INSERT INTO book (title, author, category_id) VALUES ('Убийство в Восточном экспрессе', 'Агата Кристи', 3);
INSERT INTO book (title, author, category_id) VALUES ('Мастер и Маргарита', 'Михаил Булгаков', 1);
INSERT INTO book (title, author, category_id) VALUES ('Анна Каренина', 'Лев Толстой', 2);
INSERT INTO book (title, author, category_id) VALUES ('Собака Баскервилей', 'Артур Конан Дойл', 3);
INSERT INTO book (title, author, category_id) VALUES ('451 градус по Фаренгейту', 'Рэй Брэдбери', 1);
INSERT INTO book (title, author, category_id) VALUES ('Грозовой перевал', 'Эмили Бронте', 2);
INSERT INTO book (title, author, category_id) VALUES ('Маленький принц', 'Антуан де Сент-Экзюпери', 1);
INSERT INTO book (title, author, category_id) VALUES ('Преступление и наказание', 'Федор Достоевский', 3);