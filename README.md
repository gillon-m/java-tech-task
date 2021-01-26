# Lunch Microservice

The service provides an endpoint that will determine, from a set of recipes, what I can have for lunch at a given date, based on my fridge ingredient's expiry date, so that I can quickly decide what I’ll be having to eat, and the ingredients required to prepare the meal.

## Prerequisites

* [Java 11 Runtime](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
* [Docker](https://docs.docker.com/get-docker/) & [Docker-Compose](https://docs.docker.com/compose/install/)

*Note: Docker is used for the local MySQL database instance, feel free to use your own instance or any other SQL database and insert data from lunch-data.sql script*


### Run

1. Start database:

    ```
    docker-compose up -d
    ```
   
2. Add test data from  `sql/lunch-data.sql` to the database. Here's a helper script if you prefer:


    ```
    CONTAINER_ID=$(docker inspect --format="{{.Id}}" lunch-db)
    ```
    
    ```
    docker cp sql/lunch-data.sql $CONTAINER_ID:/lunch-data.sql
    ```
    
    ```
    docker exec $CONTAINER_ID /bin/sh -c 'mysql -u root -prezdytechtask lunch </lunch-data.sql'
    ```
    
3. Run Springboot LunchApplication

## Changes

### Assumptions
* Spring security not needed
* DTOs are not required to be separate to persistence entities
* If a useBy date or bestBefore date is set as "null", it is assumed that the ingredient does not have an expiration date (ie. LocalDate.MAX)
* Ingredients are still considered "good" if useBy/bestBefore date is the currently specified date

1. Moved db access logic to RecipeRepository
2. Added LunchService interface which is implemented by LunchServiceImpl
3. Added exception handler for NotFound errors
4. Added unit tests
5. Extended /lunch/recipes exclusion of ingredients functionality to also has an inclusion option where recipes that contain specified ingredients will be returned