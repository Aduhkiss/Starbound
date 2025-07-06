package family.zambrana.starbound.nickname;

import family.zambrana.starbound.clientmanager.CoreClient;
import family.zambrana.starbound.util.GUIBuilder;
import family.zambrana.starbound.util.Rank;
import family.zambrana.starbound.util.SignGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Random;

public class NickRankGUI {

    private final Player player;
    private final CoreClient client;
    private final Rank selectedRank;
    private final Random random = new Random();

    public NickRankGUI(Player player, CoreClient client, Rank selectedRank) {
        this.player = player;
        this.client = client;
        this.selectedRank = selectedRank;
    }

    public void open() {
        GUIBuilder gui = new GUIBuilder("Skin + Nickname", 9);

        gui.setItem(2, GUIBuilder.namedItem(Material.PLAYER_HEAD, "§aCustom Skin"), () ->
                SignGUI.open(player, "Skin Username", skin ->
                        SignGUI.open(player, "Nickname", name -> applyNick(skin, name))));

        gui.setItem(4, GUIBuilder.namedItem(Material.NETHER_STAR, "§bRandom Skin + Name"), () ->
                applyNick(randomSkin(), randomName()));

        gui.setItem(6, GUIBuilder.namedItem(Material.BARRIER, "§cUse My Normal Skin"), () ->
                SignGUI.open(player, "Nickname", name -> applyNick("NORMAL", name)));

        gui.open(player);
    }

    private void applyNick(String skin, String name) {
        client.updateSkin(skin);
        client.updateNickname(name);
        client.setFakePrefix(selectedRank.getPrefix());

        player.setDisplayName(selectedRank.getPrefix() + name);
        player.sendMessage("§aYour nickname is now: " + selectedRank.getPrefix() + name);
    }

    private String randomSkin() {
        String[] skins = {"Dream", "Notch", "CaptainSparklez", "Herobrine", "Technoblade"};
        return skins[random.nextInt(skins.length)];
    }

    private String randomName() {
        String[] names = {
                "ShadowFox", "TacoWizard", "ByteRider", "FlickerFlame",
                "NovaCat", "PickleMan", "Voidling", "Starshard"
        };
        return names[random.nextInt(names.length)];
    }
}
