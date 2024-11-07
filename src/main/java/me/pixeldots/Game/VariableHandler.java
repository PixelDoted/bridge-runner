package me.pixeldots.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.util.Vector;

import me.pixeldots.Game.data.*;

public class VariableHandler {

    public Vector AreaStart = new Vector(0,0,0);
    public Vector AreaEnd = new Vector(0,0,0);
    public Vector LobbyPosition = new Vector(0,0,0);

    public DirectionVector redSpawn;
    public DirectionVector blueSpawn;

    public DirectionVector redCageCenter;
    public DirectionVector blueCageCenter;

    public Vector redGoal;
    public Vector blueGoal;

    // Game Properties
    public Map<UUID, PlayerStatistics> PlayerStats = new HashMap<>();
    public List<UUID> PlayersInGame = new ArrayList<>();

    public List<WorldBlockData> WorldBlocksDestroyed = new ArrayList<>();
    public List<Vector> BlocksPlaced = new ArrayList<>();

    public int redPoints = 0;
    public int bluePoints = 0;

    public Vector AreaMin = new Vector(0,0,0);
    public Vector AreaMax = new Vector(0,0,0);

    public DirectionVector getTeamSpawn(int team) {
        switch (team) {
            case 0:
                return redSpawn;
            case 1:
                return blueSpawn;
            default:
                return new DirectionVector(0,0,0,0,0);
        }
    }
    public DirectionVector getTeamCage(int team) {
        switch (team) {
            case 0:
                return redCageCenter;
            case 1:
                return blueCageCenter;
            default:
                return new DirectionVector(0,0,0,0,0);
        }
    }
    
}
