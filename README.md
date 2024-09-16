# Photo Contest

## Project Overview
A web application for managing photo contests, including user roles, rankings, contests, photos, jury ratings, and contest participation.

## Technologies Used
- **Languages**: SQL, Java, JavaScript
- **Frameworks**: Spring Boot
- **Build Tool**: Gradle
- **IDE**: IntelliJ IDEA 2024.1.6

## Database Schema
The database schema includes the following tables:
- `roles`: Stores user roles.
- `rankings`: Stores user rankings.
- `users`: Stores user information.
- `phases`: Stores contest phases.
- `contests`: Stores contest details.
- `photos`: Stores photo details.
- `jury_photo_ratings`: Stores jury ratings for photos.
- `contests_participation`: Stores user participation in contests.

## SQL Scripts
- **`create_data.sql`**: Script to create the database and tables.
- **`insert_data.sql`**: Script to insert initial data into the tables.

## Getting Started
1. **Clone the repository**:
    ```sh
    git clone <repository-url>
    cd <repository-directory>
    ```

2. **Set up the database**:
    - Execute the `create_data.sql` script to create the database and tables.
    - Execute the `insert_data.sql` script to insert initial data.

3. **Build the project**:
    ```sh
    ./gradlew build
    ```

4. **Run the application**:
    ```sh
    ./gradlew bootRun
    ```

## Project Structure
- **`src/main/java`**: Contains Java source files.
- **`src/main/resources`**: Contains application configuration files.
- **`db`**: Contains SQL scripts for database setup.

## Contributing
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Commit your changes (`git commit -m 'Add some feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Open a pull request.

## License
This project is licensed under the MIT License.

## Contact
For any inquiries, please contact the project maintainer.