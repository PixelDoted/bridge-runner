package me.pixeldots;

import org.apache.logging.log4j.Logger;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import me.pixeldots.Commands.*;
import me.pixeldots.Events.EventsListener;
import me.pixeldots.Events.PlayerEventListener;
import me.pixeldots.Game.AsyncBridgeGameTicker;
import me.pixeldots.Game.BridgeGame;
import me.pixeldots.Game.BridgeGameTicker;
import me.pixeldots.Game.VariableHandler;
import me.pixeldots.SaveData.DataHandler;
import me.pixeldots.Utils.Utils;

public class BridgeRunner extends JavaPlugin
{

    public static World world;

    public static BridgeGameTicker tickHandler;
    public static AsyncBridgeGameTicker asyncTickHandler;
    public static VariableHandler Variables;

	public static boolean isRunning = false;
    public static boolean isStarting = false;
    public static boolean isTesting = false;

    public static long StartingTime = 0;
    public static long GameStartedTime = 0;
    public static long GameEndTime = 0;
    public static long ReleaseTime = -1;

	public static String savePath = "/BridgeRunner.json";

    public static Logger logger;
    public static JavaPlugin instance;

    @Override
    public void onEnable() {
        logger = this.getLog4JLogger();
        instance = this;

        savePath = this.getDataFolder().getAbsolutePath() + savePath;
        BridgeConf.loadConf(this);

        Variables = new VariableHandler();

        DataHandler.Load(savePath);
        this.getCommand("bridgegame").setExecutor(new BridgeGameCommand());
        this.getCommand("bridgeteams").setExecutor(new BridgeTeamsCommand());

        getServer().getPluginManager().registerEvents(new EventsListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);

        tickHandler = new BridgeGameTicker();
        tickHandler.runTaskTimer(this, 0, 0);

        asyncTickHandler = new AsyncBridgeGameTicker();
        asyncTickHandler.runTaskTimerAsynchronously(this, 0, 0);

        if (BridgeConf.autoStartWithServer) {
            Utils.runDelayedTask(() -> {
                BridgeRunner.startGame();
            }, Utils.toTickTime(BridgeConf.autoStartDelay));
        }
    }

    @Override
    public void onDisable() {
        if (isRunning || isStarting) endGame();
    }

    public static void startGame() {
        if (isStarting || isRunning) Utils.Logger().info("A game is already running");
		Utils.Logger().info("Starting Bridge game");
        BridgeGame.start();
	}

    public static void runGame() {
        if (isStarting || isRunning) Utils.Logger().info("A game is already running");
		Utils.Logger().info("Starting Bridge game");
        BridgeGame.run();
	}

	public static void endGame() {
        if (!isStarting && !isRunning) Utils.Logger().info("There is no game running");
		Utils.Logger().info("Stopping Bridge game");
		BridgeGame.stop();
	}

}
