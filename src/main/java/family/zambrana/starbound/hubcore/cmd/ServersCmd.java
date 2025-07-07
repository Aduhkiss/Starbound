package family.zambrana.starbound.hubcore.cmd;

import family.zambrana.starbound.hubcore.gui.SelectServerGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ServersCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        player.openInventory(SelectServerGui.get());
        return true;
    }
}