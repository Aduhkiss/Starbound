package family.zambrana.starbound.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cYou are not allowed to do this.");
            return false;
        }
        Player caller = (Player) sender;

        boolean flying = caller.isFlying();
        caller.setFlying(!flying);
        caller.sendMessage(flying ? "§cYou are no longer flying." : "§aYou are now flying.");

        return true;
    }
}
