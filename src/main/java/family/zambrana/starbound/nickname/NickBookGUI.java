package family.zambrana.starbound.nickname;

import family.zambrana.starbound.clientmanager.CoreClient;
import family.zambrana.starbound.util.BookUtil;
import family.zambrana.starbound.util.Rank;
import family.zambrana.starbound.util.SignGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.Arrays;
import java.util.List;

public class NickBookGUI {

    private final Player player;
    private final CoreClient client;

    public NickBookGUI(Player player, CoreClient client) {
        this.player = player;
        this.client = client;
    }

    public void openIntro() {
        BookUtil.openBook(player, "Nicknames allow you to play with a different username to not get recognized.\n\n" +
                "All rules still apply.\n\n" +
                "➤ I understand, set up my nickname", List.of(
                new BookUtil.ClickablePage("➤ I understand, set up my nickname", () -> openRankSelection())
        ));
    }

    public void openRankSelection() {
        BookUtil.openBook(player, "Let's get you set up with your nickname!\n\nChoose your RANK:\n", Arrays.asList(
                new BookUtil.ClickablePage("➤ DEFAULT", () -> openSkinSelection(Rank.PLAYER)),
                new BookUtil.ClickablePage("➤ VIP", () -> openSkinSelection(Rank.VIP)),
                new BookUtil.ClickablePage("➤ VIP+", () -> openSkinSelection(Rank.VIP_PLUS)),
                new BookUtil.ClickablePage("➤ MVP", () -> openSkinSelection(Rank.MVP)),
                new BookUtil.ClickablePage("➤ MVP+", () -> openSkinSelection(Rank.MVP_PLUS))
        ));
    }

    public void openSkinSelection(Rank rank) {
        BookUtil.openBook(player, "Which SKIN would you like to use?\n", Arrays.asList(
                new BookUtil.ClickablePage("➤ Enter account name", () -> SignGUI.open(player, "Skin Username", skinName -> {
                    // Placeholder: Replace with real skin logic
                    Bukkit.getLogger().info(player.getName() + " set skin to: " + skinName);
                    openNameSelection(rank, skinName);
                })),
                new BookUtil.ClickablePage("➤ Random skin", () -> openNameSelection(rank, "RANDOM")),
                new BookUtil.ClickablePage("➤ My normal skin", () -> openNameSelection(rank, "NORMAL"))
        ));
    }

    public void openNameSelection(Rank rank, String skin) {
        BookUtil.openBook(player, "Alright, now pick a nickname:\n", Arrays.asList(
                new BookUtil.ClickablePage("➤ Type a name", () -> SignGUI.open(player, "Nickname", name -> {
                    applyNick(rank, skin, name);
                })),
                new BookUtil.ClickablePage("➤ Use a random name", () -> applyNick(rank, skin, "RandomNick123"))
        ));
    }

    public void applyNick(Rank rank, String skin, String name) {
        // Apply NickAPI changes
        NickAPI.setNick(player, name);
        NickAPI.setSkin(player, skin);
        NickAPI.setUniqueId(player, name);
        NickAPI.setProfileName(player, name);
        NickAPI.refreshPlayer(player);

        // Update display name
        player.setDisplayName(rank.getPrefix() + name);
        player.sendMessage("§aYour nickname has been set to: " + rank.getPrefix() + name);

        // Save to CoreClient
        client.updateNickname(name);
        client.updateSkin(skin);
    }
}
