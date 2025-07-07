package family.zambrana.starbound.punishment;

import family.zambrana.starbound.MiniPlugin;

import java.util.HashMap;
import java.util.Map;

public class Punish extends MiniPlugin {

    public Punish() {
        super("Punish");
    }

    private final Map<String, Punishment> activePunishments = new HashMap<>();

    public void addPunishment(Punishment p) {
        activePunishments.put(p.getTarget().toLowerCase(), p);
    }

    public void removePunishment(String target) {
        activePunishments.remove(target.toLowerCase());
    }

    public Punishment getActivePunishment(String target, Punishment.Type type) {
        Punishment p = activePunishments.get(target.toLowerCase());
        return p != null && p.getType() == type && !p.isExpired() ? p : null;
    }

    public boolean isBanned(String player) {
        return getActivePunishment(player, Punishment.Type.BAN) != null;
    }

    public boolean isMuted(String player) {
        return getActivePunishment(player, Punishment.Type.MUTE) != null;
    }

//    @EventHandler
//    public void onPlayerLogin(PlayerLoginEvent event) {
//        Player player = event.getPlayer();
//        if (!isWhitelisted(player.getUniqueId())) {
//            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST,
//                    "§8[§d✦ Atticus' Lovely World §8]§r\n\n" +
//                            "§c§lACCESS DENIED\n" +
//                            "§7You are not cleared for interstellar travel.\n" +
//                            "§7This server requires whitelist authorization.\n\n" +
//                            "§7If you believe this is an error,\n" +
//                            "§7please contact an §badmin §7or apply on our Discord.\n\n" +
//                            "§8> §oLaunch aborted.");
//            Bukkit.getLogger().info("[Whitelist]: " + player.getName() + " tried to join, but is not whitelisted.");
//        }
//    }
}
