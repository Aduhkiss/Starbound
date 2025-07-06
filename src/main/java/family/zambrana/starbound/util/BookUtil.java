package family.zambrana.starbound.util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

public class BookUtil {

    public static class ClickablePage {
        private final String text;
        private final String command;

        public ClickablePage(String text, String command) {
            this.text = text;
            this.command = command;
        }

        public String getText() {
            return text;
        }

        public String getCommand() {
            return command;
        }
    }

    public static void openBook(Player player, String intro, List<ClickablePage> clickablePages) {
        List<String> pages = new ArrayList<>();
        StringBuilder current = new StringBuilder(intro + "\n\n");

        for (ClickablePage clickable : clickablePages) {
            current.append(clickable.getText()).append("\n");
        }

        pages.add(current.toString());

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setTitle("Nickname Setup");
        meta.setAuthor("System");
        meta.setPages(pages);
        book.setItemMeta(meta);

        int slot = player.getInventory().getHeldItemSlot();
        ItemStack prev = player.getInventory().getItem(slot);
        player.getInventory().setItem(slot, book);
        player.openBook(book);

        Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("Starbound"), () -> {
            player.getInventory().setItem(slot, prev);
        }, 1L);

        // Fallback chat lines
        Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("Starbound"), () -> {
            player.sendMessage("§7Click a line below:");
            for (ClickablePage clickable : clickablePages) {
                TextComponent line = new TextComponent(clickable.getText());
                line.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickable.getCommand()));
                player.spigot().sendMessage(line);
            }
        }, 10L);
    }
}
