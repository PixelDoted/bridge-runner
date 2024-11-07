package me.pixeldots.API.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerScoreEvent extends Event implements Cancellable {

    private int teamID;
    private Player player;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public PlayerScoreEvent(int _teamID, Player _player) {
        this.teamID = _teamID;
        this.player = _player;
    }

    public int getTeamID() {
        return teamID;
    }
    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean arg0) {
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
    
}
