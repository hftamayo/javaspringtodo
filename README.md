# ToDo App #

This is a fully functional ToDo Application built on Kotlin for the Frontend and it is also use different RestAPI developed in different programming languages. 

## Concept ##
- MultiUsers support
- Test different programming languages for the BackEnd
- Compare the benefits of using RestAPI and GrapQL
- Apply best practices: refactoring, clean Code, design patterns
- Write unit, functional and integration tests
- Evaluate the potential security holes of the application

## Status ##
- oldstable: Task Crud(01-03-2024)
- experimental: User Login 

## Version 1 specs ##

### Technical Stack ###
* Data storage: sqLite if the app is offline / noSQL
* FrontEnd: Kotlin
* BackEnd: RestAPI and GraphQL developed using different programming languages and frameworks

### Available Functions ###
* Sign-Up / Login
* View available tasks filter by users
* ToDo CRUD

## Proof of Concept ##
[Todo]

## Branches ##
* OldStable : deprecated versions of the project
* Stable: current or latest official version
* Unstable: sourcecode that has not been tested and approved
* Experimental: sourcecode in progress
* Hibernate: use of DAO classes for the database connection

## Bugs ##
Please refer to the Issues section in this repository

## Log releases ##
### 0.0.3 Version ###
- Released on: Jun 10th 2024
- Based on Spring Data JPA
- Unit and Integration testing written in JUnit
- Commit ID: (): 

### 0.0.2 Version ###
- Released on: Jun 10th 2024
- Based on Hibernate
- Unit and Integration testing written in JUnit
- Commit ID: (https://github.com/hftamayo/nodetodo/commit/70e6186b89ff4912a3c17745403db35685aa824a): typescript -> experimental -> main

### 0.0.1 Version ###
- Released on: Jun 11st 2024
- Based on JPA
- this version won't be tested
- Commit ID: (https://github.com/hftamayo/javaspringtodo/pull/38)

### 0.1.2 Version ###
- Released on:
- Unit and Integration testing written in Junit

## References ##
- https://www.bezkoder.com/docker-compose-spring-boot-mysql/
- https://stackoverflow.com/questions/76513868/mysql-docker-spring-boot-connection-issue-communications-link-failure
- https://stackoverflow.com/questions/42567475/docker-compose-check-if-mysql-connection-is-ready
- health check routine: https://www.appsloveworld.com/docker/100/78/docker-compose-healthcheck-mysql
- official mysql docker documentation: https://hub.docker.com/_/mysql/
- [JWT Implementation](https://www.javaguides.net/2023/05/spring-boot-spring-security-jwt-mysql.html)
- [Data seeding](https://studygyaan.com/spring-boot/provide-initial-default-data-for-models-in-spring-boot) 
- [Data seeding 2](https://www.javadevjournal.com/spring-boot/loading-initial-data-with-spring-boot/)
- [JWT Implementation v2](https://github.com/bezkoder/spring-boot-login-example/blob/master/src/main/java/com/bezkoder/spring/login/security/WebSecurityConfig.java)
