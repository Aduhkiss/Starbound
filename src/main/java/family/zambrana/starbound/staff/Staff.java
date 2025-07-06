package family.zambrana.starbound.staff;

import family.zambrana.starbound.MiniPlugin;
import family.zambrana.starbound.Starter;
import family.zambrana.starbound.clientmanager.CoreClient;
import family.zambrana.starbound.clientmanager.CoreClientManager;
import family.zambrana.starbound.staff.cmd.StaffCCmd;
import family.zambrana.starbound.util.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Staff extends MiniPlugin {

    private CoreClientManager clientManager;
    public Staff(CoreClientManager clientManager) {
        super("Staff Service");
        this.clientManager = clientManager;

        Starter.getInstance().getCommand("staffchat").setExecutor(new StaffCCmd(clientManager, this));
    }

    /*
    Used to send generic messages to all staff members
     */
    public void send(String message) {
        for(Player pl : Bukkit.getOnlinePlayers()) {
            // check if the player is a staff member or above from the client manager service
            CoreClient client = clientManager.getClient(pl);
            // are they a staff member?
            if(client.has(Rank.HELPER)) {
                // alright they are, lets start sending them sensitive details lmao
                pl.sendMessage("§b[STAFF] §f" + message);
            }
        }
    }

    /*
    Very similar to the above, but is used to send messages, "from" a user account. Kinda like a "staff chat" in a way
     */
    public void send(Player caller, String message) {
        CoreClient client = clientManager.getClient(caller);
        String prefix = client.getRankPrefix(); // they're real prefix, not the /nick one lol
        send(prefix + "" + client.getPlayer().getName() + "§f: " + message); // again, make sure you are using the player's real name and not the disguise
        // this should correctly use the color codes from the rank system
    }
}
