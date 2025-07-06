package family.zambrana.starbound.whitelist;

import family.zambrana.starbound.MiniPlugin;
import family.zambrana.starbound.Starbound;
import family.zambrana.starbound.clientmanager.CoreClientManager;
import family.zambrana.starbound.database.DatabaseHolder;
import family.zambrana.starbound.whitelist.cmd.WhitelistCmd;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Whitelist extends MiniPlugin {
    private final Set<UUID> whitelistedUUIDs = new HashSet<>();
    private CoreClientManager clientManager;

    public Whitelist(CoreClientManager clientManager) {
        super("Whitelist Service");
        this.clientManager = clientManager;
        loadWhitelist();

        Starbound.getInstance().getCommand("whitelist").setExecutor(new WhitelistCmd(this, clientManager));
    }

    private void loadWhitelist() {
        whitelistedUUIDs.clear();
        try (Connection conn = DatabaseHolder.get().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT uuid FROM whitelist");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                whitelistedUUIDs.add(UUID.fromString(rs.getString("uuid")));
            }

            Bukkit.getLogger().info("Loaded " + whitelistedUUIDs.size() + " whitelisted players.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPlayer(OfflinePlayer player, String addedBy) {
        try (Connection conn = DatabaseHolder.get().getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT IGNORE INTO whitelist (uuid, name, added_by) VALUES (?, ?, ?)")) {

            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, player.getName());
            ps.setString(3, addedBy);
            ps.executeUpdate();

            whitelistedUUIDs.add(player.getUniqueId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removePlayer(UUID uuid) {
        try (Connection conn = DatabaseHolder.get().getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM whitelist WHERE uuid = ?")) {

            ps.setString(1, uuid.toString());
            ps.executeUpdate();

            whitelistedUUIDs.remove(uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isWhitelisted(UUID uuid) {
        return whitelistedUUIDs.contains(uuid);
    }

    public Set<UUID> getAll() {
        return whitelistedUUIDs;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (!isWhitelisted(player.getUniqueId())) {
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST,
                    "§8[§d✦ Atticus' Lovely World §8]§r\n\n" +
                            "§c§lACCESS DENIED\n" +
                            "§7You are not cleared for interstellar travel.\n" +
                            "§7This server requires whitelist authorization.\n\n" +
                            "§7If you believe this is an error,\n" +
                            "§7please contact an §badmin §7or apply on our Discord.\n\n" +
                            "§8> §oLaunch aborted.");
            Bukkit.getLogger().info("[Whitelist]: " + player.getName() + " tried to join, but is not whitelisted.");
        }
    }
}
