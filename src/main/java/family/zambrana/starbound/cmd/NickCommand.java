package family.zambrana.starbound.cmd;

import family.zambrana.starbound.clientmanager.CoreClient;
import family.zambrana.starbound.clientmanager.CoreClientManager;
import family.zambrana.starbound.nickname.NickBookGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NickCommand implements CommandExecutor {

    private final CoreClientManager clientManager;

    public NickCommand(CoreClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player pl)) return false;

        CoreClient client = clientManager.getClient(pl);

        if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
            client.resetNick();
            pl.sendMessage("§cYour nickname and skin have been reset.");
            return true;
        }

        // Launch Nick GUI sequence
        new NickBookGUI(pl, client).openIntro();

        return true;
    }
}
