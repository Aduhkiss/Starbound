package family.zambrana.starbound.clientmanager.cmd;

import family.zambrana.starbound.clientmanager.CoreClientManager;
import family.zambrana.starbound.util.Rank;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GameModeCmd implements CommandExecutor {

    private CoreClientManager clientManager;
    public GameModeCmd(CoreClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player caller = (Player) sender;
        if(!clientManager.getClient(caller).has(Rank.ADMIN)) {
            caller.sendMessage("§cYou are not allowed to use this.");
            return false;
        }
        if (args.length == 0) {
            // Toggle self
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cConsole must specify a player.");
                return true;
            }

            Player player = (Player) sender;

            toggleGameMode(player);
            return true;
        }

        // Check if first argument is a number or string gamemode
        GameMode targetMode = parseGameMode(args[0]);
        if (targetMode != null) {
            // If there's a second argument, apply to another player
            if (args.length >= 2) {
                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null) {
                    sender.sendMessage("§cPlayer not found: " + args[1]);
                    return true;
                }

                target.setGameMode(targetMode);
                sender.sendMessage("§aSet " + target.getName() + "'s gamemode to §e" + targetMode.name());
                target.sendMessage("§aYour gamemode has been set to §e" + targetMode.name());
                return true;
            } else {
                // Apply to self
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§cConsole must specify a player.");
                    return true;
                }

                Player player = (Player) sender;
                player.setGameMode(targetMode);
                player.sendMessage("§aYour gamemode has been set to §e" + targetMode.name());
                return true;
            }
        }

        // Not a valid gamemode – maybe it's a player name to toggle
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target != null) {
            toggleGameMode(target);
            sender.sendMessage("§aToggled " + target.getName() + "'s gamemode to §e" + target.getGameMode().name());
            target.sendMessage("§aYour gamemode has been toggled to §e" + target.getGameMode().name());
            return true;
        }

        sender.sendMessage("§cInvalid input. Use /gm [mode|player] [optional player]");
        return true;
    }

    private void toggleGameMode(Player player) {
        GameMode current = player.getGameMode();
        GameMode newMode = current == GameMode.CREATIVE ? GameMode.SURVIVAL : GameMode.CREATIVE;
        player.setGameMode(newMode);
        player.sendMessage("§aToggled gamemode to §e" + newMode.name());
    }

    private GameMode parseGameMode(String input) {
        switch (input.toLowerCase()) {
            case "0":
            case "survival":
                return GameMode.SURVIVAL;
            case "1":
            case "creative":
                return GameMode.CREATIVE;
            case "2":
            case "adventure":
                return GameMode.ADVENTURE;
            case "3":
            case "spectator":
                return GameMode.SPECTATOR;
            default:
                return null;
        }
    }
}
