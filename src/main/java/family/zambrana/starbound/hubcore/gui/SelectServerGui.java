package family.zambrana.starbound.hubcore.gui;

import family.zambrana.starbound.database.DatabaseHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SelectServerGui {

    public static Inventory get() {
        List<ServerEntry> servers = loadServersFromDatabase();
        Inventory gui = Bukkit.createInventory(null, 27, "§9Select a Server");

        for (int i = 0; i < servers.size() && i < 27; i++) {
            ServerEntry entry = servers.get(i);
            ItemStack item = new ItemStack(entry.material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§b" + entry.displayName);
            List<String> lore = new ArrayList<>();
            lore.add("§7Click to connect");
            lore.add("§8Target: §f" + entry.serverTarget);
            meta.setLore(lore);
            item.setItemMeta(meta);
            gui.setItem(i, item);
        }

        return gui;
    }

    private static List<ServerEntry> loadServersFromDatabase() {
        List<ServerEntry> list = new ArrayList<>();
        try (Connection conn = DatabaseHolder.get().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT display_name, server_target, material FROM servers");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String displayName = rs.getString("display_name");
                String serverTarget = rs.getString("server_target");
                String materialStr = rs.getString("material");
                Material material = Material.matchMaterial(materialStr.toUpperCase());
                if (material == null) {
                    Bukkit.getLogger().warning("[HubCore] Invalid material: " + materialStr);
                    continue;
                }
                list.add(new ServerEntry(displayName, serverTarget, material));
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe("[HubCore] Failed to load servers: " + e.getMessage());
        }
        return list;
    }

    private static class ServerEntry {
        final String displayName;
        final String serverTarget;
        final Material material;

        ServerEntry(String displayName, String serverTarget, Material material) {
            this.displayName = displayName;
            this.serverTarget = serverTarget;
            this.material = material;
        }
    }

    public static String getTargetServer(String displayName) {
        try (Connection conn = DatabaseHolder.get().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT server_target FROM servers WHERE display_name = ?");
            stmt.setString(1, displayName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("server_target");
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe("[HubCore] Failed to fetch target server: " + e.getMessage());
        }
        return null;
    }

    public static byte[] constructVelocityForwardingData(String playerName, String targetServer) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream data = new DataOutputStream(out);
            data.writeUTF("ConnectOther");
            data.writeUTF(playerName);
            data.writeUTF(targetServer);
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}