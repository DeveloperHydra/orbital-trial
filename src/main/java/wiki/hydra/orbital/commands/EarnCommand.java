package wiki.hydra.orbital.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wiki.hydra.orbital.Main;
import wiki.hydra.orbital.database.DatabaseManager;
import wiki.hydra.orbital.utils.text.Color;
import wiki.hydra.orbital.utils.command.Command;

import java.util.HashMap;
import java.util.Map;

public class EarnCommand extends Command {
    private final DatabaseManager databaseManager;
    private final Map<String, Long> lastEarnTimes;

    public EarnCommand(DatabaseManager databaseManager) {
        super("earn", new String[]{"earning"}, "Command to earn money", "orbital.earn");
        this.databaseManager = databaseManager;
        this.lastEarnTimes = new HashMap<>();
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
        long currentTime = System.currentTimeMillis();
        long lastEarnTime = lastEarnTimes.getOrDefault(player.getName(), 0L);

        if (currentTime - lastEarnTime < 60000) {
            sender.sendMessage(Color.translate(Main.getInstance()
                    .getConfig()
                    .getString("MESSAGES.EARN-COOLDOWN")));
            return;
        }

        int earnedAmount = (int) (Math.random() * 5) + 1;
        double currentBalance = databaseManager.getBalance(player.getName());
        double newBalance = currentBalance + earnedAmount;

        databaseManager.addBalance(player.getName(), newBalance);
        sender.sendMessage(Color.translate(Main.getInstance()
                .getConfig()
                .getString("MESSAGES.EARN-MESSAGE")
                .replace("%earnedAmount%", String.valueOf(earnedAmount))
                .replace("%newBalance%", String.valueOf(newBalance))));

        lastEarnTimes.put(player.getName(), currentTime);
    }
}