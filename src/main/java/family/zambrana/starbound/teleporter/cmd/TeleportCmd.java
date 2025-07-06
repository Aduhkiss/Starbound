package family.zambrana.starbound.teleporter.cmd;

import family.zambrana.starbound.clientmanager.CoreClientManager;
import family.zambrana.starbound.util.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCmd implements CommandExecutor {

    private CoreClientManager manager;

    public TeleportCmd(CoreClientManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) && args.length == 1) {
            sender.sendMessage("§cOnly players can use this command like that.");
            return true;
        }

        Player player = (Player) sender;
        if(!manager.getClient(player).has(Rank.MOD)) {
            player.sendMessage("§cYou are not allowed to use this.");
            return false;
        }

        if (args.length == 1) {
            // /tp MiyaTastic
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage("§cPlayer not found.");
                return true;
            }

            player.teleport(target.getLocation());
            player.sendMessage("§aTeleported to §e" + target.getName() + "§a.");
            return true;
        }

        if (args.length == 2) {
            // /tp Notch MiyaTastic
            Player from = Bukkit.getPlayerExact(args[0]);
            Player to = Bukkit.getPlayerExact(args[1]);

            if (from == null || to == null) {
                sender.sendMessage("§cOne or both players not found.");
                return true;
            }

            from.teleport(to.getLocation());
            sender.sendMessage("§aTeleported §e" + from.getName() + " §ato §e" + to.getName() + "§a.");
            return true;
        }

        sender.sendMessage("§cUsage: /tp <target> OR /tp <player1> <player2>");
        return true;
    }
}
