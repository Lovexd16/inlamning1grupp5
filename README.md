# E-Sales Platform Server

Welcome to the server side of our e-sales platform project! This project is part of a school project where we design, develop, and present a Minimum Viable Product (MVP) for an e-sales platform. Our platform is specifically tailored for podcasts to present and sell their content, along with merchandise sales.

## Project Description

Our e-sales platform allows podcast creators to showcase their episodes and merchandise for sale to their audience. The platform is built using the Quarkus/Jakarta EE framework and integrates with Stripe for payment processing.

## Team Members

- David Hankin
- Victor Juhlin
- Love Rumar Karlquist

## Getting Started

To clone and start the server yourself, follow these steps:

1. Clone the repository to your local machine:
```shell script
git@github.com:Lovexd16/inlamning1grupp5.git
```

2. Navigate to the project directory:
```shell script
cd e-sales-platform-server
```

3. Insert your own private and publishable keys in the `StripeModel` and `StripeResource` files, respectively. These files are located in the following directories:
- `src/main/java/org/inlamning1grupp5/model/StripeModel.java`
- `src/main/java/org/inlamning1grupp5/model/StripeResource.java`

4. Build the project:
```shell script
./mvnw clean package
```

5. Run the server:
```shell script
java -jar target/e-sales-platform-server.jar
```


6. The server should now be running locally on port 8080.

## Usage

Once the server is running, you can interact with it using HTTP requests to create, retrieve, update, and delete podcast episodes, as well as manage merchandise sales.

## Client

The client side of our e-sales platform project is available at [e-sales-platform-client](https://github.com/Lovexd16/inlamning1grupp5-FRONTEND). Be sure to check it out for the full experience!

## Contributing

Thank you for considering contributing to our project! If you would like to contribute, please submit a pull request with your proposed changes.

## Issues

If you encounter any issues or have any questions, please feel free to open an issue on GitHub and we will do our best to assist you.

Sincerely,
The Team.

(Javautveckling, Jönköping University, Sweden)



*Below is content generated automatically by the Quarkus framework*

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/inlamning1grupp5-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- Hibernate ORM ([guide](https://quarkus.io/guides/hibernate-orm)): Define your persistent model with Hibernate ORM and Jakarta Persistence
- Narayana JTA - Transaction manager ([guide](https://quarkus.io/guides/transaction)): Offer JTA transaction support (included in Hibernate ORM)
- Hibernate Validator ([guide](https://quarkus.io/guides/validation)): Validate object properties (field, getter) and method parameters for your beans (REST, CDI, Jakarta Persistence)
- RESTEasy Classic JSON-B ([guide](https://quarkus.io/guides/rest-json)): JSON-B serialization support for RESTEasy Classic
- SmallRye OpenAPI ([guide](https://quarkus.io/guides/openapi-swaggerui)): Document your REST APIs with OpenAPI - comes with Swagger UI
- RESTEasy Classic ([guide](https://quarkus.io/guides/resteasy)): REST endpoint framework implementing Jakarta REST and more
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC

## Provided Code

### Hibernate ORM

Create your first JPA entity

[Related guide section...](https://quarkus.io/guides/hibernate-orm)



### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
