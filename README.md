# Gilded Rose 

A hotel

# Running Locally

Use MariaDB or MySQL and create a database `credit`. Grant all privileges to `maria` identified by `maria`, or change `application.properties` accordingly.

```
CREATE DATABASE hotel;
CREATE USER 'maria'@'localhost' IDENTIFIED BY 'maria';
GRANT ALL PRIVILEGES ON hotel.* TO 'maria'@'localhost';
FLUSH PRIVILEGES;

```


`mvn clean package -DskipTests`

Run the application: `HotelApiApplication.java`

[http://localhost:8080]()

### API Documentation (Swagger)

[http://localhost:8080/swagger-ui.html]()


#### Item


POST

```
curl -v -X POST -H "Authorization: Bearer 2eb41ea6ee31a6a45ec5a7495f4bc315f695e1a0" -H "Content-Type: application/json" -d '{"name": "room item 1", "amount": 99.95, "description": "room item 1 desc"}' "http://localhost:8080/item"
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


# 4

#### Architecture
Uses a 3 layer architecture. 
- At the bottom is the 'db' for an integration to the data store.
- Middle tier is the 'core' for the business logic exposed via Services
- Top is the interface layer
- 'client-model', allows for Java clients to use the API easily by importing the module from a repository.

Note: The core layer could use it's own data objects, but here I just used
the DTOs.

#### Surge Pricing
Implemented in the core layer since this is business logic. 

#### Data Format
JSON - Standard messaging format for clients. See `curl` examples above for output sample.

#### Authentication
- SpringSecurity: Out of the box allows for `Role`s 
```
    @PreAuthorize("hasAnyRole('ANONYMOUS', 'USER')")

```

- OAuth use another API to authenticate users. Not implemented in this API.
