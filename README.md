# Rat
Java spring boot application with React frontend implementing simple auction portal.
## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info
The project application allows user to view available offers and, for logged in users, to create and delete previously created offers
## Technologies
Project is created with:
* Java 15
* Spring boot 2.4.0
* Maven
* PostgreSQL
* ReactJS
## Setup

Default database username is 'postgres' and password is 'root', database port is 5432
By default database is cleared and recreated after every backend application start.
To disable this change
```
spring.datasource.initialization-mode=always
```
to
```
spring.datasource.initialization-mode=never
```
in application.properties file

To run backend application use:
```
$ git clone "https://github.com/maciejp12/postsapp.git"
$ cd postsapp/
$ ./mvnw spring-boot:run
```
To run react application:

```
$ cd src/js
$ npm start
```
Default frontend application port is 3000.
