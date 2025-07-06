package family.zambrana.starbound.staff.cmd;

import family.zambrana.starbound.clientmanager.CoreClient;
import family.zambrana.starbound.clientmanager.CoreClientManager;
import family.zambrana.starbound.staff.Staff;
import family.zambrana.starbound.util.Rank;
import family.zambrana.starbound.util.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StaffCCmd implements CommandExecutor {
    private CoreClientManager clientManager;
    private Staff service;
    public StaffCCmd(CoreClientManager clientManager, Staff service) {
        this.clientManager = clientManager;
        this.service = service;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        //TODO: Do some bullshit check to see if the sender is the console and do some bullshit formatting for it, since it's not a child of Player

        // make a call to the core client manager to make sure that the caller of this command actually is allowed to
        Player caller = (Player) sender;
        if(!clientManager.getClient(caller).has(Rank.HELPER)) {
            caller.sendMessage("§cYou are not allowed to use this.");
            return false;
        }

        // okay now that we know they are a staff member, lets make sure they wrote something
        if(args.length == 0 || args == null) {
            caller.sendMessage("§cUsage: /s <Message>");
            return false;
        }
        String message = StringUtil.combine(args, 0);

        // then blast off the message to any online staff members lol
        service.send(caller, message);

        //TODO: Add proxy support for like BungeeCord or Velocity or what ever the fuck the minecraft community uses now
        return true;
    }
}
