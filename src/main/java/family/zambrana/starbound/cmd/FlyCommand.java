package family.zambrana.starbound.cmd;

import family.zambrana.starbound.clientmanager.CoreClientManager;
import family.zambrana.starbound.util.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {

    private CoreClientManager clientManager;

    public FlyCommand(CoreClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if(!clientManager.getClient(player).has(Rank.MVP)) {
            player.sendMessage("§cYou are not allowed to use this.");
            return false;
        }

        boolean isFlying = player.isFlying();
        if (isFlying) {
            player.setFlying(false);
            player.setAllowFlight(false);
            player.sendMessage("§cYou are no longer flying.");
        } else {
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage("§aYou are now flying.");
        }

        return true;
    }
}
