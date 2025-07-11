package family.zambrana.starbound.clientmanager;

import family.zambrana.starbound.MiniPlugin;
import family.zambrana.starbound.Starbound;
import family.zambrana.starbound.clientmanager.cmd.GameModeCmd;
import family.zambrana.starbound.clientmanager.cmd.RankCmd;
import family.zambrana.starbound.database.Database;
import family.zambrana.starbound.database.DatabaseHolder;
import family.zambrana.starbound.util.Rank;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CoreClientManager extends MiniPlugin {

    private final Database db;
    private final Map<Player, CoreClient> clients = new HashMap<>();

    public CoreClientManager(Database db) {
        super("Core Client Manager");
        this.db = db;

        Starbound.getInstance().getCommand("rank").setExecutor(new RankCmd(this));
        Starbound.getInstance().getCommand("gamemode").setExecutor(new GameModeCmd(this));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        CoreClient client = new CoreClient(player);
        clients.put(player, client);

        String nick = client.getNickname();
        String prefix = client.getDisplayPrefix();

        event.setJoinMessage("§e" + prefix + nick + " §ejoined the game");

        if (client.isFirstJoin()) {
            player.sendMessage("§8§m-----------------------------------------");
            player.sendMessage("");
            player.sendMessage("§b§l★ §r§aWelcome to §lAtticus' Lovely World§a, §e" + nick + "§a!");
            player.sendMessage("§7We're glad you're here. Enjoy your stay!");
            player.sendMessage("");
            player.sendMessage("§8§m-----------------------------------------");
        } else {
            player.sendMessage("§8§m-----------------------------------------");
            player.sendMessage("");
            player.sendMessage("§b§l★ §r§aWelcome back to §lAtticus' Lovely World§a, §e" + nick + "§a!");
            player.sendMessage("§7Great to see you again. Let’s make this session awesome!");
            player.sendMessage("");
            player.sendMessage("§8§m-----------------------------------------");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        CoreClient client = clients.remove(event.getPlayer());

        if (client != null) {
            String nick = client.getNickname();
            String prefix = client.getDisplayPrefix();
            event.setQuitMessage("§e" + prefix + nick + " §eleft the game");
        } else {
            // Fallback in case client was not loaded properly
            event.setQuitMessage("§e" + event.getPlayer().getName() + " §eleft the game");
        }
    }

    public CoreClient getClient(Player player) {
        return clients.get(player);
    }

    public CoreClient getClient(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        return player != null ? clients.get(player) : null;
    }

    public CoreClient getClient(OfflinePlayer player) {
        Player online = player.getPlayer();
        if (online != null && online.isOnline()) {
            return getClient(online);
        }
        return null;
    }

    public Rank getRank(UUID uuid) {
        try (Connection conn = DatabaseHolder.get().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT rank FROM players WHERE uuid = ?");
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Rank.safeValueOf(rs.getString("rank"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Rank.PLAYER; // fallback if no record exists
    }

    public Rank getRank(OfflinePlayer player) {
        return getRank(player.getUniqueId());
    }
}