package wiki.hydra.orbital.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wiki.hydra.orbital.Main;
import wiki.hydra.orbital.database.DatabaseManager;
import wiki.hydra.orbital.utils.text.Color;
import wiki.hydra.orbital.utils.command.Command;

public class SetBalCommand extends Command {
    private final DatabaseManager databaseManager;

    public SetBalCommand(DatabaseManager databaseManager) {
        super("setbal", new String[]{"setbalance"}, "Used to set money", "orbital.setbal");
        this.databaseManager = databaseManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Color.translate(Main.getInstance()
                            .getConfig()
                            .getString("MESSAGES.NO-PERMISSION")));
            return;
        }

        if (args.length != 2) {
            sender.sendMessage(Color.translate("&cUsage: /setbal <player> <amount>"));
            return;
        }

        String targetUsername = args[0];
        Player targetPlayer = Bukkit.getPlayer(targetUsername);
        if (targetPlayer == null) {
            sender.sendMessage(Color.translate(Main.getInstance()
                    .getConfig()
                    .getString("MESSAGES.NULL-PLAYER")));
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Color.translate(Main.getInstance()
                    .getConfig()
                    .getString("MESSAGES.INVALID-AMOUNT")));
            return;
        }

        if (amount < 0) {
            sender.sendMessage(Color.translate(Main.getInstance()
                    .getConfig()
                    .getString("MESSAGES.POSITIVE-AMOUNT")));
            return;
        }

        databaseManager.setBalance(targetPlayer.getName(), amount);
        sender.sendMessage(Color.translate(Main.getInstance()
                        .getConfig()
                        .getString("MESSAGES.SET-BALANCE"))
                        .replace("%player%", targetPlayer.getName())
                        .replace("%amount%", String.valueOf(amount)));
    }
}
