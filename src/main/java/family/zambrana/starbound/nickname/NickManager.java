package family.zambrana.starbound.nickname;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class NickManager {

    private final Plugin plugin;
    private final Map<UUID, String[]> originalSkins = new HashMap<>();

    public NickManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void applyNickname(Player player, String nickname, String skinUsername) {
        String[] textures = fetchSkinData(skinUsername);
        if (textures == null) {
            player.sendMessage("§cCould not fetch skin data.");
            return;
        }

        GameProfile profile = new GameProfile(player.getUniqueId(), nickname);
        profile.getProperties().put("textures", new Property("textures", textures[0], textures[1]));

        // Save original skin
        if (!originalSkins.containsKey(player.getUniqueId())) {
            String[] original = fetchSkinData(player.getName());
            if (original != null) {
                originalSkins.put(player.getUniqueId(), original);
            }
        }

        sendNickPacket(player, profile);
    }

    public void resetNickname(Player player) {
        String[] original = originalSkins.get(player.getUniqueId());
        if (original != null) {
            GameProfile profile = new GameProfile(player.getUniqueId(), player.getName());
            profile.getProperties().put("textures", new Property("textures", original[0], original[1]));
            sendNickPacket(player, profile);
        }
    }

    private void sendNickPacket(Player player, GameProfile profile) {
        WrappedGameProfile wrapped = WrappedGameProfile.fromHandle(profile);

        PlayerInfoData data = new PlayerInfoData(
                wrapped,
                0,
                EnumWrappers.NativeGameMode.fromBukkit(player.getGameMode()),
                WrappedChatComponent.fromText(player.getDisplayName())
        );

        PacketContainer remove = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        remove.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        remove.getPlayerInfoDataLists().write(0, Collections.singletonList(data));

        PacketContainer add = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        add.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        add.getPlayerInfoDataLists().write(0, Collections.singletonList(data));

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            try {
                for (Player viewer : Bukkit.getOnlinePlayers()) {
                    ProtocolLibrary.getProtocolManager().sendServerPacket(viewer, remove);
                    ProtocolLibrary.getProtocolManager().sendServerPacket(viewer, add);
                    viewer.hidePlayer(plugin, player);
                    viewer.showPlayer(plugin, player);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 5L);
    }

    private String[] fetchSkinData(String name) {
        try {
            // Step 1: Get UUID
            HttpURLConnection uuidConn = (HttpURLConnection) new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openConnection();
            uuidConn.setRequestMethod("GET");
            JsonObject uuidJson = JsonParser.parseReader(new InputStreamReader(uuidConn.getInputStream())).getAsJsonObject();
            String uuid = uuidJson.get("id").getAsString();

            // Step 2: Get Profile (skin)
            HttpURLConnection profileConn = (HttpURLConnection) new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false").openConnection();
            profileConn.setRequestMethod("GET");
            JsonObject profileJson = JsonParser.parseReader(new InputStreamReader(profileConn.getInputStream())).getAsJsonObject();
            JsonObject skinData = profileJson.getAsJsonArray("properties").get(0).getAsJsonObject();

            return new String[]{
                    skinData.get("value").getAsString(),
                    skinData.get("signature").getAsString()
            };
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void applyRandomNickname(Player player) {
        String[] randomNames = {"Firestar", "PixelNova", "ShadowByte", "WaffleKing", "Starlord"};
        String[] randomSkins = {"Dream", "GeorgeNotFound", "Notch", "Technoblade", "CaptainSparklez"};

        String nickname = randomNames[new Random().nextInt(randomNames.length)];
        String skin = randomSkins[new Random().nextInt(randomSkins.length)];

        applyNickname(player, nickname, skin);
    }
}