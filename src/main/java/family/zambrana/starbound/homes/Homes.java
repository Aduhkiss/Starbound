package family.zambrana.starbound.homes;

import family.zambrana.starbound.MiniPlugin;
import family.zambrana.starbound.Starter;
import family.zambrana.starbound.homes.cmd.HomeCmd;
import family.zambrana.starbound.homes.cmd.SetHomeCmd;

public class Homes extends MiniPlugin {
    public Homes() {
        super("Homes");

        Starter.getInstance().getCommand("sethome").setExecutor(new SetHomeCmd());
        Starter.getInstance().getCommand("home").setExecutor(new HomeCmd());
    }
}
