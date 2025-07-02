package family.zambrana.starbound;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class MiniPlugin implements Listener {

    private String name;

    public MiniPlugin(String name) {
        this.name = name;
        Bukkit.getLogger().info(name + "> Started.");
        Bukkit.getPluginManager().registerEvents(this, Starter.getInstance());
    }
    public String getName() {
        return name;
    }
}
