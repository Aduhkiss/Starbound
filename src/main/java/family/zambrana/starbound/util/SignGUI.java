package family.zambrana.starbound.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SignGUI implements Listener {

    private static final Map<Player, Consumer<String>> inputMap = new HashMap<>();
    private static final Plugin plugin = Bukkit.getPluginManager().getPlugin("Starbound");

    public static void open(Player player, String title, Consumer<String> callback) {
        inputMap.put(player, callback);
        player.sendMessage("§7[Sign GUI: " + title + "] Please type your input in chat.");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (inputMap.containsKey(player)) {
            event.setCancelled(true);
            Consumer<String> action = inputMap.remove(player);
            if (action != null) {
                action.accept(event.getMessage());
            }
        }
    }

    public static void register() {
        Bukkit.getPluginManager().registerEvents(new SignGUI(), plugin);
    }
}
