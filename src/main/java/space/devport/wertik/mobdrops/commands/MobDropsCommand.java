package space.devport.wertik.mobdrops.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import space.devport.wertik.mobdrops.Main;

public class MobDropsCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Main.inst.getCfg().getColoredListString("Messages.help")
            .replace("%prefix%", Main.inst.cO.getPrefix())
            .replace("%version%", Main.inst.getDescription().getVersion()));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                Main.inst.reload(sender);
                break;
            default:
            case "help":
                sender.sendMessage(Main.inst.getCfg().getColoredListString("Messages.help")
                        .replace("%prefix%", Main.inst.cO.getPrefix())
                        .replace("%version%", Main.inst.getDescription().getVersion()));
        }

        return false;
    }
}
