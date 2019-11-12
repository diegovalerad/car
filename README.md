# REST service for a car application

Web application that has a REST service as an endpoint. The application makes use of Java EE. 

The application lets the client make CRUD operations (create, read, updated & delete) about cars. 

The system makes use of a synchronous service, where every change takes effect on the database instantly. Besides, the application allows asynchronous operations (create, update & delete), where the changes may not take effect instantly.

## JAVA EE

* The business logic is made using EJB. 
* The entities are JPA objects, stored in a SQL database. 
* The entities are validated through bean validations.
* The API REST is made using JAX-RS.

## API REST

* GET / (get all cars)
* GET /:id (get car by id)
* POST / (create car)
* UPDATE /:id (modify car by id)
* DELETE /:id (delete car by id)

Every method return a Response object with the appropiate response codes.

## TESTING

### Unit test
Every part of the application has been tested using JUnit, Mockito and PowerMock.

### Integration test
Integration tests has been made using Postman software. The set of tests used are in the *integrationTest* folder.

## DOCUMENTATION

The API REST has been documented through annotations, using Swagger.

Most of the classes and methods are commented using Javadoc.

## OTHERS

* Log4j as logger.
* Timer to delete soft-removed cars using a CRON expression.
* Maven as package manager.
* Payara as server.

## Authors

* Diego Valera - *Development* - [GitHub profile](https://github.com/Di3GO95/)
* Francisco Javier - *Overseer* - [GitHub profile](https://github.com/javieraviles)
