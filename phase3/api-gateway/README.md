# API Gateway Microservice

The API Gateway Microservice acts as a gateway for all microservices like auth service, product service,
inventory service and order service, and provides better scalability for microservices, and avoids
exposing microservices individually.
It has functions such as getting the JWT access token if any, and set User and Gateway headers to every 
request, and the headers are forwarded to the services with gateway mapping.
It also validates public and protected routes in the gateway itself.

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
GATEWAY_SERVER_PORT = port number for running the app(must be unique from other microservice)
GATEWAY_SECRET = get the gateway secret string from api gateway microservice.
JWT_PUBLIC_KEY = get the public key from RSA key pair generated in auth service, for jwt token verification. 
Follow the steps in auth service for more details.

For generating the gateway secret follow the steps:

1) To create a random gateway secret key run the following command:
`bash
openssl rand -hex 32
`
2) Copy the randomly generated hex string and set it as environment variables in all microservices
for gateway authorization.