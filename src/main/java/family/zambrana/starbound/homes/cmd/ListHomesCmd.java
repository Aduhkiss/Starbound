package family.zambrana.starbound.homes.cmd;

import family.zambrana.starbound.database.DatabaseHolder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ListHomesCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        try (Connection conn = DatabaseHolder.get().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT name FROM homes WHERE uuid = ?");
            stmt.setString(1, player.getUniqueId().toString());
            ResultSet rs = stmt.executeQuery();

            StringBuilder homes = new StringBuilder("§aYour homes: ");
            boolean found = false;

            while (rs.next()) {
                homes.append("§f").append(rs.getString("name")).append(" §7| ");
                found = true;
            }

            if (!found) {
                player.sendMessage("§cYou don't have any homes.");
            } else {
                player.sendMessage(homes.substring(0, homes.length() - 3)); // remove last " | "
            }

        } catch (Exception e) {
            player.sendMessage("§cFailed to fetch homes.");
            e.printStackTrace();
        }

        return true;
    }
}
