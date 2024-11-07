package me.pixeldots.Game;

import org.bukkit.scheduler.BukkitRunnable;

import me.pixeldots.BridgeRunner;
import me.pixeldots.Utils.Utils;

public class BridgeGameTicker extends BukkitRunnable {

    @Override
    public void run() {
        long currentTime = Utils.getDateTime();

        if (!(BridgeRunner.isRunning && BridgeRunner.world != null)) return;
        try {
            BridgeGame.TickPlayers(currentTime);
            BridgeGame.TickExtras(currentTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}