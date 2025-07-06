package family.zambrana.starbound.teleporter.cmd;

import family.zambrana.starbound.clientmanager.CoreClientManager;
import family.zambrana.starbound.util.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCmd implements CommandExecutor {

    private final CoreClientManager manager;

    public TeleportCmd(CoreClientManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) && args.length <= 2) {
            sender.sendMessage("§cOnly players can use this command like that.");
            return true;
        }

        Player senderPlayer = sender instanceof Player ? (Player) sender : null;
        if (senderPlayer != null && !manager.getClient(senderPlayer).has(Rank.MOD)) {
            sender.sendMessage("§cYou are not allowed to use this.");
            return true;
        }

        // /tp <target>
        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage("§cPlayer not found.");
                return true;
            }

            senderPlayer.teleport(target.getLocation());
            senderPlayer.sendMessage("§aTeleported to §e" + target.getName() + "§a.");
            return true;
        }

        // /tp <from> <to>
        if (args.length == 2) {
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

        // /tp x y z
        if (args.length == 3) {
            if (senderPlayer == null) {
                sender.sendMessage("§cConsole must specify a target player.");
                return true;
            }

            Location loc = parseCoordinates(senderPlayer.getLocation(), args[0], args[1], args[2]);
            if (loc == null) {
                sender.sendMessage("§cInvalid coordinates.");
                return true;
            }

            senderPlayer.teleport(loc);
            senderPlayer.sendMessage("§aTeleported to coordinates.");
            return true;
        }

        // /tp <player> x y z
        if (args.length == 4) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage("§cPlayer not found.");
                return true;
            }

            Location loc = parseCoordinates(target.getLocation(), args[1], args[2], args[3]);
            if (loc == null) {
                sender.sendMessage("§cInvalid coordinates.");
                return true;
            }

            target.teleport(loc);
            sender.sendMessage("§aTeleported §e" + target.getName() + " §ato coordinates.");
            return true;
        }

        sender.sendMessage("§cUsage: /tp <target>, /tp <from> <to>, /tp [<player>] <x> <y> <z>");
        return true;
    }

    private Location parseCoordinates(Location base, String sx, String sy, String sz) {
        try {
            double x = parseCoordinate(base.getX(), sx);
            double y = parseCoordinate(base.getY(), sy);
            double z = parseCoordinate(base.getZ(), sz);
            return new Location(base.getWorld(), x, y, z);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private double parseCoordinate(double base, String input) throws NumberFormatException {
        if (input.startsWith("~")) {
            if (input.length() == 1) return base;
            return base + Double.parseDouble(input.substring(1));
        }
        return Double.parseDouble(input);
    }
}
