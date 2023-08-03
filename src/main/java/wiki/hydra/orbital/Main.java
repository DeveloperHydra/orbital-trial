package wiki.hydra.orbital;

import org.bukkit.plugin.java.JavaPlugin;

import wiki.hydra.orbital.commands.*;
import wiki.hydra.orbital.database.DatabaseManager;

public class Main extends JavaPlugin {
    private DatabaseManager databaseManager;
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();

        databaseManager = new DatabaseManager(this);
        databaseManager.connectDatabase();

        registerCommands();
    }

    @Override
    public void onDisable() {
        databaseManager.closeDatabase();
    }

    public void registerCommands() {
        new BalanceCommand(databaseManager);
        new SetBalCommand(databaseManager);
        new EarnCommand(databaseManager);
        new GiveCommand(databaseManager);
        new ReloadCommand();
    }

    public static Main getInstance() {
        return instance;
    }
}
