# Gilded Rose 

A hotel reservation application exercise.

# Running Locally

Use MariaDB or MySQL and create a database `hotel`. Grant all privileges to `maria` identified by `maria`, or change `application.properties` accordingly.

```
CREATE DATABASE hotel;
CREATE USER 'maria'@'localhost' IDENTIFIED BY 'maria';
GRANT ALL PRIVILEGES ON hotel.* TO 'maria'@'localhost';
FLUSH PRIVILEGES;

```


`mvn clean package -DskipTests`

Run the application: `HotelApiApplication.java` in your IDE or as a .jar

```
java -jar application-0.0.1-SNAPSHOT.jar
```

[http://localhost:8080]()


#### Item


POST (`Bearer 123` = Authenticated)

```
curl -v -X POST -H "Authorization: Bearer 123" -H "Content-Type: application/json" -d '{"name": "room item 1", "amount": 99.95, "description": "room item 1 desc"}' "http://localhost:8080/item"
```

```
{
    "amount": 88.95,
    "created": "2019-06-02T01:35:42.757+0000",
    "description": "room item 2 desc",
    "id": 2,
    "name": "room item 2"
}

```

POST (Bearer 999 = Unauthenticated) 

```
{
    "error": "Forbidden",
    "message": "Forbidden",
    "path": "/item",
    "status": 403,
    "timestamp": "2019-06-02T04:42:06.878+0000",
    "trace": "..."
}
```


POST (negative `Amount`) 
```
curl -v -X POST -H "Authorization: Bearer 123" -H "Content-Type: application/json" -d '{"name": "name", "amount": -10.00, "description": "room item 2 desc"}' "http://localhost:8080/item"
```

```
< HTTP/1.1 400
 
{
    "message": "Amount must be positive."
}

```

#### Inventory
GET

```
curl -v -X GET -H "Content-Type: application/json" "http://localhost:8080/inventory"

```


```
[
    {
        "amount": 99.95,
        "created": "2019-06-02T01:13:33.000+0000",
        "description": "room item 1 desc",
        "id": 1,
        "name": "room item 1"
    },
    {
        "amount": 88.95,
        "created": "2019-06-02T01:14:06.000+0000",
        "description": "room item 2 desc",
        "id": 2,
        "name": "room item 2"
    }
]

```

#### Testing
- Integration tests use an in-memory database `H2` see module `db`
- Functional tests use Mockito see module `core`
- Testing the Controllers in `application` could be done with JUnit and a REST client. But
I did not do that as black box automated tests should normally cover this scenario.


#### Architecture
Uses a 3 layer architecture. 
- At the bottom is the 'db' for an integration to the data store.
- `oauth` module is to communicate with external OAuth API (mocked)
- Middle tier `core` for the business logic exposed via Services
- Top layer `application` is the interface layer
- 'client-model', allows for Java clients to use the API easily by importing the module from a repository.
- Exception handler see `DefaultErrorController.java`

Note: The core layer could use it's own data objects, but here I just used
the DTOs.

#### Surge Pricing
Implemented in the core layer since this is business logic. `ItemService.java`, although yes, I should move this logic to it's
own class `SurgePricingService.java`

#### Data Format
JSON - Standard messaging format for clients. See `curl` examples above for output sample.

#### Authentication
- SpringSecurity: Out of the box allows for `Role`s 
```
    @PreAuthorize("hasAnyRole('ANONYMOUS', 'USER')")

```

- OAuth use another API to authenticate users. Not implemented in this API.

```
"Authorization: Bearer 123"
```

Note: `123` is the special token for authenticating with role `USER` else user
is `ANONYMOUS`

