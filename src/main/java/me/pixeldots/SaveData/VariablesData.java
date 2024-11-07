package me.pixeldots.SaveData;

import me.pixeldots.Game.VariableHandler;

public class VariablesData {

    public int[] AreaStart;
    public int[] AreaEnd;
    public int[] LobbyPosition;

    public float[] redCage;
    public float[] redSpawn;
    public float[] redGoal;

    public float[] blueCage;
    public float[] blueSpawn;
    public float[] blueGoal;

    public VariablesData(VariableHandler handler) {
        this.AreaStart = new int[] {handler.AreaStart.getBlockX(), handler.AreaStart.getBlockY(), handler.AreaStart.getBlockZ()};
        this.AreaEnd = new int[] {handler.AreaEnd.getBlockX(), handler.AreaEnd.getBlockY(), handler.AreaEnd.getBlockZ()};
        this.LobbyPosition = new int[] {handler.LobbyPosition.getBlockX(), handler.LobbyPosition.getBlockY(), handler.LobbyPosition.getBlockZ()};

        redCage = new float[] {(float)handler.redCageCenter.pos.getX(), (float)handler.redCageCenter.pos.getY(), (float)handler.redCageCenter.pos.getZ(), handler.redCageCenter.pitch, handler.redCageCenter.yaw};
        redSpawn = new float[] {(float)handler.redSpawn.pos.getX(), (float)handler.redSpawn.pos.getY(), (float)handler.redSpawn.pos.getZ(), handler.redSpawn.pitch, handler.redSpawn.yaw};
        redGoal = new float[] {(float)handler.redGoal.getX(), (float)handler.redGoal.getY(), (float)handler.redGoal.getZ()};
        
        blueCage = new float[] {(float)handler.blueCageCenter.pos.getX(), (float)handler.blueCageCenter.pos.getY(), (float)handler.blueCageCenter.pos.getZ(), handler.blueCageCenter.pitch, handler.blueCageCenter.yaw};
        blueSpawn = new float[] {(float)handler.blueSpawn.pos.getX(), (float)handler.blueSpawn.pos.getY(), (float)handler.blueSpawn.pos.getZ(), handler.blueSpawn.pitch, handler.blueSpawn.yaw};
        blueGoal = new float[] {(float)handler.blueGoal.getX(), (float)handler.blueGoal.getY(), (float)handler.blueGoal.getZ()};
    }
    
}
