# Eureka Discovery Server

The Eureka discovery server acts as a service registry for other microservices, and provides
DNS resolution for microservices, so that you don't need to hardcode the urls or port number
for each microservice.

## Steps

To configure this microservice, follow the following steps:

1) Clone this repo.
2) Open the microservice in some Java compatible IDE like IntelliJ IDEA.
3) Install the dependencies using Maven build tool from pom.xml.
4) Edit the Configuration of the project, by setting the configurations under "Spring Boot" application.
5) Set the environment variables for the project as given below, and setup the environment variables in "Configuration" of the IntelliJ.
6) Run the project.

For environment variables use this template, create an .env file at the root of the project, and load it inside the "Configuration" of the IntelliJ:
EUREKA_SERVER_URL = url of running eureka discovery server, for service registry.
EUREKA_PORT = port number for running the app(must be unique from other microservice)