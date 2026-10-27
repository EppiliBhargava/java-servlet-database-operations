# Java Servlet Database Operations

A Java Servlet web application for performing common database operations against an Oracle XE database. The UI uses HTML and CSS, and servlet endpoints handle database requests through JDBC.

## Features

- **SELECT** — view records from a table.
- **INSERT** — generate input fields from the table's column metadata and insert a record.
- **UPDATE** — locate a record by its primary key and update its non-key columns.
- **DELETE** — delete a record by primary key.
- **CREATE** — create a table by specifying column names and Oracle data types.
- **ALTER** — add, modify, drop, or rename a column.
- Responsive dashboard and operation forms with a consistent dark gradient design.

## Tech stack

- Java Servlets (`javax.servlet` API)
- JDBC
- Oracle Database XE
- HTML5 and CSS3
- Apache Tomcat 9 (recommended for the `javax.servlet` namespace)

## Project structure

```text
java-servlet-database-operations/
├── src/
│   └── main/
│       ├── java/com/myapp/www/
│       │   ├── MyAppSrv1.java   # SELECT endpoint
│       │   ├── MyAppSrv2.java   # INSERT endpoint
│       │   ├── MyAppSrv3.java   # UPDATE endpoint
│       │   ├── MyAppSrv4.java   # ALTER endpoint
│       │   ├── MyAppSrv5.java   # DELETE endpoint
│       │   └── MyAppSrv6.java   # CREATE endpoint
│       └── webapp/
│           ├── index.html
│           ├── select.html
│           ├── insert.html
│           ├── update.html
│           ├── delete.html
│           ├── create.html
│           └── alter.html
├── screenshots/
├── .gitignore
└── README.md
```

## Requirements

1. JDK 8 or newer.
2. Apache Tomcat 9.x.
3. Oracle Database XE running locally or on a reachable host.
4. Oracle JDBC driver (`ojdbc` JAR) available to the application. For a local Tomcat deployment, place the compatible driver JAR in Tomcat's `lib` directory or configure it in your IDE's deployment setup.
5. A database user with the required table privileges.

> **Compatibility note:** These servlets import `javax.servlet.*`; Tomcat 10+ uses `jakarta.servlet.*` and will require code/package updates. Tomcat 9 is the simpler fit for the supplied source.

## Configuration

The source has been prepared to read connection settings from environment variables instead of embedding the supplied password in Java source:

| Variable | Default / example |
|---|---|
| `ORACLE_JDBC_URL` | `jdbc:oracle:thin:@localhost:1521:XE` |
| `ORACLE_DB_USER` | `system` |
| `ORACLE_DB_PASSWORD` | Set this to your local database password |

Set these variables in the environment used to start Tomcat or in your IDE's Tomcat run configuration. Do not commit real credentials to GitHub. Prefer a dedicated application database user with only the privileges this project needs rather than using `SYSTEM`.

## Run locally

1. Start Oracle XE and confirm the listener/service is available.
2. Configure `ORACLE_JDBC_URL`, `ORACLE_DB_USER`, and `ORACLE_DB_PASSWORD`.
3. Add the Oracle JDBC driver to Tomcat's classpath.
4. In your IDE, create/import a web application project and add the files under `src/main/java` and `src/main/webapp`. Ensure the Servlet API is configured as a provided/runtime dependency.
5. Deploy the application to Tomcat 9.
6. Open `http://localhost:8080/<your-context-path>/index.html`.
7. Select an operation and enter a table name that exists in the configured schema (or use **CREATE** first).

The uploaded source files do not include a Maven/Gradle build file or a database schema, so exact build commands and sample data depend on your local IDE/server setup.

## Screenshots

The following screenshots demonstrate the application's user interface and database operation pages.

![Application screenshot 1](screenshots/Screenshot%202026-10-27%20133349.png)

![Application screenshot 2](screenshots/Screenshot%202026-10-27%20133406.png)

![Application screenshot 3](screenshots/Screenshot%202026-10-27%20133455.png)

![Application screenshot 4](screenshots/Screenshot%202026-10-27%20133507.png)

![Application screenshot 5](screenshots/Screenshot%202026-10-27%20133533.png)

![Application screenshot 6](screenshots/Screenshot%202026-10-27%20133549.png)

![Application screenshot 7](screenshots/Screenshot%202026-10-27%20133605.png)

![Application screenshot 8](screenshots/Screenshot%202026-10-27%20133635.png)

| Screenshot | Suggested filename |
|---|---|
| Main dashboard | `screenshots/dashboard.png` |
| Select results | `screenshots/select-results.png` |
| Insert form/result | `screenshots/insert.png` |
| Update form/result | `screenshots/update.png` |
| Create/alter table | `screenshots/schema-operations.png` |
| Delete confirmation | `screenshots/delete.png` |

After adding the files, you can display them in this README with Markdown, for example:

```md
![Database Operations dashboard](screenshots/dashboard.png)
```

Screenshots should use sample data only. Do not include database passwords, personal data, or other secrets.

## Important security notes

This is a learning project and should not be exposed directly to the public internet without further hardening.

- Some endpoints use **GET** requests for operations that change database state. Use POST for state-changing operations and add CSRF protections before production use.
- Table and column identifiers are built from request parameters in several queries. Validate identifiers against a strict allow-list; JDBC placeholders protect values, not SQL identifiers.
- Avoid returning raw database exception details to users in production.
- Add authentication/authorization and appropriate database privileges before deploying beyond a local learning environment.
- Review HTML output encoding to reduce cross-site scripting risk.

## Learning outcomes

This project demonstrates servlet routing with `@WebServlet`, request parameter handling, JDBC connections, prepared statements for values, result-set metadata, primary-key metadata, and dynamic HTML response generation.

## License

No license was supplied with the project. Add a license file if you want to grant others explicit permission to reuse or modify the code.
