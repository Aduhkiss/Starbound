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

        if (args.length != 1) {
            player.sendMessage("§cUsage: /home <name>");
            return true;
        }

        String name = args[0].toLowerCase();

        try (Connection conn = DatabaseHolder.get().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM homes WHERE uuid = ? AND name = ?");
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setString(2, name);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                player.sendMessage("§cNo home found with name: " + name);
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
            player.sendMessage("§aTeleported to home '" + name + "'.");
        } catch (Exception e) {
            player.sendMessage("§cFailed to teleport home.");
            e.printStackTrace();
        }

        return true;
    }
}
