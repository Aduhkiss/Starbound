package family.zambrana.starbound.homes.cmd;

import family.zambrana.starbound.database.DatabaseHolder;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SetHomeCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        Location loc = player.getLocation();

        try (Connection conn = DatabaseHolder.get().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "REPLACE INTO homes (uuid, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setString(2, loc.getWorld().getName());
            stmt.setDouble(3, loc.getX());
            stmt.setDouble(4, loc.getY());
            stmt.setDouble(5, loc.getZ());
            stmt.setFloat(6, loc.getYaw());
            stmt.setFloat(7, loc.getPitch());
            stmt.executeUpdate();

            player.sendMessage("§aHome set successfully!");
        } catch (Exception e) {
            player.sendMessage("§cFailed to set home.");
            e.printStackTrace();
        }

        return true;
    }
}
