package family.zambrana.starbound.homes.cmd;

import family.zambrana.starbound.database.DatabaseHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HomeCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        try (Connection conn = DatabaseHolder.get().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM homes WHERE uuid = ?");
            stmt.setString(1, player.getUniqueId().toString());
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                player.sendMessage("§cYou don't have a home set. Use /sethome first.");
                return true;
            }

            World world = Bukkit.getWorld(rs.getString("world"));
            if (world == null) {
                player.sendMessage("§cWorld not found.");
                return true;
            }

            Location loc = new Location(
                    world,
                    rs.getDouble("x"),
                    rs.getDouble("y"),
                    rs.getDouble("z"),
                    rs.getFloat("yaw"),
                    rs.getFloat("pitch")
            );

            player.teleport(loc);
            player.sendMessage("§aTeleported to home.");
        } catch (Exception e) {
            player.sendMessage("§cFailed to teleport home.");
            e.printStackTrace();
        }

        return true;
    }
}
