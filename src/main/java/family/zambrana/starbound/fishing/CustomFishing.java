package family.zambrana.starbound.fishing;

import family.zambrana.starbound.MiniPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

public class CustomFishing extends MiniPlugin {

    private final LootManager lootManager;

    public CustomFishing() {
        super("Fishing Service");
        lootManager = new LootManager();
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        if (!(event.getCaught() instanceof Item)) return;

        Player player = event.getPlayer();
        Item caughtItem = (Item) event.getCaught();

        // Try to get custom loot
        ItemStack customLoot = lootManager.rollLoot();
        if (customLoot != null) {
            caughtItem.setItemStack(customLoot);
            player.sendMessage(ChatColor.AQUA + "You fished up something rare!");
        }
    }
}
