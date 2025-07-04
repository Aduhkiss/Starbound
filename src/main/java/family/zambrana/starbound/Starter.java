package family.zambrana.starbound;

import family.zambrana.starbound.chat.StarboundChat;
import family.zambrana.starbound.clientmanager.CoreClientManager;
import family.zambrana.starbound.cmd.FlyCommand;
import family.zambrana.starbound.cmd.NickCommand;
import family.zambrana.starbound.database.Database;
import family.zambrana.starbound.database.DatabaseHolder;
import family.zambrana.starbound.fishing.CustomFishing;
import family.zambrana.starbound.homes.Homes;
import family.zambrana.starbound.util.SignGUI;
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
        //getCommand("nick").setExecutor(new NickCommand(clientManager));

        StarboundChat chat = new StarboundChat(clientManager);
        new Homes();
        new CustomFishing();

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
