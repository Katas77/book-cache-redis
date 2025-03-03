<div align="center"><b>📚 BookManagement 📚</b></div>

## Overview 🌍
## The BookManagement application is a small console-based program built on the Representational State Transfer (REST) architecture. It provides a RESTful API for managing books, offering features such as finding, creating, updating, and deleting books.

## Features 🧩
- Find a single book by its title and author 🔍
- Retrieve a list of books by category name 📚
- Create new books 🖋️
- Update existing book information 🔄
- Delete books by ID 🗑️
## Prerequisites 🔧
- Java 17 💻
- Maven (for building the application) 📦
- Spring Boot 3.2.3 🚂
- Docker Desktop 🐳
- Setup and Installation 🛠️
## Clone the repository:

- git clone https://github.com/Katas77
- Navigate to the project directory:

## Run the application:  
## General usage:  
- Use Spring Boot Data JPA and PostgreSQL for database operations.
- Configure and launch the database using Docker.
- Start Docker services:  
- Go to the docker subdirectory:


## Usage 📖
- The service layer caches searches for books by title and author in Redis, using the book’s title and author’s name as keys. Searches for books by category are also cached based on the category name.
- When creating, updating, or deleting a book, all related cached entities are cleared to reflect changes in the underlying data.
- Book Management 📘
- Books are managed through a simple command-line interface (CLI).
- Input validation ensures graceful handling of incorrect inputs, providing clear feedback to the user.
## Technologies Used 🛠️
Java 🟢
Spring Boot 🚄
Docker 🐋
Spring Boot Data JPA 📝
Redis 🔁
Spring Boot Cache 🕒
📬 Contact Us:Need help or have questions? Reach out at krp77@mail.ru.


____
✉ Почта для обратной связи:
<a href="">krp77@mail.ru</a>