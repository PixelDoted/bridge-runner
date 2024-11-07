package me.pixeldots.Game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import me.pixeldots.BridgeRunner;
import me.pixeldots.Scoreboard.ScoreboardUtils;

public class AsyncBridgeGameTicker extends BukkitRunnable {

    public static int fiveTickCounter = 0;
    public static List<AsyncGameAction> actions = new ArrayList<>();

    @Override
    public void run() {
        if (BridgeRunner.isRunning || BridgeRunner.isStarting) {
            fiveTickCounter++;
            if (fiveTickCounter >= 5) {
                ScoreboardUtils.UpdateGameTimer();
                fiveTickCounter = 0;
            }
            for (int i = actions.size()-1; i >= 0; i--) {
                AsyncGameAction action = actions.get(i);
                action.run();
                actions.remove(action);
            }
        }
    }

    public static interface AsyncGameAction {
        public void run();
    }
    
}