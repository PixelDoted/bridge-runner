package me.pixeldots.Commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import me.pixeldots.BridgeRunner;
import me.pixeldots.SaveData.DataHandler;
import me.pixeldots.Utils.Utils;

public class BridgeGameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String raw, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player)sender;

        if (args[0].equalsIgnoreCase("start")) {
            if (args.length > 1 && args[1].equalsIgnoreCase("test")) BridgeRunner.isTesting = true;
            else BridgeRunner.isTesting = false;

            player.sendMessage("Starting Bridge match" + (BridgeRunner.isTesting ? " (Testing)" : ""));
            BridgeRunner.world = player.getWorld();
            BridgeRunner.startGame();
        } else if (args[0].equalsIgnoreCase("stop")) {
            player.sendMessage("Stopping Bridge match");
            BridgeRunner.endGame();
        } else if (args[0].equalsIgnoreCase("run")) {
            if (args.length > 1 && args[1].equalsIgnoreCase("test")) BridgeRunner.isTesting = true;
            else BridgeRunner.isTesting = false;

            player.sendMessage("Running Bridge match" + (BridgeRunner.isTesting ? " (Testing)" : ""));
            BridgeRunner.world = player.getWorld();
            BridgeRunner.runGame();
        } else if (args[0].equalsIgnoreCase("save")) {
            player.sendMessage("Saving Bridge match");
            DataHandler.Save(BridgeRunner.savePath);
        } else if (args[0].equalsIgnoreCase("setAreaStart")) {
            Location pos = player.getLocation();
            Vector vec = new Vector(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
            BridgeRunner.Variables.AreaStart = vec;
        } else if (args[0].equalsIgnoreCase("setAreaEnd")) {
            Location pos = player.getLocation();
            Vector vec = new Vector(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
            BridgeRunner.Variables.AreaEnd = vec;   
        } else if (args[0].equalsIgnoreCase("setLobby")) {
            Location pos = player.getLocation();
            Vector vec = new Vector(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
            BridgeRunner.Variables.LobbyPosition = vec;
        } else if (args[0].equalsIgnoreCase("help")) {
            player.sendMessage(Utils.text("start, Starts a bridge match"));
            player.sendMessage(Utils.text("stop, Stops a bridge match"));
            player.sendMessage(Utils.text("run, Starts a bridge match without the lobby"));
            player.sendMessage(Utils.text("save, Saves the bridge settings (teams, generators, etc)"));
            player.sendMessage(Utils.text("setAreaStart, sets the start position of the bridge Area"));
            player.sendMessage(Utils.text("setAreaEnd, sets the end position of the bridge Area"));
            player.sendMessage(Utils.text("setLobby, sets the lobby position"));
        }
        return true;
    }
    
}
