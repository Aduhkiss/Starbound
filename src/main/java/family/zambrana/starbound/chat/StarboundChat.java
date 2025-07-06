package family.zambrana.starbound.chat;

import family.zambrana.starbound.MiniPlugin;
import family.zambrana.starbound.clientmanager.CoreClient;
import family.zambrana.starbound.clientmanager.CoreClientManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class StarboundChat extends MiniPlugin {

    private final CoreClientManager clientManager;

    public StarboundChat(CoreClientManager clientManager) {
        super("Chat Service");
        this.clientManager = clientManager;
    }

    @EventHandler
    public void onPlayerChatted(AsyncPlayerChatEvent ev) {
        ev.setCancelled(true);

        CoreClient client = clientManager.getClient(ev.getPlayer());

        String prefix = client.getDisplayPrefix();
        String nick = client.getNickname();
        String message = ev.getMessage();

        for (Player pl : Bukkit.getOnlinePlayers()) {
            pl.sendMessage(prefix + nick + "§f: " + message);
        }
    }
}
