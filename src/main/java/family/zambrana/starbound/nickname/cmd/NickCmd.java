package family.zambrana.starbound.nickname.cmd;

import family.zambrana.starbound.clientmanager.CoreClient;
import family.zambrana.starbound.clientmanager.CoreClientManager;
import family.zambrana.starbound.nickname.NickManagerRegistry;
import family.zambrana.starbound.util.EZ;
import family.zambrana.starbound.util.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static family.zambrana.starbound.database.DatabaseHolder.get;

public class NickCmd implements CommandExecutor {

    private final CoreClientManager clientManager;

    public NickCmd() {
        this.clientManager = EZ.clientManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be run by a player.");
            return true;
        }

        CoreClient self = clientManager.getClient(player);

        if (!self.has(Rank.MVP_PLUS_PLUS)) {
            player.sendMessage("§cYou are not allowed to use this command.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
            self.resetNick();
            player.sendMessage("§8[§d✦§8] §7Your disguise has been §areset§7.");
            return true;
        }

        if (args.length == 1) {
            String requestedName = args[0];
            String skinName = requestedName;
            String displayName = requestedName;
            Rank rank = Rank.PLAYER;

            // Attempt to fetch extra data from database if they exist
            try (Connection conn = get().getConnection()) {
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM players WHERE LOWER(name) = LOWER(?)");
                stmt.setString(1, requestedName);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    if (rs.getString("display_name") != null) displayName = rs.getString("display_name");
                    if (rs.getString("skin_name") != null) skinName = rs.getString("skin_name");
                    if (rs.getString("rank") != null) rank = Rank.safeValueOf(rs.getString("rank"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage("§c[✦] Failed to lookup player info. Defaulting to raw Mojang data.");
            }

            // Update local state
            self.updateNickname(displayName);
            self.updateSkin(skinName);
            self.setFakePrefix(rank.getPrefix());

            // Apply spoofed name & skin
            NickManagerRegistry.get().applyNickname(player, displayName, skinName);

            player.sendMessage("§8[§d✦§8] §7You are now disguised as §r" + rank.getPrefix() + displayName + "§7.");
            return true;
        }

        player.sendMessage("§8[§d✦§8] §7Usage: §f/disguise §7<player> §8| §f/disguise reset");
        return true;
    }
}
