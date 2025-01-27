# Online Shopping Platform

An e-commerce web application built with a Spring Boot backend and a Next.js frontend. This platform supports cart management, product browsing, and order placement functionalities.

## 🚀 Features

### For Customers:
- **Product Browsing**: View available products and their details.
- **Cart Management**:
    - Add products to the cart.
    - Update product quantities.
    - Remove products from the cart.
- **Checkout**: Place orders and view past orders.

### For Admins:
- **Role-Based Access**: Admins can access protected endpoints.
- **Product Management**:
    - Add new products.
    - Update product details.
    - Delete products.

## 📂 Project Structure

### Backend (Spring Boot)

```bash
    src/
    ├── main/
    │   ├── java/com/example/demo/
    │   │   ├── config/          # Spring configuration files
    │   │   ├── controller/      # REST controllers
    │   │   ├── dto/             # Data Transfer Objects
    │   │   ├── entity/          # Entity classes for database models
    │   │   ├── exception/       # Global exception handling
    │   │   ├── repository/      # JPA repositories
    │   │   ├── service/         # Business logic and services
    │   │   ├── OnlineShoppingApplication.java  # Main application class
    │   ├── resources/
    │   │   ├── application.properties  # Database configurations
    │   │   ├── data.sql                 # Initial data (if applicable)
    │   │   ├── schema.sql               # Database schema (if applicable)
    └── test/                            # Unit and integration tests
```

### Frontend (Next.js)

```bash
    src/
    ├── app/
    │   ├── components/      # Reusable components (e.g., Header, Footer, ProductCard)
    │   ├── context/         # Global state management (e.g., CartContext)
    │   ├── pages/           # Page components (e.g., Home, Cart, Orders)
    │   ├── services/        # API integrations (e.g., axios instance)
    │   ├── utils/           # Utility functions (e.g., formatCurrency)
    │   ├── globals.css      # Global styles
    │   ├── layout.js        # Application layout
    │   ├── page.js          # Entry point for the app
```


## 🛠 Installation and Setup

### Prerequisites:
- Java 17+ (for the backend).
- Node.js 16+ (for the frontend).
- MySQL (or another relational database).

### Backend Setup:

1. Clone the repository and navigate to the backend folder:

    ```bash
    git clone https://github.com/INFINITYASH3699/Online-Shopping-Java-Project.git
    cd online_shopping_java_project/online_shopping
    ```

2. Configure the `application.properties` file:

    Add your database credentials:
    ```bash
    spring.datasource.url=jdbc:mysql://localhost:3306/online_shopping
    spring.datasource.username=<db_user>
    spring.datasource.password=<db_password>
    ```

3. Run the application:

    ```bash
    mvn spring-boot:run
    ```

## Frontend Setup:

1. Navigate to the frontend folder:

    ```bash
    cd online_shopping_java_project/frontend
    ```

2. Navigate to the frontend folder:

    ```bash
    npm install
    ```

3. Start the development server:

    ```bash
    npm run dev
    ```

4. Open your browser and navigate to:

    ```bash
    http://localhost:3000
    ```



## 🔢 API Endpoints

### Products:
- **GET /api/products** - Get all products.
- **POST /api/admin/products** - Add a product (Admin only).
- **PUT /api/admin/products/{id}** - Update a product (Admin only).
- **DELETE /api/admin/products/{id}** - Delete a product (Admin only).

### Cart:
- **GET /api/carts/{customerId}** - Get cart for a customer.
- **POST /api/carts/{customerId}/add** - Add a product to the cart.
- **POST /api/carts/{customerId}/update** - Update product quantity.
- **DELETE /api/carts/{customerId}/remove** - Remove a product from the cart.

### Orders:
- **GET /api/orders** - Get all orders (Admin only).
- **GET /api/orders/{orderId}** - Get order by ID (Admin only).
- **POST /api/carts/{customerId}/checkout** - Checkout cart and create an order.


## 🐞 Troubleshooting

### Common Issues:

- **Database Connection Failed**: Ensure MySQL is running and the credentials in `application.properties` are correct.
- **Cart Not Loading**: Verify that the customer ID is being passed correctly.


## 🚦 Next Steps

### Future Enhancements:
- **Search and Filter**: Allow users to search and filter products.
- **Payment Integration**: Add payment gateway for checkout.
- **Email Notifications**: Notify customers about order confirmations.
- **Unit Tests**: Increase test coverage for both backend and frontend.


## 📝 License

This project is licensed under the MIT License. See the LICENSE file for details.

## 🙋‍♂ Author

- **Name**: Yash Hulle
- **Email**: yash.hulle3699@gmail.com
- **GitHub**: [https://github.com/INFINITYASH3699](https://github.com/INFINITYASH3699)
