package family.zambrana.starbound.nickname;

import family.zambrana.starbound.clientmanager.CoreClient;
import family.zambrana.starbound.util.GUIBuilder;
import family.zambrana.starbound.util.Rank;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NickMainGUI {

    private final Player player;
    private final CoreClient client;

    public NickMainGUI(Player player, CoreClient client) {
        this.player = player;
        this.client = client;
    }

    public void open() {
        GUIBuilder gui = new GUIBuilder("Choose a Nickname Rank", 9);

        gui.setItem(1, createRankItem(Rank.PLAYER), () ->
                new NickRankGUI(player, client, Rank.PLAYER).open());

        gui.setItem(2, createRankItem(Rank.VIP), () ->
                new NickRankGUI(player, client, Rank.VIP).open());

        gui.setItem(3, createRankItem(Rank.VIP_PLUS), () ->
                new NickRankGUI(player, client, Rank.VIP_PLUS).open());

        gui.setItem(4, createRankItem(Rank.MVP), () ->
                new NickRankGUI(player, client, Rank.MVP).open());

        gui.setItem(5, createRankItem(Rank.MVP_PLUS), () ->
                new NickRankGUI(player, client, Rank.MVP_PLUS).open());

        gui.open(player);
    }

    private ItemStack createRankItem(Rank rank) {
        return GUIBuilder.namedItem(Material.NAME_TAG, rank.getPrefix() + rank.getName());
    }
}
