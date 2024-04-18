<center><font size="3" face="Georgia"> <h3> "BookManagement"
</h3></font>
</center>

## Overview:
-  The BookManagement application is a small console application based on the Representational state Transfer software architecture. 
- It Implements a REST API for a news service


## Features:
- find one book by its title and author,
- find a list of books by category name,
- create a book,
- update book information,
- delete a book by ID.


## Prerequisites
- Java 17
- Maven (for building the application)
- Spring Boot 3.2.3
- Docker Desktop

## Setup and Installation
- Clone the repository:
- git clone [https://github.com/Katas77]
- Navigate to the project directory:
- cd contacts-application
- Build the application using Maven:
- mvn clean install 
- Run the application:
- For general use:
- - Work with the database occurs through Spring Boot Data JPA and org.postgresql
- - Launch and configure the database via Docker
- - To run using Docker, you need to enter the following commands in the terminal:
- - cd docker   
- - docker-compose up


____

### Usage
- A service layer is implemented, in which the method of searching a book by title and author is cached in Redis, based on the book title and author's name (it is necessary to use these fields as a key), and the method of searching a collection of books by category name is cached by category name.

- The service layer implements methods for creating, updating, and deleting a book. These methods trigger the process of deleting all cached entities related to changes in the state of your model that are populated by entity lookup.

### BookManagement
- BookManagement are managed through a simple command-line interface.
- Input errors are handled gracefully, with prompts for correct input.

## Technologies used:

- Java
- Spring Boot
- Docker
- Spring Boot Data JPA
- Redis
- Spring Boot starter-cache


____
✉ Почта для обратной связи:
<a href="">krp77@mail.ru</a>