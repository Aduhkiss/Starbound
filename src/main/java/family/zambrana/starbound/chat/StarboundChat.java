package family.zambrana.starbound.chat;

import family.zambrana.starbound.MiniPlugin;
import family.zambrana.starbound.clientmanager.CoreClient;
import family.zambrana.starbound.clientmanager.CoreClientManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class StarboundChat extends MiniPlugin {

    private CoreClientManager clientManager;
    public StarboundChat(CoreClientManager clientManager) {
        super("Chat Service");
        this.clientManager = clientManager;
    }

    @EventHandler
    public void onPlayerChatted(AsyncPlayerChatEvent ev) {
        // first stop bitch ass minecraft from handling the chat message
        // also fuck mojang chat censors
        ev.setCancelled(true);

        // then lookup the player's information
        CoreClient client = clientManager.getClient(ev.getPlayer());

        // then broadcast their message to the whole server manually
        for(Player pl : Bukkit.getOnlinePlayers()) {
            pl.sendMessage(client.getRankPrefix() + client.getNickname() + "§f: " + ev.getMessage());
        }
    }
}
