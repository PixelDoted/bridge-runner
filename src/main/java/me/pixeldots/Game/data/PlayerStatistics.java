package me.pixeldots.Game.data;

public class PlayerStatistics {

    public int kills = 0;
    public int goals = 0;
    public int team = -1;

    public long usedArrow = 0;
    public boolean hasArrow = true;
    public String lastTitle = null;

    public PlayerStatistics() {}
    public PlayerStatistics(int _team) {
        this.team = _team;
    }
    

}
