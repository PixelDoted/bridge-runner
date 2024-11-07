package me.pixeldots;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BridgeConf {
    
    // config
    public static int requiredPlayersToStart = 0;
    public static boolean autoStartWithServer = false;
    public static String autoStartDelay = "5s";

    public static boolean canDestroyWorld = false;
    public static boolean friendlyFire = false;
    public static boolean endless = false;

    public static int totalPoints = 5;

    public static String gameTime = "15m";

    public static FileConfiguration config;
    // config

    public static void loadConf(JavaPlugin plugin) {
        config = plugin.getConfig();
        
        config.addDefault("requiredPlayersToStart", requiredPlayersToStart);
        config.addDefault("autoStartWithServer", autoStartWithServer);
        config.addDefault("autoStartDelay", autoStartDelay);

        config.addDefault("canDestroyWorld", canDestroyWorld);
        config.addDefault("endless", endless);

        config.addDefault("totalPoints", 5);

        config.addDefault("gameTime", gameTime);
        plugin.saveConfig();

        requiredPlayersToStart = config.getInt("requiredPlayersToStart");
        autoStartWithServer = config.getBoolean("autoStartWithServer");
        autoStartDelay = config.getString("autoStartDelay");
        
        canDestroyWorld = config.getBoolean("canDestroyWorld");
        endless = config.getBoolean("endless");

        totalPoints = config.getInt("totalPoints");

        gameTime = config.getString("gameTime");
    }

}
