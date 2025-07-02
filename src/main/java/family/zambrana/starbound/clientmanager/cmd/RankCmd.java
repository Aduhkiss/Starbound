package family.zambrana.starbound.clientmanager.cmd;

import family.zambrana.starbound.clientmanager.CoreClient;
import family.zambrana.starbound.clientmanager.CoreClientManager;
import family.zambrana.starbound.database.DatabaseHolder;
import family.zambrana.starbound.util.Rank;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;

public class RankCmd implements CommandExecutor {

    private final CoreClientManager clientManager;

    public RankCmd(CoreClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length != 2) {
            sender.sendMessage("§cUsage: /rank <player> <rank>");
            return true;
        }

        String targetName = args[0];
        String rankName = args[1].toUpperCase(Locale.ROOT);

        Rank newRank;
        try {
            newRank = Rank.valueOf(rankName);
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§cInvalid rank: " + rankName);
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);

        if (target == null || target.getUniqueId() == null) {
            sender.sendMessage("§cPlayer not found.");
            return true;
        }

        try (Connection conn = DatabaseHolder.get().getConnection()) {
            // check if player has joined before
            PreparedStatement check = conn.prepareStatement("SELECT uuid FROM players WHERE uuid = ?");
            check.setString(1, target.getUniqueId().toString());
            ResultSet rs = check.executeQuery();

            if (!rs.next()) {
                sender.sendMessage("§cThat player has never joined before.");
                return true;
            }

            // update rank in database
            PreparedStatement update = conn.prepareStatement("UPDATE players SET rank = ? WHERE uuid = ?");
            update.setString(1, newRank.name());
            update.setString(2, target.getUniqueId().toString());
            update.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage("§cAn error occurred while updating the rank.");
            return true;
        }

        // if online, update in memory
        Player online = target.getPlayer();
        if (online != null) {
            CoreClient client = clientManager.getClient(online);
            if (client != null) {
                client.setRank(newRank);
                sender.sendMessage("§aUpdated rank and live data for §e" + online.getName() + "§a to §b" + newRank.name());
            }
        } else {
            sender.sendMessage("§aUpdated rank in database for §e" + targetName + "§a to §b" + newRank.name());
        }

        return true;
    }
}
