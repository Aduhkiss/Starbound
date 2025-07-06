package family.zambrana.starbound.nickname;

import family.zambrana.starbound.MiniPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GUIHandler extends MiniPlugin {

    private static final Map<UUID, Map<Integer, Runnable>> handlers = new HashMap<>();

    public GUIHandler() {
        super("Nickname GUI Handler");
    }

    public static void register(Player player, Map<Integer, Runnable> slotHandlers) {
        handlers.put(player.getUniqueId(), slotHandlers);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Map<Integer, Runnable> slotActions = handlers.get(player.getUniqueId());
        if (slotActions == null) return;

        int slot = event.getRawSlot();
        if (slotActions.containsKey(slot)) {
            event.setCancelled(true);
            player.closeInventory();
            slotActions.get(slot).run();
        }
    }
}
