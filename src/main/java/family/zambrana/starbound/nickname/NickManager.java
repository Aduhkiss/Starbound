package family.zambrana.starbound.nickname;

import org.bukkit.entity.Player;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.*;

public class NickManager {

    private final Map<UUID, String[]> originalSkins = new HashMap<>();

    public void applyNickname(Player player, String nickname, String skinName) {
        if (!originalSkins.containsKey(player.getUniqueId())) {
            String originalNick = player.getName();
            String originalSkin = player.getName();
            originalSkins.put(player.getUniqueId(), new String[]{originalNick, originalSkin});
        }

        NickAPI.setNick(player, nickname);
        NickAPI.setSkin(player, skinName);
        NickAPI.setProfileName(player, nickname);
        NickAPI.setUniqueId(player, nickname);
        NickAPI.refreshPlayer(player);

    }

    public void resetNickname(Player player) {
        String[] original = originalSkins.get(player.getUniqueId());
        if (original != null) {
            NickAPI.setNick(player, original[0]);
            NickAPI.setSkin(player, original[1]);
            NickAPI.setUniqueId(player, original[0]);
            NickAPI.setProfileName(player, original[0]);
            NickAPI.refreshPlayer(player);
        } else {
            NickAPI.resetNick(player);
            NickAPI.resetSkin(player);
            NickAPI.resetUniqueId(player);
            NickAPI.resetProfileName(player);
            NickAPI.refreshPlayer(player);
        }
    }
}
