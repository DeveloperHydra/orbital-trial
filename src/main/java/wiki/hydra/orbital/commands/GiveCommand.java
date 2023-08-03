package wiki.hydra.orbital.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wiki.hydra.orbital.Main;
import wiki.hydra.orbital.database.DatabaseManager;
import wiki.hydra.orbital.utils.text.Color;
import wiki.hydra.orbital.utils.command.Command;

public class GiveCommand extends Command {
    private final DatabaseManager databaseManager;

    public GiveCommand(DatabaseManager databaseManager) {
        super("give", new String[]{"givemoney"}, "Give money to players", "orbital.give");
        this.databaseManager = databaseManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Color.translate(Main.getInstance()
                    .getConfig()
                    .getString("MESSAGES.MUST-BE-PLAYER")));
            return;
        }

        Player player = (Player) sender;

        if (args.length != 2) {
            sender.sendMessage(Color.translate("&cUsage: /give <target> <amount>"));
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

        if (player.getName().equalsIgnoreCase(targetUsername)) {
            sender.sendMessage(Color.translate(Main.getInstance()
                    .getConfig()
                    .getString("MESSAGES.CANNOT-GIVE-TO-SELF")));
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

        if (amount <= 0) {
            sender.sendMessage(Color.translate(Main.getInstance()
                    .getConfig()
                    .getString("MESSAGES.POSITIVE-AMOUNT")));
            return;
        }

        double senderBalance = databaseManager.getBalance(player.getName());
        if (amount > senderBalance) {
            sender.sendMessage(Color.translate(Main.getInstance()
                    .getConfig()
                    .getString("MESSAGES.NOT-ENOUGH-MONEY")));
            return;
        }

        double targetBalance = databaseManager.getBalance(targetUsername);
        targetBalance += amount;
        senderBalance -= amount;

        databaseManager.setBalance(player.getName(), senderBalance);
        databaseManager.setBalance(targetUsername, targetBalance);

        sender.sendMessage(Color.translate(Main.getInstance()
                .getConfig()
                .getString("MESSAGES.SUCCESSFUL-TRANSFER")
                .replace("%amount%", String.valueOf(amount))
                .replace("%target%", targetUsername)));
    }
}
