# Auth Microservice

The Auth Microservice has features like:-

1. Creating or registering user through register api and set credentials.
2. Verify user and authenticate user through login route, and return jwt access token with RSA encryption
algorithm.
3. Added a protected route to add new roles (can only be accessed by Admin).
4. The system has gateway authorization, meaning if gateway credentials are not found, then the service
will not respond. The api gateway acts as an entry point for all microservices.

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
JWT_PRIVATE_KEY = generate RSA public-private key pair for authentication of jwt token signature.

For generating the RSA public private key follow the steps:

1) To create a private key run the following command:
`bash
openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:2048
`
2) Convert the private_key.pem to base64 string for storing in .env file:
`bash
base64 -w 0 private_key.pem > private.b64
`
3) Copy the private key and set environment variable as mentioned above:
`bash
cat private.b64
`
4) Generate public key using following command:
`bash
openssl rsa -pubout -in private_key.pem -out public_key.pem
`
5) Convert the public key to base64 string and copy it(you will need it later in gateway microservice):
`bash
cat public_key.pem | base64 -w 0
`