package me.pixeldots.Scoreboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;

import me.pixeldots.BridgeRunner;

public class ScoreboardUtils {

    public static Map<UUID, Objective> Sidebar = new HashMap<>();

    public static void RegisterLobbyBoard() {
        LobbyScoreboardUtils.Register();
    }

    public static void UpdateLobbyTimer() {
        LobbyScoreboardUtils.UpdateTimer();
    }

    public static void RegisterGameBoard() {
        GameScoreboardUtils.Register();
    }

    public static void UpdateTeamsboard() {
        //GameScoreboardUtils.UpdateTeamsboard();
    }

    public static void UpdateGameTimer() {
        List<Player> players = BridgeRunner.world.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            GameScoreboardUtils.UpdateTimer(players.get(i));
        }
    }
    
}
