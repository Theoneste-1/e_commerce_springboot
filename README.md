# E-Commerce Backend API Documentation
## Comprehensive Guide to Spring Boot E-commerce Application
### Project Overview


A robust, scalable, and secure e-commerce backend application built with Spring Boot, Spring Security, JPA/Hibernate, and PostgreSQL. This application provides a complete set of RESTful APIs for managing an online store.

Technology Stack

## 🚀 Features

- **User Management**: Registration, authentication, and authorization
- **Product Catalog**: CRUD operations for products, categories, and brands
- **Inventory Management**: Stock tracking and updates
- **Shopping Cart**: Add, update, and remove items from cart
- **Order Processing**: Complete order lifecycle management
- **Payment Integration**: Payment processing and status tracking
- **Shipping Management**: Shipment tracking and delivery updates
- **Review System**: Product ratings and reviews
- **Wishlist**: Save products for later
- **Address Management**: Multiple shipping addresses

## 🛠️ Technology Stack

- **Backend Framework**: Spring Boot 3.2.0
- **Security**: Spring Security 6 with JWT authentication
- **Database**: PostgreSQL 15 with Hibernate ORM
- **Validation**: Jakarta Bean Validation 3.0
- **API Documentation**: OpenAPI 3.0 (Swagger)
- **Build Tool**: Maven
- **Java Version**: 17+
- **Testing**: JUnit 5, Mockito, Testcontainers

## 📦 Project Structure
Step-by-Step Installation
Clone the Repository

```
git clone <repository-url>
cd ecommerce-backend

```

### Database Setup
````
CREATE DATABASE ecommerce_db;
CREATE USER ecommerce_user WITH PASSWORD 'securepassword';
GRANT ALL PRIVILEGES ON DATABASE ecommerce_db TO ecommerce_user;

````
### Configuration
Update application.properties:

#### properties
````
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=ecommerce_user
spring.datasource.password=securepassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
````
### Build the Application

````
mvn clean install
Run the Application
````

````
mvn spring-boot:run
````


## Controllers Overview

| Controller          | Purpose                              | Key Endpoints                     |
|---------------------|--------------------------------------|-----------------------------------|
| `AuthController`    | Handles authentication              | `/api/auth/login`, `/api/auth/register` |
| `ProductController` | Manages product data                | `/api/products`, `/api/products/{id}` |
| `BrandController`   | Manages brand data                  | `/api/brands`, `/api/brands/{id}` |
| `CategoryController` | Manages category data               | `/api/categories`, `/api/categories/{id}` |
| `OrderController`   | Processes orders                    | `/api/orders`, `/api/orders/{id}` |
| `CartController`    | Manages shopping cart               | `/api/cart`, `/api/cart/items` |
| `PaymentController` | Handles payment processing          | `/api/payments`, `/api/payments/{id}` |
| `UserController`    | Manages user data                   | `/api/users`, `/api/users/{id}` |
| `WhishlistController` | Managers Users wishlist             | `/api/wishlists`, `/api/wishlists/{id}` |  


## Security Implementation
## JWT Authentication Flow
### User logs in with credentials

 - **Server validates credentials and generates JWT**

- **Client includes JWT in Authorization header for subsequent requests**

- **Server validates JWT and grants access to protected resources**

- **Security Configuration**

## Security Configuration

```
@Configuration
public class SecurityConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder.encoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
//                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/roles/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll() // For development

                        // Product endpoints
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/products/**").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

                        // Category endpoints
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")

                        // Order endpoints
                        .requestMatchers("/api/orders/**").hasAnyRole("USER", "ADMIN", "SELLER")

                        // Cart endpoints
                        .requestMatchers("/api/cart/**").hasRole("USER")

                        // User management
                        .requestMatchers("/api/users/profile").hasAnyRole("USER", "ADMIN", "SELLER")
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // Admin endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Seller endpoints
                        .requestMatchers("/api/seller/**").hasAnyRole("ADMIN", "SELLER")

                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
```


## Database Schema
### Key Entities
- **User: User accounts and authentication details**

- **Product: Product information and details**

- **Category: Product categorization**

- **Brand: Product manufacturers**

- **Order: Customer orders**

- **OrderItem: Individual items within orders**

- **Payment: Payment transactions**

- **Cart: Shopping cart information**

- **Review: Product ratings and reviews**


## API Testing with curl Examples
### User Registration:

```
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "securePassword123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

### User Login:

```
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "securePassword123"
  }'
```

## Troubleshooting
### Common Issues
- **Database Connection Issues**

Verify PostgreSQL is running

Check database credentials in application.properties

- **JWT Authentication Problems**

Ensure proper token format in Authorization header

Check token expiration

- **CORS Errors**

Configure CORS properly for frontend applications

- **Getting Help**
Check application logs for detailed error messages

Verify all dependencies are correctly configured

Ensure database migrations have run successfully
## Support
For support, please email  theodufi.rw@gmail.com or create an issue in the project repository.

This document was generated on: December 5, 2023