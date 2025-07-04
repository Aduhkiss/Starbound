package family.zambrana.starbound.homes.cmd;

import family.zambrana.starbound.database.DatabaseHolder;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SetHomeCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length != 1) {
            player.sendMessage("§cUsage: /sethome <name>");
            return true;
        }

        String name = args[0].toLowerCase();
        if (name.length() > 32) {
            player.sendMessage("§cHome name too long.");
            return true;
        }

        Location loc = player.getLocation();

        try (Connection conn = DatabaseHolder.get().getConnection()) {
            // Count how many homes the player has
            PreparedStatement countStmt = conn.prepareStatement("SELECT COUNT(*) FROM homes WHERE uuid = ?");
            countStmt.setString(1, player.getUniqueId().toString());
            ResultSet countResult = countStmt.executeQuery();
            countResult.next();
            int homeCount = countResult.getInt(1);

            // Only limit if it's a new home
            PreparedStatement checkStmt = conn.prepareStatement("SELECT 1 FROM homes WHERE uuid = ? AND name = ?");
            checkStmt.setString(1, player.getUniqueId().toString());
            checkStmt.setString(2, name);
            ResultSet exists = checkStmt.executeQuery();

            if (!exists.next() && homeCount >= 4) {
                player.sendMessage("§cYou already have 4 homes.");
                return true;
            }

            PreparedStatement stmt = conn.prepareStatement(
                    "REPLACE INTO homes (uuid, name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setString(2, name);
            stmt.setString(3, loc.getWorld().getName());
            stmt.setDouble(4, loc.getX());
            stmt.setDouble(5, loc.getY());
            stmt.setDouble(6, loc.getZ());
            stmt.setFloat(7, loc.getYaw());
            stmt.setFloat(8, loc.getPitch());
            stmt.executeUpdate();

            player.sendMessage("§aHome '" + name + "' set successfully!");
        } catch (Exception e) {
            player.sendMessage("§cFailed to set home.");
            e.printStackTrace();
        }

        return true;
    }
}
