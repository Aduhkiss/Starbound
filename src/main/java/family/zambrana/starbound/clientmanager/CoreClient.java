package family.zambrana.starbound.clientmanager;

import family.zambrana.starbound.database.DatabaseHolder;
import family.zambrana.starbound.util.Rank;
import org.bukkit.entity.Player;
import xyz.haoshoku.nick.api.NickAPI;

import java.sql.*;
import java.time.Instant;

public class CoreClient {

    private final Player player;
    private Rank rank = Rank.PLAYER;
    private boolean firstJoin = false;
    private String nickname;
    private String skin;

    public CoreClient(Player player) {
        this.player = player;
        this.nickname = player.getName(); // fallback default
        loadOrCreatePlayerData();
    }

    public void updateTablistName() {
        String fullName = rank.getPrefix() + nickname;
        player.setPlayerListName(fullName);
    }

    // Make this shit public so that we can call from upon it later
    public void loadOrCreatePlayerData() {
        try (Connection conn = DatabaseHolder.get().getConnection()) {
            String uuid = player.getUniqueId().toString();

            PreparedStatement select = conn.prepareStatement("SELECT * FROM players WHERE uuid = ?");
            select.setString(1, uuid);

            ResultSet rs = select.executeQuery();

            if (rs.next()) {
                // Load rank
                String rankStr = rs.getString("rank");
                try {
                    this.rank = Rank.valueOf(rankStr.toUpperCase());
                } catch (IllegalArgumentException ignored) {
                    this.rank = Rank.PLAYER;
                }

                // Load nickname and fallback if null
                this.nickname = rs.getString("display_name");
                this.skin = rs.getString("skin_name");
                if (this.nickname == null || this.nickname.trim().isEmpty()) {
                    this.nickname = player.getName();

                    // Patch DB to fix null nickname
                    PreparedStatement patch = conn.prepareStatement(
                            "UPDATE players SET display_name = ? WHERE uuid = ?"
                    );
                    patch.setString(1, this.nickname);
                    patch.setString(2, uuid);
                    patch.executeUpdate();
                }

                player.setDisplayName(this.nickname);

                // Update IP address
                PreparedStatement update = conn.prepareStatement(
                        "UPDATE players SET last_ip = ? WHERE uuid = ?"
                );
                update.setString(1, player.getAddress().getAddress().getHostAddress());
                update.setString(2, uuid);
                update.executeUpdate();

            } else {
                // First-time player
                firstJoin = true;

                this.nickname = player.getName();
                player.setDisplayName(this.nickname);

                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO players (uuid, name, rank, first_join, last_ip, display_name) VALUES (?, ?, ?, ?, ?, ?)"
                );
                insert.setString(1, uuid);
                insert.setString(2, player.getName());
                insert.setString(3, Rank.PLAYER.name());
                insert.setTimestamp(4, Timestamp.from(Instant.now()));
                insert.setString(5, player.getAddress().getAddress().getHostAddress());
                insert.setString(6, this.nickname);
                insert.executeUpdate();
            }

            player.setDisplayName(nickname);
            updateTablistName();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Rank getRank() {
        return rank;
    }

    public String getRankPrefix() {
        return rank.getPrefix();
    }

    public boolean isFirstJoin() {
        return firstJoin;
    }

    public String getNickname() {
        return nickname != null ? nickname : player.getName();
    }

    public String getSkin() {
        return skin;
    }

    public void updateSkin(String skinName) {
        this.skin = skinName;
        try (Connection conn = DatabaseHolder.get().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE players SET skin_name = ? WHERE uuid = ?"
            );
            stmt.setString(1, skinName);
            stmt.setString(2, player.getUniqueId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Placeholder for actual skin change
        player.sendMessage("§a[Starbound] Skin updated to: " + skinName);
        updateTablistName();
    }

    public void updateNickname(String newName) {
        this.nickname = newName;
        player.setDisplayName(newName);

        try (Connection conn = DatabaseHolder.get().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE players SET display_name = ? WHERE uuid = ?"
            );
            stmt.setString(1, newName);
            stmt.setString(2, player.getUniqueId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateTablistName();
    }
    public void resetNick() {
        // Reset with NickAPI
        NickAPI.resetNick(player);
        NickAPI.resetSkin(player);
        NickAPI.resetUniqueId(player);
        NickAPI.resetProfileName(player);
        NickAPI.refreshPlayer(player);

        // Reset nickname in database
        this.nickname = player.getName(); // original name
        try (Connection conn = DatabaseHolder.get().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE players SET display_name = ?, skin_name = ? WHERE uuid = ?"
            );
            stmt.setString(1, player.getName());
            stmt.setString(2, "NORMAL");
            stmt.setString(3, player.getUniqueId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        updateTablistName();

        player.setDisplayName(player.getName());
        player.sendMessage("§cYour nickname and skin have been reset.");
    }

    public void setRank(Rank newRank) {
        this.rank = newRank;
        updateTablistName(); // if you use tab updates
        player.sendMessage("§aYour rank has been updated to: " + newRank.getPrefix());
    }
}
