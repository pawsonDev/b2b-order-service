# b2b-order-service
Spring-Boot application for business online shopping.

## About

This is a project SpringBoot web application. The idea was to build some basic shopping basket web app for online shopping. Companies can buy products using their validated VAT number.

## Database

Database is set in MySql and is ready to start using Docker.

To set up database:
- install Docker
- go to root project file, and go to 'docker' file
- open command line console
- use commands:

   $ docker build -t b2b_image .
   
   $ docker run -d -p 50000:3306 --name b2b_db b2b_image

## How to run app

What you need to install/have on your computer:
- Java 8
- Maven

In `/src/main/resources/application.properties` it is possible to change database source and local port.
For local usage use properties (the same as in Dockerfile):
````
  spring.datasource.url=jdbc:mysql://localhost:50000/b2b
  spring.datasource.username=admin
  spring.datasource.password=pass
````
You can run the application from the command line with Maven. 
Go to the root folder of the application and type:
````
./mvnw spring-boot:run
````
Or just use your IDE(f.e. IntelliJIDEA)

The application should be up and running within a few seconds.

Api url: `http://localhost:8080/api/`

### For TEST purpose H2-database is set

Database is filled with some example data when 'test' start.

