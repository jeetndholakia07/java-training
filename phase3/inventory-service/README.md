# Inventory Microservice

The Inventory Microservice has features like:-

1. Admin can add inventories of available products.
2. Admin can view inventories of available products.
3. Admin can update/soft delete inventories of available products.
4. Customer can access checkout route to check availability of inventory product, for checkout of order, and also update the inventory available units count.
5. The system has role based authorization, and can't be accessed without gateway credentials.

## Steps

To configure this microservice, follow the following steps:

1) Clone this repo.
2) Open the microservice in some Java compatible IDE like IntelliJ IDEA.
3) Install the dependencies using Maven build tool from pom.xml.
4) Edit the Configuration of the project, by setting the configurations under "Spring Boot" application.
5) Set the environment variables for the project as given below, and setup the environment variables in "Configuration" of the IntelliJ.
6) Run the project.

For environment variables use this template, create an .env file at the root of the project, and load it inside the "Configuration" of the IntelliJ:
DB_URL= url of jdbc mysql driver
DB_USERNAME = your username in mysql
DB_PASSWORD = your password in mysql
EUREKA_SERVER_URL = url of running eureka discovery server, for service registry.
PORT = port number for running the app(must be unique from other microservice)
GATEWAY_SECRET = get the gateway secret string from api gateway microservice.