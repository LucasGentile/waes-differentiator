# WAES Differentiator

WAES Differentiator is a RESTful api responsible for creating a diff structure with "LEFT" and "RIGHT" content sides, which are JSON base64 encoded binary data, differentiating them by size and also diff offsets and its lengths.

## Main Technologies:

* Java
* Spring Boot
* Gradle
* Swagger
* H2 Database
* [Lombok](https://projectlombok.org/)
* IntelliJ

## Run

* Import WAES Differentiator as a Gradle project;
* Execute Gradle task called application:bootRun in order to start the application;
* Access the application with following URL: http://localhost:8080/api/swagger-ui.html
* The following endpoints will be provided by the application:
    * POST /v1/diff/{id}
    * POST /v1/diff/{id}/left
    * POST /v1/diff/{id}/right
    * GET /v1/diff/diffPair/{id}

Obs: In order to use Lombok, you need to enable annotation processing in your IDE.

## Improvments

* Create an UI that consumes the exposed endoints;
* Improve exceptions treatment;
* Add more test scenarios (besides the happy path ones) to the integration tests;
* Improve in-code documentation