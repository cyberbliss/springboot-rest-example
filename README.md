# springboot-rest-example
A complete Spring Boot example application for REST APIs; its purpose is to demonstrate various API documentation tools.

## Installation
* Ensure that Java 8 and Maven 3.2 are installed
* Some of the documentation tools also required Node to be installed
* Clone this repo:
    `git clone https://github.com/cyberbliss/springboot-rest-example.git`

## Usage
### Running the Spring Boot app
Navigate to the directory into which you cloned the repo and execute this:
`mvn spring-boot:run`

Once started you can access the APIs on port 9080, e.g.
`http://localhost:9080/api/books`

The port number can be changed by editing the port property in `src/main/resources/application.yml`

### Convert Swagger spec into HTML doc
The swagger2markup and asciidoc maven plugins are used to publish the API documentation as an HTML doc. To use:

`mvn clean test site`

This will create the HTML doc in `target/generated-docs/html`