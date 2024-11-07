package me.pixeldots.Events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import me.pixeldots.BridgeRunner;
import me.pixeldots.Game.BridgeGame;
import me.pixeldots.Game.data.DirectionVector;
import me.pixeldots.Game.data.PlayerStatistics;
import me.pixeldots.Scoreboard.GameScoreboardUtils;
import me.pixeldots.Utils.Utils;

public class PlayerEventListener implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();

        int team = BridgeRunner.Variables.PlayerStats.get(player.getUniqueId()).team;
        DirectionVector vector;

        if (team == 0) vector = BridgeRunner.Variables.redSpawn;
        else vector = BridgeRunner.Variables.blueSpawn;
        
        player.getInventory().clear();
        BridgeGame.givePlayerDefaultItems(player, player.getInventory(), team);

        Location location = new Location(player.getWorld(), vector.pos.getX(), vector.pos.getY(), vector.pos.getZ());
        location.setYaw(vector.yaw);
        location.setPitch(vector.pitch);
        e.setRespawnLocation(location);
    }

    @EventHandler
    public void onPlayerCraft(CraftItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerSaturation(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() == Material.GOLDEN_APPLE)
            e.getPlayer().setHealth(e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Player player = (Player)e.getEntity().getShooter();
            if (BridgeRunner.Variables.PlayerStats.containsKey(player.getUniqueId())) {
                PlayerStatistics stats = BridgeRunner.Variables.PlayerStats.get(player.getUniqueId());
                stats.usedArrow = Utils.getDateTime()+Utils.toMillisecondTime("3s");
                stats.hasArrow = false;
            }
        }
    }

    @EventHandler
    public void onPlayerEntityPortal(PlayerPortalEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Entity killer = e.getPlayer().getKiller();
        if (killer != null) {
            if (killer instanceof Arrow) {
                ProjectileSource shooter = ((Arrow)killer).getShooter();
                if (shooter instanceof Player) killer = (Player)shooter;
            }
            if (!(killer instanceof Player)) return;
            BridgeRunner.Variables.PlayerStats.get(((Player)killer).getUniqueId()).kills++;
            GameScoreboardUtils.UpdateStatistics((Player)killer);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (BridgeRunner.world == null) BridgeRunner.world = player.getWorld();
        if (BridgeRunner.isRunning) { 
            GameScoreboardUtils.RegisterPlayer(player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.damage(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*2);
                }
            }.runTaskLater(BridgeRunner.instance, 2);
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (BridgeRunner.isRunning) {
            UUID plrUUID = e.getPlayer().getUniqueId();
            PlayerStatistics plrStats = BridgeRunner.Variables.PlayerStats.get(plrUUID);
            for (int i = 0; i < BridgeRunner.Variables.PlayersInGame.size(); i++) {
                UUID uuid = BridgeRunner.Variables.PlayersInGame.get(i);
                PlayerStatistics stats = BridgeRunner.Variables.PlayerStats.get(uuid);
                if (uuid != plrUUID && stats.team == plrStats.team && Bukkit.getPlayer(uuid) != null) return;
            }
            BridgeGame.RandomEndGame();
        }
    }

}
