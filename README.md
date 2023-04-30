# Microservices-example
Example of a microservices using Spring Cloud

# About the project
<ul style="list-style-type:disc">
  <li>User can create, customer, create order and get all orders by customer  through <strong>API Gateway</strong>. <strong>MySQL</strong> is used for storage customers and orders. The service uses Asynchronous communication between notification service, using <strong>RabbitMQ</strong>. And <strong>WebClient</strong> is used as Synchronous communication betwen inventoery service</li>
  <li>User can create product, get list of all products and check for product availability through <strong>API Gateway</strong>. <strong>MongoDB</strong> is used for storage all prodcuts.</li>
  <li>You can't create order if there isn't your product in stock.</li>
  <li>Notification service is used for getting email after create a new order.</li>
  <li>User can login through <strong>Keycloak</strong></li>
  <li>All projects configurations: https://github.com/MaximRom00/Microservices-example/tree/master/config-repo</strong></li>
</ul>

6 services whose name are shown below have been devised in this project:

- <strong>API Gateway</strong> - API Gateway which is responsible to route the request to specific microservice. The service does user authentication via OAuth 2.0. You can access the API Gateway at http://localhost:8180.
- <strong>Config Server</strong> - Centralized configuration service.
- <strong>Discovery Server</strong> - Service registry that allows other services to find and communicate with each other. You can access this service at http://localhost:8761.
- <strong>Customer Service</strong> - Responsible for managing orders and customers products.
- <strong>Invtentory Service</strong> - Responsible for managing catalog products.
- <strong>Notification Service</strong> - Responsible for getting and sending email notification.


# Used Dependencies
* Core
  * Spring
    * Spring Boot
    * Spring Security
      * OAuth 2.0 Resource Server
    * Spring Web
    * Spring Data
      * Spring Data JPA
    * Spring WebFlux
    * Spring Cloud
      * Spring Cloud Gateway
      * Spring Cloud Bootstrap
      * Spring Cloud Config Server
      * Spring Cloud Config Client
     * Springdoc-openapi
  * Netflix
    * Eureka Server
    * Eureka Client
* Database
  * Mysql
  * MongoDB 
* Message Broker
  * RabbitMQ
* Circuit Breaker
  * Resilience4j
* Lombok
* Security
  * Keycloak
