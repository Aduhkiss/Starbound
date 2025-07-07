package family.zambrana.starbound.chat;

import family.zambrana.starbound.MiniPlugin;
import family.zambrana.starbound.clientmanager.CoreClient;
import family.zambrana.starbound.clientmanager.CoreClientManager;
import family.zambrana.starbound.staff.Staff;
import family.zambrana.starbound.util.Rank;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class StarboundChat extends MiniPlugin {

    private final CoreClientManager clientManager;
    private final Staff staff;

    public StarboundChat(CoreClientManager clientManager, Staff staff) {
        super("Chat Service");
        this.clientManager = clientManager;
        this.staff = staff;
    }

    @EventHandler
    public void onPlayerChatted(AsyncPlayerChatEvent ev) {
        ev.setCancelled(true);

        CoreClient client = clientManager.getClient(ev.getPlayer());
        Rank rank = client.getRank();

        String nick = client.getNickname();
        String message = ev.getMessage();

        // Staff channel support with '@'
        if (client.has(Rank.HELPER) && message.startsWith("@")) {
            String staffMessage = message.substring(1).trim();
            staff.send(ev.getPlayer(), staffMessage);
            return;
        }

        // Create prefix with hover
        String displayPrefix = client.getDisplayPrefix();
        TextComponent prefixComponent = new TextComponent(displayPrefix);
        prefixComponent.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(getHoverText(client)).create()
        ));

        // Color the player's nickname to match the first color in the prefix
        String prefixColor = extractFirstColorCode(displayPrefix);
        TextComponent nameComponent = new TextComponent(prefixColor + nick + "§f: ");

        // Plain message text
        TextComponent messageComponent = new TextComponent(message);

        // Full combined message
        TextComponent fullMessage = new TextComponent();
        fullMessage.addExtra(prefixComponent);
        fullMessage.addExtra(nameComponent);
        fullMessage.addExtra(messageComponent);

        for (Player pl : Bukkit.getOnlinePlayers()) {
            pl.spigot().sendMessage(fullMessage);
        }
        // ha dumbass didnt add message logging
        Bukkit.getLogger().info("[" + ev.getPlayer().getName() + "]: " + ev.getMessage());
    }

    private String extractFirstColorCode(String text) {
        for (int i = 0; i < text.length() - 1; i++) {
            if (text.charAt(i) == '§') {
                return "§" + text.charAt(i + 1);
            }
        }
        return "§f"; // fallback to white
    }

    private String getHoverText(CoreClient client) {
        String prefix = client.getDisplayPrefix();
        Rank disguisedRank = Rank.PLAYER;

        for (Rank r : Rank.values()) {
            if (r.getPrefix().equalsIgnoreCase(prefix)) {
                disguisedRank = r;
                break;
            }
        }

        return "§e" + disguisedRank.getName() + "\n§7" + disguisedRank.getDescription();
    }
}
