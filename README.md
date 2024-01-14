# Webshop Application (Malmö)
## Description
This project is a webshop application developed using Spring Boot and Vaadin. The application allows users to log in as either an admin or a regular user to perform various actions, including adding items, making purchases, and reviewing purchase history.

## Functionality
Admin Functions
* Login as an admin and manage items in the webshop.
* View a complete history of performed actions.

User Functions
* Login as a user and add items to the shopping cart.
* Complete purchases and review purchase history.

## System Requirements
• Ensure that you have Java 17 installed on your computer.
• Verify that Maven is installed on your computer.
• MySQL database needs to be available on the computer.

## Installation
• Clone the project from the GitHub repository.[GitHub repository for backend] (https://github.com/94hansnu/WebserviceMotEnWebshop) and [GitHub repository for frontend] (https://github.com/kellyvasss/frontend).
• Configure your MySQL database and update the connection information in the 'application.properties' file.
• Run 'mvn clean install' to build the project.
• Start the Spring Boot server by going to the 'DemoApplication' class and selecting 'run'.
• Start the client application by going to the 'FrontendApplication' class and selecting 'run'.

## Usage
• Open a web browser and navigate to 'http://localhost:8081'.
• Log in as an admin or user using the existing credentials.
• Perform the desired functions that are available.

## Technology & Framework
• Spring Boot: Used to build a powerful and flexible backend.
• Vaadin: Java-based framework for creating user interfaces (UI) for web applications.
• Spring Security: Used for authentication and authorization to ensure application security.
• MySQL: Database for storing and managing application data.
• Spring Data JPA: Facilitates interaction with databases through JPA-based repositories.
