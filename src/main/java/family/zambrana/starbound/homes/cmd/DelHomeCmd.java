package family.zambrana.starbound.homes.cmd;

import family.zambrana.starbound.database.DatabaseHolder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DelHomeCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length != 1) {
            player.sendMessage("§cUsage: /delhome <name>");
            return true;
        }

        String name = args[0].toLowerCase();

        try (Connection conn = DatabaseHolder.get().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM homes WHERE uuid = ? AND name = ?");
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setString(2, name);
            int affected = stmt.executeUpdate();

            if (affected == 0) {
                player.sendMessage("§cNo home found with the name '" + name + "'.");
            } else {
                player.sendMessage("§aHome '" + name + "' has been deleted.");
            }
        } catch (Exception e) {
            player.sendMessage("§cFailed to delete home.");
            e.printStackTrace();
        }

        return true;
    }
}
