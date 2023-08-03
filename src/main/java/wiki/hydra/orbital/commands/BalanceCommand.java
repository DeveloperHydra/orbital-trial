package wiki.hydra.orbital.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wiki.hydra.orbital.Main;
import wiki.hydra.orbital.database.DatabaseManager;
import wiki.hydra.orbital.utils.command.Command;
import wiki.hydra.orbital.utils.text.Color;

public class BalanceCommand extends Command {
    private final DatabaseManager databaseManager;

    public BalanceCommand(DatabaseManager databaseManager) {
        super("bal", new String[]{"balance"}, "Display your money or the target's money", "orbital.balance");
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

        if (args.length == 0) {
            Player player = (Player) sender;
            displayBalance(player, player.getName());
        } else if (args.length == 1) {
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                sender.sendMessage(Color.translate(Main.getInstance()
                        .getConfig()
                        .getString("MESSAGES.NULL-PLAYER")));
                return;
            }
            displayBalance(sender, targetPlayer.getName());
        } else {
            sender.sendMessage(Color.translate("&cUsage: /bal [target]"));
        }
    }

    private void displayBalance(CommandSender sender, String targetUsername) {
        double balance = databaseManager.getBalance(targetUsername);
        String balanceMessage = Main.getInstance().getConfig().getString("MESSAGES.BALANCE-MESSAGE");
        String formattedBalance = String.format("%.2f", balance);
        String finalMessage = balanceMessage.replace("%balance%", formattedBalance).replace("%player%", targetUsername);
        sender.sendMessage(Color.translate(finalMessage));
    }
}
