package family.zambrana.starbound.whitelist.cmd;

import family.zambrana.starbound.clientmanager.CoreClientManager;
import family.zambrana.starbound.util.Rank;
import family.zambrana.starbound.whitelist.Whitelist;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.UUID;

public class WhitelistCmd implements CommandExecutor {
    private final Whitelist whitelist;
    private CoreClientManager clientManager;

    public WhitelistCmd(Whitelist whitelist, CoreClientManager clientManager) {
        this.whitelist = whitelist;
        this.clientManager = clientManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player caller = (Player) sender;
            if(!clientManager.getClient(caller).has(Rank.ADMIN)) {
                caller.sendMessage("§cYou are not allowed to use this.");
                return false;
            }
        }

        if (args.length < 1) {
            sender.sendMessage("§7Usage: /whitelist <add|remove|list> [player]");
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "add":
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /whitelist add <player>");
                    return true;
                }

                OfflinePlayer toAdd = Bukkit.getOfflinePlayer(args[1]);
                if (toAdd.getName() == null || toAdd.getUniqueId() == null) {
                    sender.sendMessage("§cCould not find that player.");
                    return true;
                }

                whitelist.addPlayer(toAdd.getPlayer() != null ? toAdd.getPlayer() : toAdd, sender.getName());
                sender.sendMessage("§aWhitelisted " + toAdd.getName() + ".");
                break;

            case "remove":
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /whitelist remove <player>");
                    return true;
                }

                OfflinePlayer toRemove = Bukkit.getOfflinePlayer(args[1]);
                UUID uuidToRemove = toRemove.getUniqueId();
                whitelist.removePlayer(uuidToRemove);
                sender.sendMessage("§cRemoved " + toRemove.getName() + " from the whitelist.");
                break;

            case "list":
                sender.sendMessage("§7Whitelisted players:");
                for (UUID uuid : whitelist.getAll()) {
                    OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
                    sender.sendMessage("§8- §f" + (p.getName() != null ? p.getName() : uuid.toString()));
                }
                break;

            default:
                sender.sendMessage("§7Usage: /whitelist <add|remove|list> [player]");
        }

        return true;
    }
}
