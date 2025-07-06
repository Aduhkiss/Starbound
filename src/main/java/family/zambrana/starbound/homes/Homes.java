package family.zambrana.starbound.homes;

import family.zambrana.starbound.MiniPlugin;
import family.zambrana.starbound.Starbound;
import family.zambrana.starbound.homes.cmd.DelHomeCmd;
import family.zambrana.starbound.homes.cmd.HomeCmd;
import family.zambrana.starbound.homes.cmd.ListHomesCmd;
import family.zambrana.starbound.homes.cmd.SetHomeCmd;

public class Homes extends MiniPlugin {
    public Homes() {
        super("Homes");

        Starbound.getInstance().getCommand("sethome").setExecutor(new SetHomeCmd());
        Starbound.getInstance().getCommand("home").setExecutor(new HomeCmd());
        Starbound.getInstance().getCommand("homes").setExecutor(new ListHomesCmd());
        Starbound.getInstance().getCommand("delhome").setExecutor(new DelHomeCmd());
    }
}
