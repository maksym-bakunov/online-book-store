package com.example.onlinebookstore.liquibase;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Class UpdateUsersPassword is used by Liquibase script to convert the previous simple user
 * password to a new hashed password.
 *
 * There is no Spring context during the execution of execute method by liquibase script. This is
 * why JDBC is uses to work with database.
 *
 * @author Maksym Bakunov
 * @version 1.0
 * @since 2024-10-05
 */
public class UpdateUsersPassword implements CustomTaskChange {
    private static final String PROPERTIES_NAME_FILE = "application.properties";
    private static final String PROPERTY_JDBC_URL = "spring.datasource.url";
    private static final String PROPERTY_USERNAME = "spring.datasource.username";
    private static final String PROPERTY_PASSWORD = "spring.datasource.password";
    private String jdbcUrl;
    private String username;
    private String password;

    public UpdateUsersPassword() {
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream(PROPERTIES_NAME_FILE)) {

            Properties prop = new Properties();
            prop.load(input);
            jdbcUrl = prop.getProperty(PROPERTY_JDBC_URL);
            username = prop.getProperty(PROPERTY_USERNAME);
            password = prop.getProperty(PROPERTY_PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load database properties", e);
        }
    }

    @Override
    public void execute(Database database) throws CustomChangeException {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            Map<Long, String> usersPasswords = getAllUsers(connection);
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            for (Map.Entry<Long, String> userPassword : usersPasswords.entrySet()) {
                updateUserPassword(connection, userPassword.getKey(),
                        passwordEncoder.encode(userPassword.getValue()));
            }
        } catch (Exception e) {
            throw new CustomChangeException("Error updating user passwords", e);
        }
    }

    private Map<Long, String> getAllUsers(Connection connection) throws Exception {
        String sql = "SELECT id, password FROM users WHERE password NOT LIKE '$2a$%'";
        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            Map<Long, String> usersPasswords = new HashMap<>();

            while (resultSet.next()) {
                usersPasswords.put(resultSet.getLong("id"), resultSet.getString("password"));
            }
            return usersPasswords;
        }
    }

    private void updateUserPassword(Connection connection, Long userId,
                                    String newPassword) throws Exception {

        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newPassword);
            statement.setLong(2, userId);
            statement.executeUpdate();
        }
    }

    @Override
    public String getConfirmationMessage() {
        return null;
    }

    @Override
    public void setUp() throws SetupException {

    }

    @Override
    public void setFileOpener(ResourceAccessor resourceAccessor) {

    }

    @Override
    public ValidationErrors validate(Database database) {
        return null;
    }
}
