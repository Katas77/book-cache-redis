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



INSERT INTO category (id, category) VALUES (1, 'Фантастика');
INSERT INTO category (id, category) VALUES (2, 'Романтика');
INSERT INTO category (id, category) VALUES (3, 'Детектив');

INSERT INTO book (id, title, author, category_id) VALUES (1, '1984', 'Джордж Оруэлл', 1);
INSERT INTO book (id, title, author, category_id) VALUES (2, 'Гордость и предубеждение', 'Джейн Остин', 2);
INSERT INTO book (id, title, author, category_id) VALUES (3, 'Убийство в Восточном экспрессе', 'Агата Кристи', 3);
INSERT INTO book (id, title, author, category_id) VALUES (4, 'Мастер и Маргарита', 'Михаил Булгаков', 1);
INSERT INTO book (id, title, author, category_id) VALUES (5, 'Анна Каренина', 'Лев Толстой', 2);
INSERT INTO book (id, title, author, category_id) VALUES (6, 'Собака Баскервилей', 'Артур Конан Дойл', 3);
INSERT INTO book (id, title, author, category_id) VALUES (7, '451 градус по Фаренгейту', 'Рэй Брэдбери', 1);
INSERT INTO book (id, title, author, category_id) VALUES (8, 'Грозовой перевал', 'Эмили Бронте', 2);
INSERT INTO book (id, title, author, category_id) VALUES (9, 'Маленький принц', 'Антуан де Сент-Экзюпери', 1);
INSERT INTO book (id, title, author, category_id) VALUES (10, 'Преступление и наказание', 'Федор Достоевский', 3);