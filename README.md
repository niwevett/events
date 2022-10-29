# Events application

This is a simple JAVA voting application build with Spring Boot


### Prerequisites

* PostgreSQL 15
* Suitable compiler // I used IntelliJ IDEA Community Edition 2022.2.3
* Java 19

### Installing

1. Create a new database for this project in CMD or PgAdmin4 and add the credentials and path to application properties.
2. Install Postman and import Postman collection

## Build with

* Spring Boot
* Maven

Testing was done by using Postman


## Features

* Post new events with options
* Get all events list
* Get events by id
* Vote event
* Get votes for an event
* Get result for an event which suit all voters

## Methods


| **Method** | **Path**                   | **Description**                         |
|------------|----------------------------|-----------------------------------------|
| get        | api/v1/events/list         | get all events listed                   |
| get        | api/v1/events/{id}         | get a spescific event                   |
| get        | api/v1/events/{id}/votes   | gets all options and votes for an event |
| get        | api/v1/events/{id}/results | gets the results of the vote            |
| post       | api/v1/events/             | post new event                          |
| post       | api/v1/events/{id}/votes   | post a vote for an event                |


## Authors

* **Niklas Vettanen** - *Initial work* - [niwevett](https://github.com/niwevett)


