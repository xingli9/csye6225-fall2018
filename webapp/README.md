Cloud-NativeWebService

Spring boot framework with mariadb application

Feature:
• REST APIs
Get    /currentTime
Post   /user/register
• Http Basic Authentication 
• User password to be stored securely using BCrypt password hashing scheme

Steps to setup, run and test application

1.	git clone https://github.com/LeoChengLeo/csye6225-fall2018.git

2.	Open intellj IDE and open a new maven project in csye6225-fall2018/webapp/Csye6225Webapp/ working directory

3.	Change your database username and password on application.properties file for mariadb connection.

spring.jpa.hibernate.ddl-auto=create
spring.datasource.url=jdbc:mariadb://localhost:3306/userRegister
spring.datasource.username=YourUserName
spring.datasource.password=YourPassword
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver


4.	After running your web application , you can use curl command to test the services.

Register for the service:
curl  ‘localhost:8080?username=YourEmail&password=YouPassword’

Check your registration:
curl  ‘localhost:8080?/user’

Assess the service by providing auth header:
curl  -u username:password “localhost:8080?/currentTime”


