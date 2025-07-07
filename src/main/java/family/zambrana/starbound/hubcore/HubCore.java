package family.zambrana.starbound.hubcore;

import family.zambrana.starbound.MiniPlugin;
import family.zambrana.starbound.Starbound;
import family.zambrana.starbound.hubcore.cmd.ServersCmd;
import family.zambrana.starbound.hubcore.gui.SelectServerGui;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class HubCore extends MiniPlugin {

    private static final Set<UUID> HIDDEN_PLAYERS = new HashSet<>();

    public HubCore() {
        super("Hub Core");
        Bukkit.getPluginCommand("servers").setExecutor(new ServersCmd());

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(Starbound.getInstance(), "velocity:main");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Teleport to world spawn
        Location spawn = player.getWorld().getSpawnLocation();
        player.teleport(spawn);

        // Wipe inventory
        player.getInventory().clear();

        // Create and give hub items
        player.getInventory().setItem(0, createItem(Material.COMPASS, "§bSelect a Server"));
        player.getInventory().setItem(4, createItem(Material.CLOCK, "§eSelect a Hub"));
        player.getInventory().setItem(3, createSkull(player.getName(), "§aMy Profile"));

        boolean hidden = HIDDEN_PLAYERS.contains(player.getUniqueId());
        Material toggleMat = Material.REDSTONE_TORCH;
        String toggleName = hidden ? "§cPlayers: HIDDEN" : "§aPlayers: VISIBLE";
        player.getInventory().setItem(8, createItem(toggleMat, toggleName));

        player.updateInventory();

        // Hide other players if toggled
        updateVisibility(player, !hidden);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("§9Select a Server")) {
            event.setCancelled(true);
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.AIR || !clicked.hasItemMeta()) return;

            String displayName = clicked.getItemMeta().getDisplayName().replace("§b", "");
            Player player = (Player) event.getWhoClicked();
            String target = SelectServerGui.getTargetServer(displayName);
            if (target != null) {
                player.sendMessage("§7Connecting you to §b" + displayName + "§7...");
                player.sendPluginMessage(
                        Bukkit.getPluginManager().getPlugin("Starbound"),
                        "velocity:main",
                        SelectServerGui.constructVelocityForwardingData(player.getName(), target)
                );
                player.closeInventory();
            } else {
                player.sendMessage("§cFailed to connect. Server not found.");
            }
        }
    }

    // Helper: create custom item
    private ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    // Helper: create player skull item
    private ItemStack createSkull(String ownerName, String name) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setDisplayName(name);
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(ownerName));
        skull.setItemMeta(meta);
        return skull;
    }

    // Toggle player visibility
    public static void updateVisibility(Player player, boolean visible) {
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other.equals(player)) continue;
            if (visible) {
                player.showPlayer(Bukkit.getPluginManager().getPlugin("Starbound"), other);
            } else {
                player.hidePlayer(Bukkit.getPluginManager().getPlugin("Starbound"), other);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta()) return;

        String name = item.getItemMeta().getDisplayName();
        if (name == null) return;

        if (name.contains("Select a Server")) {
            player.openInventory(SelectServerGui.get());
        } else if (name.contains("Players:")) {
            HubCore.toggleVisibility(player);
        } else if (name.contains("My Profile")) {
            player.sendMessage("§7Opening your profile...");
            // TODO: Open profile GUI here
        }
    }

    // Use this in interact handler to toggle visibility
    public static void toggleVisibility(Player player) {
        UUID uuid = player.getUniqueId();
        boolean currentlyHidden = HIDDEN_PLAYERS.contains(uuid);
        if (currentlyHidden) {
            HIDDEN_PLAYERS.remove(uuid);
            player.sendMessage("§aYou can now see other players.");
        } else {
            HIDDEN_PLAYERS.add(uuid);
            player.sendMessage("§cYou can no longer see other players.");
        }

        updateVisibility(player, currentlyHidden);
        player.getInventory().setItem(8, createStaticItem(Material.REDSTONE_TORCH,
                currentlyHidden ? "§aPlayers: VISIBLE" : "§cPlayers: HIDDEN"));
        player.updateInventory();
    }

    private static ItemStack createStaticItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}