package family.zambrana.starbound.util;

import family.zambrana.starbound.nickname.GUIHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.function.Consumer;

public class GUIBuilder {

    private final Inventory inventory;
    private final HashMap<Integer, Runnable> handlers = new HashMap<>();

    public GUIBuilder(String title, int size) {
        this.inventory = Bukkit.createInventory(null, size, title);
    }

    public void setItem(int slot, ItemStack item, Runnable onClick) {
        inventory.setItem(slot, item);
        handlers.put(slot, onClick);
    }

    public void open(Player player) {
        player.openInventory(inventory);
        GUIHandler.register(player, handlers);
    }

    public static ItemStack namedItem(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
