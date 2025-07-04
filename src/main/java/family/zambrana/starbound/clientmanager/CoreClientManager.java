package family.zambrana.starbound.clientmanager;

import family.zambrana.starbound.MiniPlugin;
import family.zambrana.starbound.Starter;
import family.zambrana.starbound.clientmanager.cmd.RankCmd;
import family.zambrana.starbound.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CoreClientManager extends MiniPlugin {

    private final Database db;
    private final Map<Player, CoreClient> clients = new HashMap<>();

    public CoreClientManager(Database db) {
        super("Core Client Manager");
        this.db = db;

        Starter.getInstance().getCommand("rank").setExecutor(new RankCmd(this));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        CoreClient client = new CoreClient(player);
        clients.put(player, client);

        if (client.isFirstJoin()) {
            player.sendMessage("§8§m-----------------------------------------");
            player.sendMessage("");
            player.sendMessage("§b§l★ §r§aWelcome to §lAtticus' Lovely World§a, §e" + player.getName() + "§a!");
            player.sendMessage("§7We're glad you're here. Enjoy your stay!");
            player.sendMessage("");
            player.sendMessage("§8§m-----------------------------------------");
        } else {
            player.sendMessage("§8§m-----------------------------------------");
            player.sendMessage("");
            player.sendMessage("§b§l★ §r§aWelcome back to §lAtticus' Lovely World§a, §e" + player.getName() + "§a!");
            player.sendMessage("§7Great to see you again. Let’s make this session awesome!");
            player.sendMessage("");
            player.sendMessage("§8§m-----------------------------------------");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        clients.remove(event.getPlayer());
    }

    public CoreClient getClient(Player player) {
        return clients.get(player);
    }

    public CoreClient getClient(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        return player != null ? clients.get(player) : null;
    }
}
