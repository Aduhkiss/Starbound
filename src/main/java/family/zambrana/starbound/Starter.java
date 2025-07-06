package family.zambrana.starbound;

import family.zambrana.starbound.chat.StarboundChat;
import family.zambrana.starbound.clientmanager.CoreClientManager;
import family.zambrana.starbound.cmd.FlyCommand;
import family.zambrana.starbound.database.Database;
import family.zambrana.starbound.database.DatabaseHolder;
import family.zambrana.starbound.fishing.CustomFishing;
import family.zambrana.starbound.homes.Homes;
import family.zambrana.starbound.nickname.GUIHandler;
import family.zambrana.starbound.nickname.NickManager;
import family.zambrana.starbound.nickname.NickManagerRegistry;
import family.zambrana.starbound.nickname.cmd.NickCmd;
import family.zambrana.starbound.staff.Staff;
import family.zambrana.starbound.teleporter.Teleporter;
import family.zambrana.starbound.util.SignGUI;
import family.zambrana.starbound.whitelist.Whitelist;
import org.bukkit.plugin.java.JavaPlugin;

public final class Starter extends JavaPlugin {

    private static Starter me;

    @Override
    public void onEnable() {
        // Plugin startup logic
        me = this;
        saveDefaultConfig();

        SignGUI.register();

        Database db = new Database();
        DatabaseHolder.set(db);
        CoreClientManager clientManager = new CoreClientManager(db);
        //getCommand("nick").setExecutor(new NickCmd(clientManager));

        StarboundChat chat = new StarboundChat(clientManager);

        //NickManager nicknames = new NickManager(this);
        //NickManagerRegistry.register(nicknames);
        //new GUIHandler();

        new Homes();
        new CustomFishing();
        new Teleporter(clientManager);
        new Whitelist(clientManager);
        new Staff(clientManager);

        // set any commands here after all the miniplugins are done loading
        getCommand("fly").setExecutor(new FlyCommand(clientManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        me = null;
    }

    public static Starter getInstance() { return me; }
}
