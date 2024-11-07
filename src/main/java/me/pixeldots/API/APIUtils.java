package me.pixeldots.API;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import me.pixeldots.BridgeRunner;
import me.pixeldots.Game.data.WorldBlockData;

public class APIUtils {

    public static void worldBlockDestroyed(Block block) {
        WorldBlockData data = new WorldBlockData(block.getType(), block.getLocation(), block.getBlockData());
        BridgeRunner.Variables.WorldBlocksDestroyed.add(data);
    }

    public static void addPlayerPlacedBlock(Location pos) {
        addPlayerPlacedBlock(new Vector(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()));
    }
    public static void addPlayerPlacedBlock(Vector pos) {
        BridgeRunner.Variables.BlocksPlaced.add(pos);
    }

    // Statistics
    public static int getPlayerKills(UUID uuid) {
        if (!BridgeRunner.Variables.PlayerStats.containsKey(uuid)) return 0;
        return BridgeRunner.Variables.PlayerStats.get(uuid).kills;
    }
    public static int getPlayerGoals(UUID uuid) {
        if (!BridgeRunner.Variables.PlayerStats.containsKey(uuid)) return 0;
        return BridgeRunner.Variables.PlayerStats.get(uuid).goals;
    }

}
