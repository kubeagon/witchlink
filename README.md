# WitchLink - Open Source Link-in-Bio Platform

WitchLink is a self-hosted, open-source alternative to Linktree built with Java Spring Boot. Create your personalized link page and share multiple links through a single URL.

## Features

- 🎨 **Customizable Themes** - Multiple themes to personalize your profile
- 📊 **Analytics** - Track click counts on your links
- 🔒 **Privacy-Focused** - Self-hosted solution with full data control
- ⚡ **Fast & Simple** - Easy-to-use interface with no coding required
- 👤 **User Management** - Multi-user support with authentication
- 📱 **Responsive Design** - Works perfectly on all devices

## Tech Stack

- **Backend**: Java 17 + Spring Boot 3.2.1
- **Database**: H2 (embedded, can be switched to PostgreSQL/MySQL)
- **Security**: Spring Security with BCrypt password encryption
- **Frontend**: Thymeleaf + HTML/CSS/JavaScript
- **Build Tool**: Gradle

## Quick Start

### Prerequisites

- Java 17 or higher
- Gradle 8.x or higher (or use the included Gradle wrapper)

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/witchlink.git
cd witchlink
```

2. Run the application:
```bash
./gradlew bootRun
```

Or on Windows:
```bash
gradlew.bat bootRun
```

3. Open your browser and navigate to:
```
http://localhost:8080
```

### First Time Setup

1. Visit `http://localhost:8080`
2. Click "Get Started Free" to create an account
3. After registration, login with your credentials
4. Start adding links to your profile!

## Usage

### Creating Your Profile

1. **Register an Account**: Choose a username (this will be your profile URL)
2. **Login**: Access your dashboard
3. **Edit Profile**: Customize your display name, bio, and theme
4. **Add Links**: Create links to your social media, websites, etc.

### Accessing Your Profile

Your public profile will be available at:
```
http://localhost:8080/u/yourusername
```

Share this link on your social media profiles!

### Managing Links

- **Add Link**: Click "Add New Link" from your dashboard
- **Edit Link**: Click "Edit" on any link to modify it
- **Toggle Active**: Enable/disable links without deleting them
- **Track Stats**: View click counts for each link
- **Reorder**: Links are displayed in the order you create them

## Development

### Building for Production

```bash
./gradlew clean build
```

The JAR file will be created in `build/libs/witchlink-1.0.0.jar`

### Running the Production Build

```bash
java -jar build/libs/witchlink-1.0.0.jar
```

### Hot Reload

The project includes Spring Boot DevTools for automatic restart when you make changes. Just save your files and the application will restart automatically.

### Running Tests

```bash
./gradlew test
```

### Database

By default, WitchLink uses H2 database with file storage. The database file is created at `./data/witchlink.mv.db`

To switch to PostgreSQL or MySQL, update `application.properties`:

```properties
# PostgreSQL Example
spring.datasource.url=jdbc:postgresql://localhost:5432/witchlink
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### H2 Console

Access the H2 database console at:
```
http://localhost:8080/h2-console
```

Connection details:
- JDBC URL: `jdbc:h2:file:./data/witchlink`
- Username: `sa`
- Password: (leave empty)

## Configuration

Edit `src/main/resources/application.properties` to customize:

- Server port
- Database connection
- Logging levels
- Security settings

## Project Structure

```
witchlink/
├── src/
│   ├── main/
│   │   ├── java/com/witchlink/
│   │   │   ├── controller/      # Web controllers
│   │   │   ├── model/           # JPA entities
│   │   │   ├── repository/      # Data repositories
│   │   │   ├── security/        # Security configuration
│   │   │   ├── service/         # Business logic
│   │   │   └── WitchLinkApplication.java
│   │   └── resources/
│   │       ├── static/          # CSS, JS, images
│   │       ├── templates/       # Thymeleaf templates
│   │       └── application.properties
├── pom.xml
└── README.md
```

## API Endpoints

### Public Endpoints
- `GET /` - Home page
- `GET /u/{username}` - User profile page
- `GET /login` - Login page
- `GET /register` - Registration page
- `POST /register` - Create new account

### Protected Endpoints
- `GET /dashboard` - User dashboard
- `GET /dashboard/profile` - Edit profile
- `POST /dashboard/profile` - Update profile
- `POST /dashboard/links` - Create new link
- `POST /dashboard/links/{id}` - Update link
- `POST /dashboard/links/{id}/delete` - Delete link

### API Endpoints
- `POST /api/links/{id}/click` - Track link click

## Themes

Available themes:
- **Default** - Purple gradient
- **Dark** - Dark mode
- **Ocean** - Blue/cyan gradient
- **Sunset** - Orange/pink gradient
- **Forest** - Green gradient

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is open source and available under the MIT License.

## Support

For issues, questions, or contributions, please open an issue on GitHub.

## Acknowledgments

Inspired by [LinkStack](https://github.com/LinkStackOrg/LinkStack) - the PHP-based Linktree alternative.

---

Made with ❤️ by the open source community
