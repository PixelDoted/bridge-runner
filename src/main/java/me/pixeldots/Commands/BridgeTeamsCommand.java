package me.pixeldots.Commands;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Bed;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import me.pixeldots.BridgeRunner;
import me.pixeldots.Game.data.DirectionVector;
import me.pixeldots.Utils.BlockUtils;
import me.pixeldots.Utils.Utils;

public class BridgeTeamsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String raw, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player)sender;
        
        String team = args[1];

        Location location = player.getLocation();
        Vector pos = new Vector(location.getX(), location.getY(), location.getZ());
        DirectionVector vector = new DirectionVector(pos, location.getPitch(), location.getYaw());

        if (args[0].equalsIgnoreCase("setCage")) {
            if (team.equalsIgnoreCase("red")) BridgeRunner.Variables.redCageCenter = vector;
            else BridgeRunner.Variables.blueCageCenter = vector;
        } else if (args[0].equalsIgnoreCase("setSpawn")) {
            if (team.equalsIgnoreCase("red")) BridgeRunner.Variables.redSpawn = vector;
            else BridgeRunner.Variables.blueSpawn = vector;
        } else if (args[0].equalsIgnoreCase("setGoal")) {
            if (team.equalsIgnoreCase("red")) BridgeRunner.Variables.redGoal = pos;
            else BridgeRunner.Variables.blueGoal = pos;
        } else if (args[0].equalsIgnoreCase("help")) {
            player.sendMessage(Utils.text("setCage <team color>, sets a team cage"));
            player.sendMessage(Utils.text("setSpawn <team color>, sets a team spawn"));
            player.sendMessage(Utils.text("setGoal <team color>, sets a team goal"));
        }
        return false;
    }
    
}
