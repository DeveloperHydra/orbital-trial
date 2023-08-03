package wiki.hydra.orbital.commands;

import org.bukkit.command.CommandSender;
import wiki.hydra.orbital.Main;
import wiki.hydra.orbital.utils.text.Color;
import wiki.hydra.orbital.utils.command.Command;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("orbital", new String[]{"orbital"}, "Reload config", "");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.isOp()) {
                Main.getInstance().reloadConfig();
                sender.sendMessage(Color.translate(Main.getInstance()
                        .getConfig()
                        .getString("MESSAGES.CONFIG-RELOADED")));
            } else {
                sender.sendMessage(Color.translate(Main.getInstance()
                        .getConfig()
                        .getString("MESSAGES.NO-PERMISSION")));
            }
        } else {
            sender.sendMessage(Color.translate("&cUsage: /orbital reload"));
        }
    }
}
