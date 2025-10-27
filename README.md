# BookManagement 📚 


## Обзор 🌍
- BookManagement — компактное консольное приложение, предназначенное как практическое упражнение по использованию кэширования с Redis. 
- Приложение управляет записями книг (CRUD) и демонстрирует, как кэшировать частые чтения.

---

## Возможности 🧩
- 🔍 Поиск одной книги по заголовку и автору
- 📚 Получение списка книг по имени категории
- 🖋️ Создание новой книги
- 🔄 Обновление информации о книге
- 🗑️ Удаление книги по ID
- ✅ Валидная обработка некорректного ввода в CLI (чёткие сообщения об ошибках)

---

## Технологии 🛠️
- Java 17
- Spring Boot 3.2.3
- Spring Data JPA (H2 для разработческой БД)
- Redis (кэш)
- Docker / Docker Compose
- Maven (сборка)
- Spring Boot Cache

---

## Предварительные требования 🔧
- Java 17 (JDK)
- Maven
- Docker Desktop (или локальный Redis)
- Доступный порт 8080 для H2-console

---

## Установка и запуск 🚀

### 1) Клонировать репозиторий
- git clone https://github.com/Katas77

### 2) Запустить Redis (локально через Docker)
- Рекомендация (быстро):
    - docker run --name redis -p 6379:6379 -d redis:7
- Или использовать docker-compose (пример ниже).

### 3) Собрать и запустить приложение
- mvn clean package
- java -jar target/bookmanagement-0.0.1-SNAPSHOT.jar

### 4) H2 консоль (разработческая БД)
- http://localhost:8080/h2-console  
  (по умолчанию используйте конфигурацию из application.yml)

Пример docker-compose.yml (опционально):
```yaml
version: "3.8"
services:
  redis:
    image: redis:7
    ports:
      - "6379:6379"
  app:
    build: .
    depends_on:
      - redis
    environment:
      SPRING_REDIS_HOST: redis
    ports:
      - "8080:8080"
```

---


## Поведение кэша и рекомендации 🔁

### Как используется кэш в приложении
- Поиск по title+author кэшируется в кэше с именем DATABASE_ENTITY. Ключ — конкатенация title и author.
- Поиск по категории кэшируется в ENTITY_BY_CATEGORY по имени категории.
- При создании/обновлении/удалении книги выполняется инвалидация (очистка) соответствующих кэшей, чтобы не возвращать устаревшие данные.

- Пример безопасного ключа:
```java
@Cacheable(cacheNames = AppCacheProperties.CacheNames.DATABASE_ENTITY, key = "#title + '::' + #author")
public Book findByTitleAndAuthor(String title, String author) { ... }
```



## Контакты ✉
Если нужно — пишите: krp77@mail.ru

