package family.zambrana.starbound.database;

import family.zambrana.starbound.MiniPlugin;
import family.zambrana.starbound.Starbound;

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
            String host = Starbound.getInstance().getConfig().getString("database.host");
            int port = Starbound.getInstance().getConfig().getInt("database.port");
            String database = Starbound.getInstance().getConfig().getString("database.name");
            String username = Starbound.getInstance().getConfig().getString("database.username");
            String password = Starbound.getInstance().getConfig().getString("database.password");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database +
                    "?useSSL=false&allowPublicKeyRetrieval=true&autoReconnect=true";

            connection = DriverManager.getConnection(url, username, password);
            Starbound.getInstance().getLogger().info("[Database] Connected to MySQL.");
        } catch (SQLException e) {
            Starbound.getInstance().getLogger().log(Level.SEVERE, "[Database] Connection failed!", e);
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(1)) {
                connect();
            }
        } catch (SQLException e) {
            Starbound.getInstance().getLogger().log(Level.WARNING, "[Database] Connection was not valid. Reconnecting...", e);
            connect();
        }
        return connection;
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                Starbound.getInstance().getLogger().info("[Database] Disconnected.");
            }
        } catch (SQLException e) {
            Starbound.getInstance().getLogger().log(Level.WARNING, "[Database] Error during disconnect.", e);
        }
    }
}
