package family.zambrana.starbound.nickname.cmd;

import family.zambrana.starbound.clientmanager.CoreClientManager;
import family.zambrana.starbound.nickname.NickMainGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NickCmd implements CommandExecutor {

    private final CoreClientManager clientManager;

    public NickCmd(CoreClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
            clientManager.getClient(player).resetNick();
            return true;
        }

        Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("Starbound"), () ->
                new NickMainGUI(player, clientManager.getClient(player)).open()
        );

        return true;
    }
}
