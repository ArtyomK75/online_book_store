# Book store api
## Description
Book store it's a service which allows register customers and bye books. This api supports
authentication, registration, CRUD (Create, Read, Update,Delete) operations with entity of database.

## Features:
- registration user with role "USER";
- authentication user;
- create/get list of books;
- create/get categories of books;
- add books to a shopping cart;
- edit a shopping cart;
- checkout an order for an authenticated user;
- complete an order and get list of orders for an authenticated user;
- under role ADMIN can edit statuses of an order

## Technologies Used
- Spring boot framework 3.1.3
- jackson web token 0.11.5
- Java 17
- Maven 3.1.1
- Mapstruct 0.2.0
- Test containers 1.18.0 
- Lombok 0.2.0
- MySQL 8.0.28
- Docker 3.1.3

# Endpoints available for used api  
## Available for non authenticated users:
- POST: /api/auth/register
- POST: /api/auth/login
## Available for users with role USER
- GET: /api/books
- GET: /api/books/{id}
- GET: /api/categories
- GET: /api/categories/{id}
- GET: /api/categories/{id}/books
- GET: /api/cart
- POST: /api/cart
- PUT: /api/cart/cart-items/{cartItemId}
- DELETE: /api/cart/cart-items/{cartItemId}
- GET: /api/orders
- POST: /api/orders
- GET: /api/orders/{orderId}/items
- GET: /api/orders/{orderId}/items/{itemId}
## Available for users with role ADMIN
- POST: /api/books/
- PUT: /api/books/{id}
- DELETE: /api/books/{id}
- POST: /api/categories
- PUT: /api/categories/{id}
- DELETE: /api/categories/{id}
- PATCH: /api/orders/{id}

# Note on use the api
Please note that when using the api endpoints with methods POST, PUT, PATCH required JSON body. Security builds on
technology jackson web token (JWT) with using Bearer token. 

