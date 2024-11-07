package me.pixeldots.API;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.pixeldots.API.Events.GameEndEvent;
import me.pixeldots.API.Events.PlayerScoreEvent;

public class APIEventCaller {

    public static void gameEnd(int teamWon, List<UUID> won, List<UUID> lost) {
        GameEndEvent data = new GameEndEvent(teamWon, won, lost);
        Bukkit.getServer().getPluginManager().callEvent(data);
    }

    public static void playerScore(int teamID, Player player) {
        PlayerScoreEvent data = new PlayerScoreEvent(teamID, player);
        Bukkit.getServer().getPluginManager().callEvent(data);
    }

}
