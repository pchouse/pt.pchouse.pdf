package pt.pchouse.pdf.api.request.datasource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Represents a database configuration and connection utility class.
 * This class is used to manage details required to connect to a database,
 * including the database provider, server address, port, schema, user credentials,
 * and connection string generation.
 * <p>
 * The class supports multiple database providers and offers functionality
 * to validate and construct JDBC connection strings.
 *
 * @since 1.0.0
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Database implements IDataSource
{

    /**
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Represents supported database providers for the application.
     * <p>
     * This enumeration lists various database providers that can be used
     * to configure database connections in the application. It serves as
     * a predefined set of database platforms that the system supports, such as:
     * <p>
     * - ORACLE
     * - SQLSERVER
     * - MYSQL
     * - POSTGRESQL
     * - H2
     * - DB2
     * - MARIADB
     * - DERBY
     * - HSQLDB
     * - SQLITE
     * - INFORMIX
     *
     * @since 1.0.0
     */
    public enum Provider
    {
        ORACLE,
        SQLSERVER,
        MYSQL,
        POSTGRESQL,
        H2,
        DB2,
        MARIADB,
        DERBY,
        HSQLDB,
        SQLITE,
        INFORMIX,
    }

    /**
     * The database provider used to establish the connection.
     * Specifies the type of database being interacted with, such as ORACLE, MYSQL, POSTGRESQL, etc.
     *
     * @since 1.0.0
     */
    private Provider provider;


    /**
     * The address of the server to which the database connects.
     * Or SQLITE file path
     *
     * @since 1.0.0
     */
    private String serverAddress;

    /**
     * The port number on which the server is configured to listen for connections.
     * It can be used to define or retrieve the specific port necessary for
     * establishing a connection with the server.
     *
     * @since 1.0.0
     */
    private Integer serverPort = null;

    /**
     * Represents the schema of the database.
     * <p>
     * This field holds the name of the schema to be used
     * for database operations. It is typically used to
     * specify a specific schema within a database that
     * contains the tables and other database objects
     * relevant to the application.
     *
     * @since 1.0.0
     */
    private String databaseSchema;

    /**
     * The username used for authentication with the database.
     *
     * @since 1.0.0
     */
    private String user;

    /**
     * The password used to authenticate with the database.
     * <p>
     * This field holds the user's password required for establishing a connection
     * to the specified database. It should be managed securely to prevent
     * unauthorized access.
     *
     * @since 1.0.0
     */
    private String password;

    /**
     * Indicates whether to encrypt the connection.
     * @since 1.0.0
     */
    private Boolean encrypt = true;

    /**
     * Indicates whether to trust the server certificate.
     * @since 1.0.0
     */
    private Boolean trustServerCertificate = true;

    /**
     * Default constructor for the Database class.
     * Initializes a new instance of the Database class, logging the instantiation event.
     *
     * @since 1.0.0
     */
    public Database() {
        logger.debug("New instance of {}", this.getClass().getName());
    }

    /**
     * Constructs a new Database instance using the specified parameters.
     *
     * @param provider       The database provider.
     * @param serverAddress  The address of the database server. Or SQLITE file path.
     * @param databaseSchema The schema name of the database.
     * @param user           The username for database authentication.
     * @param password       The password for database authentication.
     * @param serverPort     The port number on which the database server is running.
     * @since 1.0.0
     */
    public Database(Provider provider, String serverAddress, String databaseSchema, String user, String password, Integer serverPort) {
        logger.debug("New instance of {} for server {}", this.getClass().getName(), serverAddress);
        this.serverAddress  = serverAddress;
        this.databaseSchema = databaseSchema;
        this.user           = user;
        this.password       = password;
        this.serverPort     = serverPort;
        this.provider       = provider;
    }


    /**
     * Retrieves the database provider.
     *
     * @return The provider of the database.
     * @since 1.0.0
     */
    public Provider getProvider() {
        return provider;
    }

    /**
     * Sets the database provider.
     *
     * @param provider The database provider to be set.
     * @since 1.0.0
     */
    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    /**
     * Retrieves the address of the database server.
     * Or SQLITE file path.
     *
     * @return The server's address.
     * @since 1.0.0
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * Sets the address of the database server.
     * Or SQLITE file path.
     *
     * @param serverAddress The address of the server to be set.
     * @since 1.0.0
     */
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * Retrieves the port number of the database server.
     *
     * @return The port number, or null if not set.
     * @since 1.0.0
     */
    public Integer getServerPort() {
        return serverPort;
    }

    /**
     * Sets the port number for the database server.
     *
     * @param serverPort The port number to be set, or null if default.
     * @since 1.0.0
     */
    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * Retrieves the schema name of the database.
     *
     * @return The schema name.
     * @since 1.0.0
     */
    public String getDatabaseSchema() {
        return databaseSchema;
    }

    /**
     * Sets the schema name for the database.
     *
     * @param databaseSchema The schema name to be set.
     * @since 1.0.0
     */
    public void setDatabaseSchema(String databaseSchema) {
        this.databaseSchema = databaseSchema;
    }

    /**
     * Retrieves the username used for database authentication.
     *
     * @return The username.
     * @since 1.0.0
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the username for database authentication.
     *
     * @param user The username to be set.
     * @since 1.0.0
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Retrieves the password used for database authentication.
     *
     * @return The password.
     * @since 1.0.0
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for database authentication.
     *
     * @param password The password to be set.
     *
     * @since 1.0.0
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Indicates whether the connection should be encrypted.
     * @return True if the connection should be encrypted, false otherwise.
     *
     * @since 1.0.0
     */
    public Boolean getEncrypt() {
        return encrypt;
    }

    /**
     * Sets whether the connection should be encrypted.
     * @param encrypt True to encrypt the connection, false otherwise.
     *
     * @since 1.0.0
     */
    public void setEncrypt(Boolean encrypt) {
        this.encrypt = encrypt;
    }

    /**
     * Indicates whether to trust the server certificate.
     * @return True if the server certificate should be trusted, false otherwise.
     *
     * @since 1.0.0
     */
    public Boolean getTrustServerCertificate() {
        return trustServerCertificate;
    }

    /**
     * Sets whether to trust the server certificate.
     * @param trustServerCertificate True to trust the server certificate, false otherwise.
     *
     * @since 1.0.0
     */
    public void setTrustServerCertificate(Boolean trustServerCertificate) {
        this.trustServerCertificate = trustServerCertificate;
    }

    /**
     * Constructs and returns the JDBC connection string based on the database provider,
     * server address, port, and schema.
     *
     * @return The JDBC connection string for the configured database provider.
     * @throws IllegalStateException         If the provider, server address, or database schema is not set.
     * @throws UnsupportedOperationException If the database provider is not supported.
     * @since 1.0.0
     */
    public String getJdbcConnectionString() {

        if (provider == null) {
            throw new IllegalStateException("Database provider must be set.");
        }

        if (provider == Provider.SQLITE) {
            if (StringUtils.isBlank(serverAddress)) {
                throw new IllegalStateException("SQLITE database requires a file path set in serverAddress field.");
            }
        } else if (StringUtils.isBlank(serverAddress) || StringUtils.isBlank(databaseSchema)) {
            throw new IllegalStateException("Provider, server address, and database schema must be set.");
        }

        return switch (provider) {
            case ORACLE -> String.format("jdbc:oracle:thin:@%s:%d:%s", serverAddress,
                    serverPort == null ? 1521 : serverPort, databaseSchema);
            case SQLSERVER -> String.format(
                    "jdbc:sqlserver://%s%s;databaseName=%s;encrypt=%s;trustServerCertificate=%s",
                    serverAddress,
                    serverPort == null ? "" : (":" + serverPort),
                    databaseSchema,
                    encrypt ? "true" : "false",
                    trustServerCertificate ? "true" : "false"
            );
            case MYSQL, MARIADB -> String.format("jdbc:mysql://%s:%d/%s", serverAddress,
                    serverPort == null ? 3306 : serverPort, databaseSchema);
            case POSTGRESQL -> String.format("jdbc:postgresql://%s:%d/%s", serverAddress,
                    serverPort == null ? 5432 : serverPort, databaseSchema);
            case H2 -> String.format("jdbc:h2:tcp://%s:%d/%s", serverAddress,
                    serverPort == null ? 9092 : serverPort, databaseSchema);
            case SQLITE -> String.format("jdbc:sqlite:%s", serverAddress);
            case DB2 -> String.format("jdbc:db2://%s:%d/%s", serverAddress,
                    serverPort == null ? 50000 : serverPort, databaseSchema);
            case DERBY -> String.format("jdbc:derby://%s:%d/%s", serverAddress,
                    serverPort == null ? 1527 : serverPort, databaseSchema);
            case HSQLDB -> String.format("jdbc:hsqldb:hsql://%s:%d/%s", serverAddress,
                    serverPort == null ? 9001 : serverPort, databaseSchema);
            case INFORMIX -> String.format("jdbc:informix-sqli://%s:%d/%s:INFORMIXSERVER=server_name",
                    serverAddress, serverPort == null ? 1526 : serverPort, databaseSchema);
            default -> throw new UnsupportedOperationException("Unsupported database provider: " + provider);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Database database = (Database) o;
        return serverAddress.equals(database.serverAddress)
                && databaseSchema.equals(database.databaseSchema)
                && user.equals(database.user)
                && password.equals(database.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, serverAddress, serverPort, databaseSchema, user, password);
    }

    @Override
    public String toString() {

        return "Database{" +
                "provider=" + provider +
                ", serverAddress='" + serverAddress + '\'' +
                ", serverPort=" + serverPort +
                ", databaseSchema='" + databaseSchema + '\'' +
                ", user='" + user + '\'' +
                ", password='*********'" + '\'' +
                '}';
    }
}
