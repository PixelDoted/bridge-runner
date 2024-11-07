package me.pixeldots.Scoreboard;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import me.pixeldots.BridgeConf;
import me.pixeldots.BridgeRunner;
import me.pixeldots.Game.data.PlayerStatistics;
import me.pixeldots.Utils.Utils;
import net.kyori.adventure.text.format.TextColor;

public class GameScoreboardUtils {

    public static int teamPosDifference = 0;
    public static int timerPosDifference = 0;
    public static int statisticsPosDifference = 0;

    public static void Register() {
        List<Player> players = BridgeRunner.world.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            RegisterPlayer(players.get(i));
        }
    }

    public static void RegisterPlayer(Player player) {
        BuildScoreboard builder = new BuildScoreboard(Utils.text(ChatColor.BOLD + "BRIDGE", TextColor.color(255, 255, 0)));
        UpdateSideboard(player, builder);

        {
            Scoreboard scoreboard = builder.build().scoreboard();

            Objective BelowNameBoard = scoreboard.registerNewObjective("nameHealth", "health", Utils.text("Health"));
            BelowNameBoard.setDisplaySlot(DisplaySlot.BELOW_NAME);

            Objective ListBoard = scoreboard.registerNewObjective("listHealth", "health", Utils.text("Bedwars"));
            ListBoard.setDisplaySlot(DisplaySlot.PLAYER_LIST);

            builder.scoreboard(scoreboard);
        }

        ScoreboardUtils.Sidebar.put(player.getUniqueId(), builder.objective);
        builder.send(player);
    }

    public static void UpdateStatistics(Player player) {
        int kills = statisticsPosDifference-1;
        int goals = statisticsPosDifference;

        PlayerStatistics stats = BridgeRunner.Variables.PlayerStats.get(player.getUniqueId());
        Objective sidebar = ScoreboardUtils.Sidebar.get(player.getUniqueId());
        BuildScoreboard.replaceScore(sidebar, kills, "Kills: " + ChatColor.GREEN + stats.kills);
        BuildScoreboard.replaceScore(sidebar, goals, "Goals: " + ChatColor.GREEN + stats.goals);
    }

    public static void UpdateTimer(Player player) {
        Objective sidebar = ScoreboardUtils.Sidebar.get(player.getUniqueId());

        if (BridgeConf.endless) BuildScoreboard.replaceScore(sidebar, timerPosDifference, "Time Played: " + Utils.formatTimer(Utils.getDateTime()-BridgeRunner.GameStartedTime));
        else BuildScoreboard.replaceScore(sidebar, timerPosDifference, "Time Left: " + Utils.formatTimer(BridgeRunner.GameEndTime-Utils.getDateTime()));
    }
    public static void UpdateTeamsboard(Player player) {
        Objective sidebar = ScoreboardUtils.Sidebar.get(player.getUniqueId());
        String totalPoints = BridgeConf.totalPoints > 0 ? "/" + BridgeConf.totalPoints : "";

        BuildScoreboard.replaceScore(sidebar, teamPosDifference, ChatColor.BLUE + "BLUE: " + BridgeRunner.Variables.bluePoints + totalPoints);
        BuildScoreboard.replaceScore(sidebar, teamPosDifference-1, ChatColor.RED + "RED: " + BridgeRunner.Variables.redPoints + totalPoints);
    }
    
    public static void UpdateSideboard(Player player, BuildScoreboard builder) {
        UUID uuid = player.getUniqueId();

        builder.add(ChatColor.YELLOW + Bukkit.getIp());
        builder.blankLine();
        
        PlayerStatistics stats = BridgeRunner.Variables.PlayerStats.get(uuid);
        builder.add("Kills: " + ChatColor.GREEN + stats.kills);
        builder.add("Goals: " + ChatColor.GREEN + stats.goals);
        statisticsPosDifference = builder.lines.size()-1;
        builder.blankLine();

        String totalPoints = BridgeConf.totalPoints > 0 ? "/" + BridgeConf.totalPoints : "";
        builder.add(ChatColor.RED + "RED: 0" + totalPoints);
        builder.add(ChatColor.BLUE + "BLUE: 0" + totalPoints);
        teamPosDifference = builder.lines.size()-1;

        builder.blankLine();

        //String time = Utils.formatTimer(1);
        //builder.add(BridgeRunner.Variables.WorldInfo.worldEventName + " in " + ChatColor.GREEN + time);
        builder.add("Time Left: 1:00");
        timerPosDifference = builder.lines.size()-1;

        builder.blankLine();
        builder.add(ChatColor.GRAY + LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yy")) + ChatColor.DARK_GRAY + " m000A");
    }

}
