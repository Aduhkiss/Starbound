package family.zambrana.starbound.fishing;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;

public class LootManager {

    private final List<LootEntry> lootTable = new ArrayList<>();

    public LootManager() {
        loadLoot();
    }

    private void loadLoot() {
        File file = new File("plugins/Starbound/fishing_loot.yml");
        if (!file.exists()) {
            Bukkit.getLogger().warning("[Fishing] Loot config not found: " + file.getAbsolutePath());
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.isConfigurationSection("custom_loot")) {
            Bukkit.getLogger().warning("[Fishing] No 'custom_loot' section found in loot file.");
            return;
        }

        for (String key : config.getConfigurationSection("custom_loot").getKeys(false)) {
            String path = "custom_loot." + key;

            String materialName = config.getString(path + ".material", "DIRT");
            String name = config.getString(path + ".name", null);
            List<String> lore = config.getStringList(path + ".lore");
            double chance = config.getDouble(path + ".chance", 0.0);

            Material material = Material.matchMaterial(materialName);
            if (material == null) {
                Bukkit.getLogger().warning("[Fishing] Invalid material for " + key + ": " + materialName);
                continue;
            }

            // Handle enchantments
            Map<Enchantment, Integer> enchantments = new HashMap<>();
            if (config.isConfigurationSection(path + ".enchantments")) {
                ConfigurationSection enchSection = config.getConfigurationSection(path + ".enchantments");
                for (String enchKey : enchSection.getKeys(false)) {
                    Enchantment enchantment = Enchantment.getByName(enchKey.toUpperCase());
                    if (enchantment == null) {
                        Bukkit.getLogger().warning("[Fishing] Invalid enchantment '" + enchKey + "' for item '" + key + "'");
                        continue;
                    }
                    int level = enchSection.getInt(enchKey);
                    enchantments.put(enchantment, level);
                }
            }

            lootTable.add(new LootEntry(material, name, lore, chance, enchantments));
        }

        Bukkit.getLogger().info("[Fishing] Loaded " + lootTable.size() + " custom loot entries.");
    }

    public ItemStack rollLoot() {
        for (LootEntry entry : lootTable) {
            if (Math.random() <= entry.chance) {
                return entry.toItemStack();
            }
        }
        return null;
    }

    private static class LootEntry {
        Material material;
        String name;
        List<String> lore;
        double chance;
        Map<Enchantment, Integer> enchantments;

        public LootEntry(Material material, String name, List<String> lore, double chance, Map<Enchantment, Integer> enchantments) {
            this.material = material;
            this.name = name;
            this.lore = lore;
            this.chance = chance;
            this.enchantments = enchantments;
        }

        public ItemStack toItemStack() {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();

            if (name != null) meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            if (lore != null && !lore.isEmpty()) {
                List<String> coloredLore = new ArrayList<>();
                for (String line : lore) {
                    coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));
                }
                meta.setLore(coloredLore);
            }

            item.setItemMeta(meta);

            if (enchantments != null) {
                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    item.addUnsafeEnchantment(entry.getKey(), entry.getValue());
                }
            }

            return item;
        }
    }

}
