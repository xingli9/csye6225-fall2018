# Spring-oauth2-jpa-example
A simple and basic example  to secure REST APsI with authentication using Spring Boot , Security , OAuth2 and JPA.
Cloud-NativeWebService

Spring boot framework with mariadb application

Feature: • REST APIs Get /currentTime Post /user/register • Http Basic Authentication • User password to be stored securely using BCrypt password hashing scheme

Steps to setup, run and test application

git clone https://github.com/zihaozheng6/csye6225-fall2018.git

Open intellj IDE and open a new maven project in csye6225-fall2018/webapp/Csye6225Webapp/ working directory

Change your database username and password on application.properties file for mariadb connection.

spring.jpa.hibernate.ddl-auto=create spring.datasource.url=jdbc:mariadb://localhost:3306/userRegister spring.datasource.username=YourUserName spring.datasource.password=YourPassword spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

After running your web application , you can use curl command to test the services.
Register for the service: curl ‘localhost:8080?username=YourEmail&password=YouPassword’

Check your registration: curl ‘localhost:8080?/user’

Assess the service by providing auth header: curl -u username:password “localhost:8080?/currentTime”
https://projects.spring.io/spring-security-oauth/docs/oauth2.html
