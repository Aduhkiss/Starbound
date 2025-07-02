package family.zambrana.starbound.database;

import family.zambrana.starbound.MiniPlugin;
import family.zambrana.starbound.Starter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class Database extends MiniPlugin {

    private Connection connection;

    public Database() {
        super("Database");
        connect(); // Initial connection
    }

    private void connect() {
        try {
            String host = Starter.getInstance().getConfig().getString("database.host");
            int port = Starter.getInstance().getConfig().getInt("database.port");
            String database = Starter.getInstance().getConfig().getString("database.name");
            String username = Starter.getInstance().getConfig().getString("database.username");
            String password = Starter.getInstance().getConfig().getString("database.password");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database +
                    "?useSSL=false&allowPublicKeyRetrieval=true&autoReconnect=true";

            connection = DriverManager.getConnection(url, username, password);
            Starter.getInstance().getLogger().info("[Database] Connected to MySQL.");
        } catch (SQLException e) {
            Starter.getInstance().getLogger().log(Level.SEVERE, "[Database] Connection failed!", e);
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(1)) {
                connect();
            }
        } catch (SQLException e) {
            Starter.getInstance().getLogger().log(Level.WARNING, "[Database] Connection was not valid. Reconnecting...", e);
            connect();
        }
        return connection;
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                Starter.getInstance().getLogger().info("[Database] Disconnected.");
            }
        } catch (SQLException e) {
            Starter.getInstance().getLogger().log(Level.WARNING, "[Database] Error during disconnect.", e);
        }
    }
}
