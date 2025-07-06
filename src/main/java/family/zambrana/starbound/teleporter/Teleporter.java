package family.zambrana.starbound.teleporter;

import family.zambrana.starbound.MiniPlugin;
import family.zambrana.starbound.Starbound;
import family.zambrana.starbound.clientmanager.CoreClientManager;
import family.zambrana.starbound.teleporter.cmd.TeleportCmd;

public class Teleporter extends MiniPlugin {

    private CoreClientManager clientManager;
    public Teleporter(CoreClientManager clientManager) {
        super("Teleporter");
        this.clientManager = clientManager;

        Starbound.getInstance().getCommand("teleport").setExecutor(new TeleportCmd(clientManager));
    }
}
