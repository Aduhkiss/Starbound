package family.zambrana.starbound.clientmanager;

import family.zambrana.starbound.database.DatabaseHolder;
import family.zambrana.starbound.nickname.NickManagerRegistry;
import family.zambrana.starbound.util.Rank;
import org.bukkit.entity.Player;

import java.sql.*;
import java.time.Instant;

public class CoreClient {

    private final Player player;
    private Rank rank = Rank.PLAYER;
    private boolean firstJoin = false;
    private String nickname;
    private String skin;
    private String fakePrefix;

    public CoreClient(Player player) {
        this.player = player;
        this.nickname = player.getName(); // fallback default
        loadOrCreatePlayerData();
    }

    public void updateTablistName() {
        String fullName = getDisplayPrefix() + nickname;
        player.setPlayerListName(fullName);
    }

    public void loadOrCreatePlayerData() {
        try (Connection conn = DatabaseHolder.get().getConnection()) {
            String uuid = player.getUniqueId().toString();

            PreparedStatement select = conn.prepareStatement("SELECT * FROM players WHERE uuid = ?");
            select.setString(1, uuid);

            ResultSet rs = select.executeQuery();

            if (rs.next()) {
                this.rank = Rank.safeValueOf(rs.getString("rank"));
                this.nickname = rs.getString("display_name");
                this.skin = rs.getString("skin_name");
                this.fakePrefix = rs.getString("fake_rank_prefix");

                if (nickname == null || nickname.trim().isEmpty()) {
                    this.nickname = player.getName();
                    PreparedStatement patch = conn.prepareStatement(
                            "UPDATE players SET display_name = ? WHERE uuid = ?"
                    );
                    patch.setString(1, this.nickname);
                    patch.setString(2, uuid);
                    patch.executeUpdate();
                }

                player.setDisplayName(nickname);

                PreparedStatement update = conn.prepareStatement(
                        "UPDATE players SET last_ip = ? WHERE uuid = ?"
                );
                update.setString(1, player.getAddress().getAddress().getHostAddress());
                update.setString(2, uuid);
                update.executeUpdate();

            } else {
                firstJoin = true;
                this.nickname = player.getName();
                this.skin = "NORMAL";
                player.setDisplayName(nickname);

                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO players (uuid, name, rank, first_join, last_ip, display_name, skin_name) VALUES (?, ?, ?, ?, ?, ?, ?)"
                );
                insert.setString(1, uuid);
                insert.setString(2, player.getName());
                insert.setString(3, Rank.PLAYER.name());
                insert.setTimestamp(4, Timestamp.from(Instant.now()));
                insert.setString(5, player.getAddress().getAddress().getHostAddress());
                insert.setString(6, nickname);
                insert.setString(7, skin);
                insert.executeUpdate();
            }

            updateTablistName();

            if (!nickname.equals(player.getName()) || (skin != null && !skin.equalsIgnoreCase("NORMAL"))) {
                NickManagerRegistry.get().applyNickname(player, nickname, skin);
            }

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

    public String getDisplayPrefix() {
        return (fakePrefix != null && !fakePrefix.isEmpty()) ? fakePrefix : rank.getPrefix();
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

    public boolean has(Rank rank) {
        return getRank().getLevel() >= rank.getLevel();
    }

    public void updateNickname(String newName) {
        this.nickname = newName;
        player.setDisplayName(newName);
        updateTablistName();

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

        NickManagerRegistry.get().applyNickname(player, newName, this.skin != null ? this.skin : "NORMAL");
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

        //player.sendMessage("§a[Starbound] Skin updated to: " + skinName);
        NickManagerRegistry.get().applyNickname(player, nickname, skinName);
    }

    public void setFakePrefix(String prefix) {
        this.fakePrefix = prefix;
        try (Connection conn = DatabaseHolder.get().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE players SET fake_rank_prefix = ? WHERE uuid = ?"
            );
            stmt.setString(1, prefix);
            stmt.setString(2, player.getUniqueId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateTablistName();
    }

    public void resetFakePrefix() {
        this.fakePrefix = null;
        try (Connection conn = DatabaseHolder.get().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE players SET fake_rank_prefix = NULL WHERE uuid = ?"
            );
            stmt.setString(1, player.getUniqueId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateTablistName();
    }

    public void resetNick() {
        this.nickname = player.getName();
        this.skin = "NORMAL";
        this.fakePrefix = null;

        player.setDisplayName(nickname);
        updateTablistName();

        try (Connection conn = DatabaseHolder.get().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE players SET display_name = ?, skin_name = ?, fake_rank_prefix = NULL WHERE uuid = ?"
            );
            stmt.setString(1, nickname);
            stmt.setString(2, "NORMAL");
            stmt.setString(3, player.getUniqueId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        NickManagerRegistry.get().resetNickname(player);
        //player.sendMessage("§cYour nickname, rank prefix, and skin have been reset.");
    }

    public void setRank(Rank newRank) {
        this.rank = newRank;
        updateTablistName();
        player.sendMessage("§aYour rank has been updated to: " + newRank.getName() + "!");
    }
}
