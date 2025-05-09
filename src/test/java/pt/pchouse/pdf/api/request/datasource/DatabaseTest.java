package pt.pchouse.pdf.api.request.datasource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class DatabaseTest
{

    /**
     * Tests the {@link Database#setServerAddress(String)} and {@link Database#getServerAddress()} methods
     * to verify that the server address is properly set and retrieved.
     */
    @Test
    void testServerAddressSetterGetter() {
        // Arrange
        Database database      = new Database();
        String   serverAddress = "localhost";

        // Act
        database.setServerAddress(serverAddress);

        // Assert
        assertEquals(serverAddress, database.getServerAddress(), "Server address should match the one set.");
    }

    /**
     * Tests the {@link Database#setDatabaseSchema(String)} and {@link Database#getDatabaseSchema()} methods
     * to verify that the database schema is properly set and retrieved.
     */
    @Test
    void testDatabaseSchemaSetterGetter() {
        // Arrange
        Database database       = new Database();
        String   databaseSchema = "test_schema";

        // Act
        database.setDatabaseSchema(databaseSchema);

        // Assert
        assertEquals(databaseSchema, database.getDatabaseSchema(), "Database schema should match the one set.");
    }

    /**
     * Tests the {@link Database#setUser(String)} and {@link Database#getUser()} methods
     * to verify that the user is properly set and retrieved.
     */
    @Test
    void testUserSetterGetter() {
        // Arrange
        Database database = new Database();
        String   user     = "test_user";

        // Act
        database.setUser(user);

        // Assert
        assertEquals(user, database.getUser(), "User should match the one set.");
    }

    /**
     * Tests the {@link Database#setPassword(String)} and {@link Database#getPassword()} methods
     * to verify that the password is properly set and retrieved.
     */
    @Test
    void testPasswordSetterGetter() {
        // Arrange
        Database database = new Database();
        String   password = "test_password";

        // Act
        database.setPassword(password);

        // Assert
        assertEquals(password, database.getPassword(), "Password should match the one set.");
    }

    /**
     * Tests the {@link Database#setServerPort(Integer)} and {@link Database#getServerPort()} methods
     * to verify that the server port is properly set and retrieved.
     */
    @Test
    void testServerPortSetterGetter() {
        // Arrange
        Database database   = new Database();
        Integer  serverPort = 5432;

        // Act
        database.setServerPort(serverPort);

        // Assert
        assertEquals(serverPort, database.getServerPort(), "Server port should match the one set.");
    }

    /**
     * Tests the {@link Database#setProvider(Database.Provider)} and {@link Database#getProvider()} methods
     * to verify that the provider is properly set and retrieved.
     */
    @Test
    void testProviderSetterGetter() {
        // Arrange
        Database          database = new Database();
        Database.Provider provider = Database.Provider.POSTGRESQL;

        // Act
        database.setProvider(provider);

        // Assert
        assertEquals(provider, database.getProvider(), "Provider should match the one set.");
    }

    /**
     * Tests the {@link Database#getProvider()} method to verify that it correctly retrieves
     * the provider when it has been explicitly set.
     */
    @Test
    void testGetProviderWhenProviderIsSet() {
        // Arrange
        Database          database         = new Database();
        Database.Provider expectedProvider = Database.Provider.POSTGRESQL;
        database.setProvider(expectedProvider);

        // Act
        Database.Provider actualProvider = database.getProvider();

        // Assert
        assertEquals(expectedProvider, actualProvider, "The provider should match the one set.");
    }

    /**
     * Tests the {@link Database#Database(Database.Provider, String, String, String, String, Integer)} constructor
     * to verify that the properties are properly initialized.
     */
    @Test
    void testDatabaseConstructor() {
        // Arrange
        String  serverAddress  = "localhost";
        String  databaseSchema = "test_schema";
        String  user           = "test_user";
        String  password       = "test_password";
        Integer serverPort     = 5432;

        // Act
        Database database = new Database(
               Database.Provider.POSTGRESQL, serverAddress, databaseSchema, user, password, serverPort
        );

        // Assert
        assertEquals(serverAddress, database.getServerAddress(), "Server address should be properly set.");
        assertEquals(databaseSchema, database.getDatabaseSchema(), "Database schema should be properly set.");
        assertEquals(user, database.getUser(), "User should be properly set.");
        assertEquals(password, database.getPassword(), "Password should be properly set.");
        assertEquals(serverPort, database.getServerPort(), "Server port should be properly set.");
    }


    /**
     * Tests the {@link Database#Database(Database.Provider,String, String, String, String, Integer)} constructor
     * to verify that the properties are properly initialized.
     */
    @Test
    void testDatabaseConstructorPortNull() {
        // Arrange
        String  serverAddress  = "localhost";
        String  databaseSchema = "test_schema";
        String  user           = "test_user";
        String  password       = "test_password";

        // Act
        Database database = new Database(
                Database.Provider.POSTGRESQL, serverAddress, databaseSchema, user, password, null
        );

        // Assert
        assertEquals(serverAddress, database.getServerAddress(), "Server address should be properly set.");
        assertEquals(databaseSchema, database.getDatabaseSchema(), "Database schema should be properly set.");
        assertEquals(user, database.getUser(), "User should be properly set.");
        assertEquals(password, database.getPassword(), "Password should be properly set.");
        assertEquals(Database.Provider.POSTGRESQL, database.getProvider(), "Provider should be properly set.");
        assertNull(database.getServerPort(), "Server port should be null.");
    }


    /**
     * Tests the {@link Database#getProvider()} method to verify that it returns null
     * when the provider has not been set.
     */
    @Test
    void testGetProviderWhenProviderIsNotSet() {
        // Arrange
        Database database = new Database();

        // Act
        Database.Provider actualProvider = database.getProvider();

        // Assert
        assertNull(actualProvider, "The provider should be null if it has not been set.");
    }

    /**
     * Tests the {@link Database#getJdbcConnectionString()} method to verify that it generates the correct
     * JDBC connection string
     * for all providers.
     */
    @Test
    void testGetJdbcConnectionString() {
        // Arrange
        String  serverAddress  = "localhost";
        String  databaseSchema = "test_schema";
        String  user           = "test_user";
        String  password       = "test_password";
        Integer serverPort     = 5432;

        // Test for PostgresSQL provider
        Database databasePostgreSQL = new Database(
                Database.Provider.POSTGRESQL, serverAddress, databaseSchema, user, password, serverPort
        );
        String expectedPostgreSqlConnectionString = "jdbc:postgresql://localhost:5432/test_schema";
        assertEquals(expectedPostgreSqlConnectionString, databasePostgreSQL.getJdbcConnectionString(),
                "The JDBC connection string for PostgresSQL should be correctly generated.");

        // Test for MySQL provider
        Database databaseMySQL = new Database(
                Database.Provider.MYSQL, serverAddress, databaseSchema, user, password, serverPort
        );
        String expectedMySqlConnectionString = "jdbc:mysql://localhost:5432/test_schema";
        assertEquals(expectedMySqlConnectionString, databaseMySQL.getJdbcConnectionString(),
                "The JDBC connection string for MySQL should be correctly generated.");

        // Test for SQLite provider
        String   serverAddressSqlite = "/path/to/test_schema";
        Database databaseSQLite      = new Database(
                Database.Provider.SQLITE,
                serverAddressSqlite,
                serverAddressSqlite,
                user,
                password,
                null
        );

        String expectedSqliteConnectionString = "jdbc:sqlite:/path/to/test_schema";
        assertEquals(expectedSqliteConnectionString, databaseSQLite.getJdbcConnectionString(),
                "The JDBC connection string for SQLite should be correctly generated.");
    }


}